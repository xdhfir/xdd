package com.my.yeshttp.utils;


import com.my.yeshttp.ok.HeaderInterceptor;
import com.my.yeshttp.ok.LoggingInterceptor;
import com.my.yeshttp.ok.MCookieJar;
import com.my.yeshttp.ok.MHostnameVerifier;
import com.my.yeshttp.ok.MX509TrustManager;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 封装网络访问类
 */
public class HttpMethod {
    private Retrofit retrofit;
    private int DEFAULT_TIME = 20;
    private static HttpMethod httpMethod;

    //构造函数
    private HttpMethod(String baseUrl) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIME, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_TIME, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_TIME, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(false);
        builder.cookieJar(new MCookieJar());
        //忽略证书验证
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{new MX509TrustManager()}, new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        if(sslContext!=null){
            builder.sslSocketFactory(sslContext.getSocketFactory());
        }
        builder.hostnameVerifier(new MHostnameVerifier());
        builder.addInterceptor(new HeaderInterceptor());
        builder.addInterceptor(new LoggingInterceptor());
        OkHttpClient build = builder.build();
        retrofit = new Retrofit.Builder()
                .client(build)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }

    public Retrofit getBuilder(){
        return retrofit;
    }


    //通过一个单例模式返回当前类的实例对象
    public static HttpMethod getInstance(String baseUrl) {
        if (httpMethod == null) {
            synchronized (HttpMethod.class) {
                if (httpMethod == null) {
                    httpMethod = new HttpMethod(baseUrl);
                }
            }
        }
        return httpMethod;
    }

    public void setTime(int time,String baseUrl){
        this.DEFAULT_TIME = time;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIME, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_TIME, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_TIME, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(false);
        builder.cookieJar(new MCookieJar());
        //忽略证书验证
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{new MX509TrustManager()}, new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        if(sslContext!=null){
            builder.sslSocketFactory(sslContext.getSocketFactory());
        }
        builder.hostnameVerifier(new MHostnameVerifier());
        builder.addInterceptor(new LoggingInterceptor());
        builder.addInterceptor(new HeaderInterceptor());
        OkHttpClient build = builder.build();
        retrofit = new Retrofit.Builder()
                .client(build)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }

    private int getTime(){
        return DEFAULT_TIME;
    }
}
