package com.my.yeshttp.ok;


import com.my.baselib.lib.utils.SPUtils;
import com.my.baselib.lib.utils.VersionUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 *增加头信息
 */
public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        String appId = "";//SPUtils.getString("appId", "");
        String version =""; //VersionUtils.getCurrentVersionName();
        Request request = null;
        if (appId == null || appId.equals("")) {
            request = chain.request().newBuilder()
                    .addHeader("Connection", "close")
                    .addHeader("Referer", "https://baidu.cn")
                    .addHeader("type", "1")
                    .addHeader("isH5", "0")
                    .build();
        } else {
            request = chain.request().newBuilder()
                    .addHeader("Connection", "close")
                    .addHeader("Referer", "https://baidu.cn")
                    .addHeader("appId", appId)
                    .addHeader("version", version)
                    .addHeader("type", "1")
                    .addHeader("isH5", "0")
                    .build();
        }
        return chain.proceed(request);
    }
}
