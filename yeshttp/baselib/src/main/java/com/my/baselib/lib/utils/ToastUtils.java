package com.my.baselib.lib.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

/**
 * 一个运行在主线程的吐司工具类
 */
public class ToastUtils {
    private static Toast toast;
    public static void makeText(final String msg) {
        if (TextUtils.isEmpty(msg)) {
            Logger.e("ToastUtils", "吐司信息为空");
            return;
        }
        UIUtils.postTaskSafely(new Runnable() {
            @Override
            public void run() {
                if (toast == null) {
                    toast = Toast.makeText(UIUtils.getContext(), msg,Toast.LENGTH_SHORT);
                } else {
                    toast.setText(msg);
                }
                toast.show();
            }
        });
    }
}
