package com.my.baselib.lib.utils;

import com.orhanobut.logger.Logger;

/**
 * 一个封装了Logger的工具类
 */
public class LogUtils {
    private static boolean isLogOpen = true;

    //information
    private static void i(String msg) {
        //该方法禁用
        if (!isLogOpen) {
            return;
        }
        if (msg == null) {
            return;
        }
        Logger.i(msg);
    }

    public static void i(String tag, String msg) {
        if (!isLogOpen) {
            return;
        }
        if (msg == null) {
            return;
        }
        Logger.i(tag, msg);
    }

    //debug
    private static void d(String msg) {
        //该方法禁用
        if (!isLogOpen) {
            return;
        }
        if (msg == null) {
            return;
        }
        Logger.d(msg);
    }

    public static void d(String tag, String msg) {
        if (!isLogOpen) {
            return;
        }
        if (msg == null) {
            return;
        }
        Logger.d(tag, msg);
    }

    //warn
    private static void w(String msg) {
        //该方法禁用
        if (!isLogOpen) {
            return;
        }
        if (msg == null) {
            return;
        }
        Logger.w(msg);
    }

    public static void w(String tag, String msg) {
        if (!isLogOpen) {
            return;
        }
        if (msg == null) {
            return;
        }
        Logger.w(tag, msg);
    }

    //error
    public static void e(Exception e) {
        //该方法禁用
        if (!isLogOpen) {
            return;
        }
        Logger.e(e);
    }

    private static void e(String msg) {
        //该方法禁用
        if (!isLogOpen) {
            return;
        }
        if (msg == null) {
            return;
        }
        Logger.e(msg);
    }

    public static void e(String tag, String msg) {
        if (!isLogOpen) {
            return;
        }
        if (msg == null) {
            return;
        }
        Logger.e(tag, msg);
    }

    //json
    private static void j(String msg) {
        //该方法禁用
        if (!isLogOpen) {
            return;
        }
        if (msg == null) {
            return;
        }
        Logger.json(msg);
    }

    public static void j(String tag, String msg) {
        if (!isLogOpen) {
            return;
        }
        if (msg == null) {
            return;
        }
        Logger.json(tag, msg);
    }

    //wtf
    private static void f(String msg) {
        //该方法禁用
        if (!isLogOpen) {
            return;
        }
        if (msg == null) {
            return;
        }
        Logger.wtf(msg);
    }

    public static void f(String tag, String msg) {
        if (!isLogOpen) {
            return;
        }
        if (msg == null) {
            return;
        }
        Logger.wtf(tag, msg);
    }
}
