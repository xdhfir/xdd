package com.my.baselib.lib.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;

import com.my.baselib.R;


/**
 * create by Administrator at 2017/2/21
 * description: 一个Button,设置两种背景选择器，第一种背景选择器不触发点击事情，第二背景选择器可以触发
 */
@SuppressLint("AppCompatCustomView")
public class InputButton extends TextView {
    private int flag = 0;

    public InputButton(Context context) {
        this(context, null);
    }

    public InputButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUnClickAble();
    }

    /**
     * @time 2017/2/21 16:19
     * @desc 0不可点击，1可点击
     */
    public void setType(int flag) {
        this.flag = flag;
        if (flag == 0) {
            setUnClickAble();
        } else {
            setClickAble();
        }
    }

    /**
     * @time 2017/2/21 14:37
     * @desc 设置按钮可点击
     */
    private void setClickAble() {
        this.setBackgroundResource(R.drawable.orange_or_orangedeep_selector);
        this.setTextColor(Color.argb(255, 255, 255, 255));
        this.setEnabled(true);
    }


    /**
     * @time 2017/2/21 14:36
     * @desc 设置按钮不可点击
     */
    private void setUnClickAble() {
        this.setBackgroundResource(R.drawable.ellipse_gray_light_5);
        this.setTextColor(Color.argb(255, 197, 197, 197));
        this.setEnabled(false);
    }
}
