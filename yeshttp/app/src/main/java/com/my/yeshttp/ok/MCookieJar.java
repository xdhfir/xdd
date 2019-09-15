package com.my.yeshttp.ok;

import com.my.baselib.lib.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Cookie管理类
 */
public class MCookieJar implements CookieJar {
    public static List<Cookie> mCookie;
    public static HashMap<String, Cookie> map = new HashMap<>();

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        for (Cookie c : cookies) {
            map.put(c.name(), c);
        }
        if (mCookie == null) {
            mCookie = new ArrayList<>();
        }
        mCookie.clear();
        for (String s : map.keySet()) {
            mCookie.add(map.get(s));
        }
        LogUtils.f("cookieJar", mCookie.toString());
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        if (mCookie == null) {
            mCookie= new ArrayList<Cookie>();
        }
        LogUtils.i("REQUEST_COOKIE","URL="+url.toString()+"\n"+"cookie="+mCookie.toString());
        return mCookie;
    }
}
