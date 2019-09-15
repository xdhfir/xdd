package com.my.yeshttp.httpmanager;

import com.my.yeshttp.httpconfig.ApiService;
import com.my.yeshttp.httpconfig.MURL;
import com.my.yeshttp.utils.HttpUtils;

/**
 * create by Administrator at 2017/4/6
 * description:
 */
public class ApiUtils {
    public static ApiService getService(){
        return (ApiService) HttpUtils.register(MURL.BASE_URL,ApiService.class);
    }
}
