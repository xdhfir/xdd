package com.my.yeshttp.utils;

import android.content.Context;


import com.my.baselib.lib.utils.UIUtils;
import com.my.yeshttp.ok.MCookieJar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
/**
 *  图片下载工具类，（利用okHttp）同步COOKIE
 */
public class IDLUtils {
    private final static int DEFAULT_TIME = 30;
    private final OkHttpClient build;

    public IDLUtils() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIME, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_TIME, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_TIME, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(false);
        builder.cookieJar(new MCookieJar());
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("Connection", "close")
                        .build();
                return chain.proceed(request);
            }
        });
        build = builder.build();
    }

    public void downloadImg(final Context context, String url, final ImgDownLoadListener listener) {
        Request.Builder builder = new Request.Builder().url(url);
        builder.method("GET", null);
        Request request = builder.build();
        Call call = build.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream = null;
                final String path = context.getExternalCacheDir() +""+System.currentTimeMillis()+"checkCode.jpg";
                try {
                    File file = new File(path);
                    if(file.exists()){
                        file.getAbsoluteFile().delete();
                    }
                    fileOutputStream = new FileOutputStream(file);
                    byte[] buffer = new byte[2048];
                    int len = 0;
                    while ((len = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, len);
                    }
                    fileOutputStream.flush();
                    UIUtils.postTaskSafely(new Runnable() {
                        @Override
                        public void run() {
                            if (listener != null) {
                                listener.download(path);
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public interface ImgDownLoadListener {
        void download(String path);
    }
}
