package com.my.baselib.lib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.my.baselib.R;
import com.my.baselib.lib.utils.ToastUtils;


/**
 * 弹窗--时间选择器
 */
public class TimePickerDayDialog extends Dialog{
    private FrameLayout mOk;
    private DatePicker mDate;
    private NumberPicker mTime;
    private String mYear;
    private String mMonth;
    private String mDay;
    private TextView mTitle;
    private Context context;

    public TimePickerDayDialog(Context context) {
        this(context, R.style.time_pick_dialog);
    }

    public TimePickerDayDialog(Context context, int theme) {
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
        mTime.setVisibility(View.GONE);
        mTitle = (TextView) findViewById(R.id.title_dialog_time_picker);
    }

    private void initData() {
        mTime.setMinValue(0);
        mTime.setValue(24);

        mYear=mDate.getYear()+"";
        mMonth=mDate.getMonth()+1+"";
        mDay=mDate.getDayOfMonth()+"";
        DatePicker date = new DatePicker(context);
        y=date.getYear();
        m=date.getMonth();
        d=date.getDayOfMonth();
    }
    int y;
    int m;
    int d;
    private void initEvent() {
        mDate.init(mDate.getYear(), mDate.getMonth(), mDate.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mYear=year+"";
                mMonth=(monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1));
                mDay=dayOfMonth>9?(dayOfMonth+""):("0"+dayOfMonth);
                y = year;
                m = monthOfYear;
                d = dayOfMonth;
            }
        });
        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker date = new DatePicker(context);
                if(y<date.getYear()){
                    ToastUtils.makeText("不可选择该时间");
                    return;
                }

                if(y==date.getYear()&&m<date.getMonth()){
                    ToastUtils.makeText("不可选择该时间");
                    return;
                }

                if(y==date.getYear()&&m==date.getMonth()&&d<=date.getDayOfMonth()){
                    ToastUtils.makeText("不可选择该时间");
                    return;
                }
                String time=mYear+"/"+mMonth+"/"+mDay;
                if(mTimePickerListener!=null){
                    mTimePickerListener.selectTime(time);
                }
                TimePickerDayDialog.this.dismiss();
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

    public void setTitle(String s){
        mTitle.setText(s);
    }
}
