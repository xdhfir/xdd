package com.my.yeshttp.glide;


import com.my.yeshttp.ok.MHostnameVerifier;
import com.my.yeshttp.ok.MX509TrustManager;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import okhttp3.OkHttpClient;

/**
 *
 */
public class UnsafeOkHttpClient {
    public static OkHttpClient.Builder getUnsafeOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{new MX509TrustManager()}, new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        builder.sslSocketFactory(sslContext.getSocketFactory());
        builder.hostnameVerifier(new MHostnameVerifier());
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        return builder;
    }
}
