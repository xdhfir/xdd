package com.my.baselib.lib.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import java.util.List;

import static android.os.Build.VERSION.SDK;

/**
 * create by Administrator at 2017/4/11
 * description: 权限判断工具类
 */
public class PermissionUtils {
    private static final String CALL_PHONE = "android.permission.CALL_PHONE";

    private static final String WRITE_SD = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static final String READ_SD = "android.permission.READ_EXTERNAL_STORAGE";

    private static final String CALL_LOG = "android.permission.READ_CALL_LOG";

    private static final String GPS_LOCATION = "android.permission.ACCESS_COARSE_LOCATION";
    private static final String NET_LOCATION = "android.permission.ACCESS_FINE_LOCATION";

    private static final String TAKE_PHOTO = "android.permission.CAMERA";

    private static final String READ_CONTACT = "android.permission.READ_CONTACTS";

    private static final String READ_SMS = "android.permission.READ_SMS";

    private static final String DEVICE_INFO = "android.permission.READ_PHONE_STATE";

    public static void getAllPermission(Context context) {
        PackageManager pm = context.getPackageManager();
        String packageName = context.getPackageName();
        PackageInfo info;
        try {
            info = pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            String[] packagePermissions = info.requestedPermissions;
            Log.v("name", info.packageName);
            if (packagePermissions != null) {
                for (int i = 0; i < packagePermissions.length; i++) {
                    LogUtils.i("result", packagePermissions[i]);
                }
            } else {
                LogUtils.i("name", info.packageName + ": no permissions");
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @time 2017/4/11 10:06
     * @desc 是否获得打电话权限
     */
    public static boolean checkIsCallPhoneOpen(Context context) {
        return checkPermission(context, CALL_PHONE);
    }

    /**
     * @time 2017/4/11 10:06
     * @desc SD卡是否可读可写
     */
    public static boolean checkIsSDCardOpen(Context context) {
        return checkPermission(context, WRITE_SD) && checkPermission(context, READ_SD);
    }

    /**
     * @time 2017/4/11 10:07
     * @desc 是否获得通话记录权限
     */
    public static boolean checkIsCallLogOpen(Context context) {
        return checkPermission(context, CALL_LOG);
    }

    /**
     * @time 2017/4/11 10:08
     * @desc 是否获得位置权限
     */
    public static boolean checkIsLocationOpen(Context context) {
        return checkPermission(context, GPS_LOCATION) || checkPermission(context, NET_LOCATION);
    }

    /**
     * @time 2017/4/11 10:08
     * @desc 是否获得拍照权限
     */
    public static boolean checkIsTakePhotoOpen(Context context) {
        return checkPermission(context, TAKE_PHOTO);
    }

    /**
     * @time 2017/4/11 10:08
     * @desc 是否获得相册权限
     */
    public static boolean checkIsGalleryOpen(Context context) {
        return checkPermission(context, WRITE_SD);
    }

    /**
     * @time 2017/4/11 10:09
     * @desc 是否获得读取联系人权限
     */
    public static boolean checkIsReadContactOpen(Context context) {
        return checkPermission(context, READ_CONTACT);
    }

    /**
     * @time 2017/4/11 10:09
     * @desc 是否获得读取短信权限
     */
    public static boolean checkIsReadSmsOpen(Context context) {
        return checkPermission(context, READ_SMS);
    }

    /**
     * @time 2017/4/11 10:09
     * @desc 是否获得读取设备信息权限
     */
    public static boolean checkIsReadDeviceOpen(Context context) {
        return checkPermission(context, DEVICE_INFO);
    }

    public static boolean checkPermission(Context context, String permName) {
        if (Build.VERSION.SDK_INT < 23) {
            PackageManager pm = context.getPackageManager();
            String packageName = context.getPackageName();
            return PackageManager.PERMISSION_GRANTED == pm.checkPermission(permName, packageName);
        } else {
            return ActivityCompat.checkSelfPermission(context, permName) == PackageManager.PERMISSION_GRANTED;
        }
    }

    public static boolean selfPermissionGranted(String permission) {
        Context context=UIUtils.getContext();
        int targetSdkVersion = 0;
        try {
            final PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            targetSdkVersion = info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        boolean result = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (targetSdkVersion >= Build.VERSION_CODES.M) {
                result = context.checkSelfPermission(permission)
                        == PackageManager.PERMISSION_GRANTED;
            } else {
                result = PermissionChecker.checkSelfPermission(context, permission)
                        == PermissionChecker.PERMISSION_GRANTED;
            }
        }
        return result;
    }
}
