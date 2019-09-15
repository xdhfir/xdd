package com.my.yeshttp.utils;

import com.my.yeshttp.base.DownLoadService;
import com.my.yeshttp.download.ProgressHandler;
import com.my.yeshttp.download.ProgressHelper;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 进行下载的网络访问封装类
 */
public class DownLoadMethod {
    private Retrofit retrofit;
    private final OkHttpClient.Builder builder;
    private static DownLoadMethod downLoadMethod;
    private Call<ResponseBody> call;

    private DownLoadMethod(String startUrl) throws Exception{
        builder = ProgressHelper.addProgress(null);
        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(startUrl)
                .build();
    }

    public void downLoadApk(ProgressHandler progressHandler, Callback<ResponseBody> callback, String endUrl) {
        DownLoadService downLoadApkService = retrofit.create(DownLoadService.class);
        ProgressHelper.setProgressHandler(progressHandler);
        call = downLoadApkService.retrofitDownload(endUrl);
        call.enqueue(callback);
    }

    public void downLoadCancel(){
        if(call!=null&&!call.isCanceled()){
            call.cancel();
        }
    }

    public static DownLoadMethod getInstance(String startUrl) throws Exception{
        if (downLoadMethod == null) {
            synchronized (DownLoadMethod.class) {
                if (downLoadMethod == null) {
                    downLoadMethod = new DownLoadMethod(startUrl);
                }
            }
        }
        return downLoadMethod;
    }
}
