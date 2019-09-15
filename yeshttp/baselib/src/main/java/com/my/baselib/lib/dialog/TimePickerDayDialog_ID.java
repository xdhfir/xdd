package com.my.baselib.lib.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;


import com.my.baselib.R;

import java.lang.reflect.Field;

/**
 * 弹窗--时间选择器
 */
public class TimePickerDayDialog_ID extends Dialog {
    private FrameLayout mOk;
    private DatePicker mDate;
    private NumberPicker mTime;
    private String mYear;
    private String mMonth;
    private String mDay;
    private TextView mTitle;
    private Context context;
    private FrameLayout mCancel;
    private TextView longTime;

    public TimePickerDayDialog_ID(Context context) {
        this(context, R.style.time_pick_dialog);
    }

    public TimePickerDayDialog_ID(Context context, int theme) {
        super(context, theme);
        this.context = context;
        initView(context);
        initData();
        initEvent();
    }

    private void initView(Context context) {
        setContentView(R.layout.view_dialog_time_picker_id);
        mOk = (FrameLayout) findViewById(R.id.ok_dialog_time_picker_id);
        mDate = (DatePicker) findViewById(R.id.date_pick_dialog_time_picker_id);
        mTime = (NumberPicker) findViewById(R.id.time_pick_dialog_time_picker_id);
        mTime.setVisibility(View.GONE);
        mTitle = (TextView) findViewById(R.id.title_dialog_time_picker_id);
        mCancel = (FrameLayout) findViewById(R.id.cancel_dialog_time_picker_id);
        longTime = (TextView) findViewById(R.id.long_time_dialog_time_picker_id);

        setDatePickerDividerColor(mDate);

        Window window = this.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height =  WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        window.setGravity(Gravity.BOTTOM);
    }

    public void isLongTimeShow(boolean flag) {
        longTime.setVisibility(flag ? View.VISIBLE : View.GONE);
    }

    private void initData() {
        mTime.setMinValue(0);
        mTime.setValue(24);

        mYear = mDate.getYear() + "";
        mMonth = mDate.getMonth() + 1 + "";
        mDay = mDate.getDayOfMonth() + "";
        DatePicker date = new DatePicker(context);
        y = date.getYear();
        m = date.getMonth();
        d = date.getDayOfMonth();
    }

    int y;
    int m;
    int d;

    private void initEvent() {
        mDate.init(mDate.getYear(), mDate.getMonth(), mDate.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mYear = year + "";
                mMonth = (monthOfYear + 1) > 9 ? ((monthOfYear + 1) + "") : ("0" + (monthOfYear + 1));
                mDay = dayOfMonth > 9 ? (dayOfMonth + "") : ("0" + dayOfMonth);
                y = year;
                m = monthOfYear;
                d = dayOfMonth;
            }
        });
        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = mYear + "" + mMonth + "" + mDay;
                if (mTimePickerListener != null) {
                    mTimePickerListener.selectTime(time);
                }
                TimePickerDayDialog_ID.this.dismiss();
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDayDialog_ID.this.dismiss();
            }
        });
        longTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTimePickerListener.selectTime("长期");
                TimePickerDayDialog_ID.this.dismiss();
            }
        });
    }

    public interface TimePickerListener {
        void selectTime(String time);
    }

    private TimePickerListener mTimePickerListener;

    public void setTimePickerListener(TimePickerListener timePickerListener) {
        this.mTimePickerListener = timePickerListener;
    }

    public void setTitle(String s) {
        mTitle.setText(s);
    }

    private void setDatePickerDividerColor(DatePicker datePicker){
        // 获取 mSpinners
        LinearLayout llFirst       = (LinearLayout) datePicker.getChildAt(0);
        // 获取 NumberPicker
        LinearLayout mSpinners      = (LinearLayout) llFirst.getChildAt(0);
        for (int i = 0; i < mSpinners.getChildCount(); i++) {
            NumberPicker picker = (NumberPicker) mSpinners.getChildAt(i);
            Field[] pickerFields = NumberPicker.class.getDeclaredFields();
            for (Field pf : pickerFields) {
                if (pf.getName().equals("mSelectionDivider")) {
                    pf.setAccessible(true);
                    try {
                        pf.set(picker, new ColorDrawable(getContext().getResources().getColor(R.color.base_transparent)));
                    } catch (IllegalArgumentException | Resources.NotFoundException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }
}
