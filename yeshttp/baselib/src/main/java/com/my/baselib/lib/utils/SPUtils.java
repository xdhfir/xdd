package com.my.baselib.lib.utils;

import android.content.Context;
import android.content.SharedPreferences;


//一个对SharedPreferences进行封装的工具类,使用之前，必须在BaseApplication内部进行注册
public class SPUtils {
    private static String SP_FILENAME="yeshttp";//SP文件的保存文件名
    public static void regist(String fileName){
        SP_FILENAME=fileName;
    }
    public static void putBoolean(Context context,String key,boolean value){
        SharedPreferences sp = context.getSharedPreferences(SP_FILENAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(Context context,String key,boolean defValue){
        SharedPreferences sp = context.getSharedPreferences(SP_FILENAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    public static void putString(Context context,String key,String value){
        SharedPreferences sp = context.getSharedPreferences(SP_FILENAME, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    public static String getString(Context context,String key,String defValue){
        SharedPreferences sp = context.getSharedPreferences(SP_FILENAME, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }
    public static void putInt(Context context,String key,int value){
        SharedPreferences sp = context.getSharedPreferences(SP_FILENAME, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).apply();

    }

    public static int getInt(Context context,String key,int defValue){
        SharedPreferences sp = context.getSharedPreferences(SP_FILENAME, Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);

    }
    public static void putLong(Context context,String key,long value){
        SharedPreferences sp = context.getSharedPreferences(SP_FILENAME, Context.MODE_PRIVATE);
        sp.edit().putLong(key, value).apply();

    }

    public static long getLong(Context context,String key,Long defValue){
        SharedPreferences sp = context.getSharedPreferences(SP_FILENAME, Context.MODE_PRIVATE);
        return sp.getLong(key, defValue);
    }
    public static void putBoolean(String key,boolean value){
        SharedPreferences sp = UIUtils.getContext().getSharedPreferences(SP_FILENAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key,boolean defValue){
        SharedPreferences sp = UIUtils.getContext().getSharedPreferences(SP_FILENAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    public static void putString(String key,String value){
        SharedPreferences sp = UIUtils.getContext().getSharedPreferences(SP_FILENAME, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    public static String getString(String key,String defValue){
        SharedPreferences sp = UIUtils.getContext().getSharedPreferences(SP_FILENAME, Context.MODE_PRIVATE);
        if(sp!=null) {
            return sp.getString(key, defValue);
        }
        return "";
    }
    public static void putInt(String key,int value){
        SharedPreferences sp = UIUtils.getContext().getSharedPreferences(SP_FILENAME, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).apply();

    }

    public static int getInt(String key,int defValue){
        SharedPreferences sp = UIUtils.getContext().getSharedPreferences(SP_FILENAME, Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);

    }
    public static void putLong(String key,long value){
        SharedPreferences sp = UIUtils.getContext().getSharedPreferences(SP_FILENAME, Context.MODE_PRIVATE);
        sp.edit().putLong(key, value).apply();

    }

    public static long getLong(String key,Long defValue){
        SharedPreferences sp = UIUtils.getContext().getSharedPreferences(SP_FILENAME, Context.MODE_PRIVATE);
        return sp.getLong(key, defValue);
    }
}
