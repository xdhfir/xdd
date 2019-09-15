package com.my.baselib.lib.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
/**
 * 这是一个获取当前应用版本号的工具类
 */
public class VersionUtils {
    //获取当前版本号
    public static int getCurrentVersion() {
        PackageManager pm = UIUtils.getContext().getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(UIUtils.getContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(pi==null){
            return 0;
        }
        return pi.versionCode;
    }
    public static String getCurrentVersionName() {
        PackageManager pm = UIUtils.getContext().getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(UIUtils.getContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(pi==null){
            return "1.0.0";
        }
        return pi.versionName;
    }
}
