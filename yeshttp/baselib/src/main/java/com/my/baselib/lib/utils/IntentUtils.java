package com.my.baselib.lib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.my.baselib.lib.base.ActivityTask;

import java.io.Serializable;
import java.util.HashMap;

/**
 * create by Administrator at 2017/4/17
 * description:
 */
public class IntentUtils {
    public static void makeIntent(Class c) {
        Activity activity = ActivityTask.getInstance().currentActivity();
        makeIntent(activity, c, false, null);
    }

    public static void makeIntent(Context context, Class c) {
        makeIntent(context, c, false, null);
    }

    public static void makeIntent(Context context, Class c, HashMap<String, Object> map) {
        makeIntent(context, c, false, map);
    }

    public static void makeIntent(Context context, Class c, boolean clearOther, HashMap<String, Object> map) {
        if (clearOther) {
            ActivityTask.getInstance().popAllActivityExceptOne(context.getClass());
        }
        Intent intent = new Intent(context, c);
        if (map != null) {
            addParam(intent, map);
        }
        context.startActivity(intent);
        if (clearOther) {
            ActivityTask.getInstance().popAllActivityExceptOne(c);
        }
    }

    /**
     * @time 2017/4/17 16:56
     * @desc intent参数，支持String,Integer,Double,Folat
     */
    private static void addParam(Intent intent, HashMap<String, Object> map) {
        for (String s : map.keySet()) {
            Object o = map.get(s);
            if (o instanceof String) {
                intent.putExtra(s, (String) map.get(s));
            } else if (o instanceof Integer) {
                intent.putExtra(s, (Integer) map.get(s));
            } else if (o instanceof Double) {
                intent.putExtra(s, (Double) map.get(s));
            } else if (o instanceof Float) {
                intent.putExtra(s, (Float) map.get(s));
            } else if (o instanceof Serializable) {
                intent.putExtra(s, (Serializable) map.get(s));
            } else {
                LogUtils.i("IntentUtils_addParam", "暂不支持的参数类型");
            }
        }
    }
}
