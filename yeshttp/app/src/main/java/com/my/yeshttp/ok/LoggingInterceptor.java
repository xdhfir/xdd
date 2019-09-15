package com.my.yeshttp.ok;


import com.my.baselib.lib.utils.LogUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 日志拦截器
 */
public class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        long t1 = System.nanoTime();
        /*请求头*/
        Headers headers = request.headers();
        Set<String> names = headers.names();
        HashMap<String, String> map = new HashMap<>();
        for (String name : names) {
            map.put(name, headers.get(name));
        }
        /*请求方法*/
        String method = request.method();
        /*请求体*/
        RequestBody body = request.body();
        HashMap<String, String> map1 = new HashMap<>();
        HashMap<String, String> map2 = new HashMap<>();
        if (body instanceof FormBody) {
            FormBody b = (FormBody) request.body();
            if (b.size() > 0) {
                //封装参数
                for (int i = 0; i < b.size(); i++) {
                    map1.put(b.encodedName(i), b.encodedValue(i));
                    map2.put(b.name(i), b.value(i));
                }
            }
        }
        /*请求*/
        String url=request.url().toString();
        logRequest(url, headers, method, map1, map2);

        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        /*返回*/
        okhttp3.MediaType mediaType = response.body().contentType();
        String content = response.body().string();
        Headers responseHeader = response.headers();
        logResponse(url, content, responseHeader);
        //捕捉登录信息丢失状态，跳转到登录页面
        checkLoginStatus(content);
        return response.newBuilder()
                .body(okhttp3.ResponseBody.create(mediaType, content))
                .build();
    }

    private void logResponse(final String url, final String content, final Headers responseHeader) {
        new Thread(){
            @Override
            public void run() {
                LogUtils.i("RESPONSE", "\n|||URL===" + url
                        + "\n|||HEAD===" + responseHeader
                        + "\n|||BODY===" + content);
            }
        }.start();
    }

    private void logRequest(final String url, final Headers headers, final String method, final HashMap<String, String> map1, final HashMap<String, String> map2) {
        new Thread(){
            @Override
            public void run() {
                LogUtils.w("REQUEST", "\n|||URL===" + url
                        + "\n|||HEAD===" + headers
                        + "\n|||METHOD===" + method
                        + "\n|||MAP1===" + map1.toString()
                        + "\n|||MAP2===" + map2.toString());
            }
        }.start();
    }

    /**
     * @time 2017/3/3 10:23
     * @desc 判断是否登录状态已经失效
     */
    private void checkLoginStatus(String content) {

    }
}
