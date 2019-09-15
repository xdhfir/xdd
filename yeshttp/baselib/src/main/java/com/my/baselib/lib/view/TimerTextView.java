package com.my.baselib.lib.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class TimerTextView extends TextView implements Runnable {
    // 时间变量
    private int day, hour, minute, second;
    // 当前计时器是否运行
    private boolean isRun = false;

    public TimerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TimerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimerTextView(Context context) {
        super(context);
    }

    /**
     * 将倒计时时间毫秒数转换为自身变量
     *
     * @param time 时间间隔毫秒数
     */
    public void setTimes(long time) {
        if (time < 0) {
            time = 0;
        }
        //将毫秒数转化为时间
        this.second = (int) (time / 1000) % 60;
        this.minute = (int) (time / (60 * 1000) % 60);
        this.hour = (int) (time / (60 * 60 * 1000) % 24);
        this.day = (int) (time / (24 * 60 * 60 * 1000));
        removeCallbacks(this);
    }

    /**
     * 显示当前时间
     *
     * @return
     */
    public String showTime() {
        StringBuilder time = new StringBuilder();
        time.append(day);
        time.append("天");
        time.append(hour);
        time.append("小时");
        time.append(minute);
        time.append("分钟");
        time.append(second);
        time.append("秒");
        return time.toString();
    }

    /**
     * 实现倒计时
     */
    private void countdown() {
        if (second == 0) {
            if (minute == 0) {
                if (hour == 0) {
                    if (day == 0) {
                        //当时间归零时停止倒计时
                        isRun = false;
                        return;
                    } else {
                        day--;
                    }
                    hour = 23;
                } else {
                    hour--;
                }
                minute = 59;
            } else {
                minute--;
            }
            second = 60;
        }
        second--;
    }

    public boolean isRun() {
        return isRun;
    }

    /**
     * 开始计时
     */
    public void start() {
        isRun = true;
        run();
    }

    /**
     * 结束计时
     */
    public void stop() {
        removeCallbacks(this);
        isRun = false;
    }

    /**
     * 实现计时循环
     */
    @Override
    public void run() {
        if (isRun) {
            // Log.d(TAG, "Run");
            countdown();
            this.setText(showTime());
            postDelayed(this, 1000);
        } else {
            removeCallbacks(this);
        }
    }

}
