package com.my.baselib.lib.utils;


import android.view.View;
import android.widget.EditText;

/**
 * Created by hp on 2017/3/17.
 * app统计工具类
 */
public class CountUtils {

    //返回点击时间
    public static String getTime() {
        long time = System.currentTimeMillis();
        return String.valueOf(time);
    }

    //返回填写时长
    public static String getCount(EditText editText) {
        final long[] disTime = new long[1];
        View.OnFocusChangeListener listener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                long startTime = 0;
                long endTime = 0;
                if (hasFocus) {
                    startTime = System.currentTimeMillis();
                } else {
                    endTime = System.currentTimeMillis();
                }
                disTime[0] = startTime - endTime;

            }
        };


        return null;
        /*long disTime;

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                long startTime = 0;
                long endTime = 0;
                if (hasFocus) {
                    startTime = System.currentTimeMillis();
                } else {
                    endTime = System.currentTimeMillis();
                }
                disTime = startTime - endTime;
            }
        });
        return null;*/
    }

}
