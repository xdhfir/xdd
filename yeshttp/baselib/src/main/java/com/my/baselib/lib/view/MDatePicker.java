package com.my.baselib.lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.DatePicker;

/**
 * 修改月份为数字显示
 */
public class MDatePicker extends DatePicker{
    public MDatePicker(Context context) {
        super(context);
    }

    public MDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
