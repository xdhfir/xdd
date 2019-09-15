package com.my.baselib.lib.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by hp on 2016/8/15.
 */
@SuppressLint("AppCompatCustomView")
public class TimerMessageView extends TextView implements Runnable {

    // 时间变量
    private int second;
    // 当前计时器是否运行
    private boolean isRun = false;

    public TimerMessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TimerMessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimerMessageView(Context context) {
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
        this.second = (int) (time / 1000);
        removeCallbacks(this);
    }

    /**
     * 显示当前时间
     *
     * @return
     */
    public String showTime() {
        StringBuilder time = new StringBuilder();
        time.append(second);
        time.append("秒后重发");
        return time.toString();
    }

    /**
     * 实现倒计时
     */
    private void countdown() {
        if (second == 0) {
            //当时间归零时停止倒计时
            isRun = false;
            return;
        }
        //second = 60;
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
        this.setClickable(false);
        run();
    }

    /**
     * 结束计时
     */
    public void stop() {
        removeCallbacks(this);
        isRun = false;
        this.setClickable(true);
        this.setText("重新发送");
    }

    /**
     * 实现计时循环
     */
    @Override
    public void run() {
        if (isRun) {
            countdown();
            this.setText(showTime());
            postDelayed(this, 1000);
        } else {
            removeCallbacks(this);
            stop();
        }
    }
}
