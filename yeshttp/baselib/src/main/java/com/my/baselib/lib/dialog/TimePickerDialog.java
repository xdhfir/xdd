package com.my.baselib.lib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.NumberPicker;


import com.my.baselib.R;
import com.my.baselib.lib.utils.LogUtils;
import com.my.baselib.lib.utils.ToastUtils;

import java.util.Date;

/**
 * 弹窗--时间选择器
 */
public class TimePickerDialog extends Dialog{
    private String[] mDisplayMonths = {"1", "2", "3","4", "5", "6","7", "8", "9","10", "11", "12"};
    private String[] mDisplayHours = {"00:00-01:00", "01:00-02:00", "02:00-03:00","03:00-04:00", "04:00-05:00", "05:00-06:00"
            ,"06:00-07:00", "07:00-08:00", "08:00-09:00","09:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-13:00", "13:00-14:00"
            , "14:00-15:00", "15:00-16:00", "16:00-17:00", "17:00-18:00", "18:00-19:00", "19:00-20:00", "21:00-22:00"
            , "22:00-23:00", "23:00-00:00"};
    private FrameLayout mOk;
    private DatePicker mDate;
    private NumberPicker mTime;
    private String mYear;
    private String mMonth;
    private String mDay;
    private String mHour;
    private Context context;
    public TimePickerDialog(Context context) {
        this(context, R.style.time_pick_dialog);
    }

    public TimePickerDialog(Context context, int theme) {
        super(context, theme);
        this.context=context;
        initView(context);
        initData();
        initEvent();
    }

    private void initView(Context context) {
        setContentView(R.layout.view_dialog_time_picker);
        mOk = (FrameLayout) findViewById(R.id.ok_dialog_time_picker);
        mDate = (DatePicker) findViewById(R.id.date_pick_dialog_time_picker);
        mTime = (NumberPicker) findViewById(R.id.time_pick_dialog_time_picker);
    }

    private void initData() {
        mTime.setDisplayedValues(mDisplayHours);
        mTime.setMinValue(0);
        mTime.setMaxValue(mDisplayHours.length - 1);
        mTime.setValue(24);
        mYear=mDate.getYear()+"";
        mMonth=(mDate.getMonth()+1)+"";
        mDay=mDate.getDayOfMonth()+"";
        mHour="01:00-02:00";

    }

    private void initEvent() {
        mDate.init(mDate.getYear(), mDate.getMonth(), mDate.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mYear=year+"";
                mMonth=(monthOfYear+1)+"";
                mDay=dayOfMonth+"";
            }
        });
        mTime.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mHour = mDisplayHours[newVal];
            }
        });
        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker date = new DatePicker(context);
                //选择的年份小于当前年份
                if(Integer.parseInt(mYear)<date.getYear()){
                    ToastUtils.makeText("不可选择该时间");
                    return;
                }
                //月份小于当前月份
                if(Integer.parseInt(mYear)==date.getYear()&&
                        Integer.parseInt(mMonth)<(date.getMonth()+1)){
                    ToastUtils.makeText("不可选择该时间");
                    return;
                }
                //日期小于当前日期
                if(Integer.parseInt(mYear)==date.getYear()&&
                        Integer.parseInt(mMonth)==(date.getMonth()+1)&&
                        Integer.parseInt(mDay)<date.getDayOfMonth()){
                    ToastUtils.makeText("不可选择该时间");
                    return;
                }
                Date d = new Date();
                int index = d.getHours();
                int currentIndex=0;
                for(int i=0;i<mDisplayHours.length;i++){
                    if(mDisplayHours[i].equals(mHour)){
                        currentIndex = i;
                        break;
                    }
                }
                if(Integer.parseInt(mYear)==date.getYear()&&
                        Integer.parseInt(mMonth)==(date.getMonth()+1)&&
                        Integer.parseInt(mDay)==date.getDayOfMonth()&&
                        index>currentIndex){
                    ToastUtils.makeText("不可选择该时间");
                    return;
                }

                String time=mYear+"-"+mMonth+"-"+mDay+" "+mHour;
                LogUtils.i("timePicker", time);
                if(mTimePickerListener!=null){
                    mTimePickerListener.selectTime(time);
                }
                TimePickerDialog.this.dismiss();
            }
        });
    }

    public interface TimePickerListener{
        void selectTime(String time);
    }
    private TimePickerListener mTimePickerListener;
    public void setTimePickerListener(TimePickerListener timePickerListener){
        this.mTimePickerListener=timePickerListener;
    }
}
