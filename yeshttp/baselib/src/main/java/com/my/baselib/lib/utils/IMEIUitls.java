package com.my.baselib.lib.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;


/**
 *
 */
public class IMEIUitls {
    public static String getIMEICode(Context context) {
        TelephonyManager TelephonyMgr = (TelephonyManager) context.
                getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("HardwareIds") String szIMEi = TelephonyMgr.getDeviceId();
        if (szIMEi == null) {
            return null;
        }
        return szIMEi;
    }

    public static String getIMSICode(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.
                getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = telephonyManager.getSubscriberId();//获取手机IMSI号
        return imsi;
    }
}
