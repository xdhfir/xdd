package com.my.baselib.lib.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.my.baselib.R;


/**
 * create by Administrator at 2017/3/3
 * description: 买买提钱包选择金额seekBar
 */
public class WalletSeekBar extends LinearLayout {

    private TextView tv;
    private SeekBar sb;
    private int min = 0;
    private int max = 100;
    int sbWidth;//seekBar的宽度
    int vgWidth;
    private int left;
    private int count = 1;
    private String des = "";

    public WalletSeekBar(Context context) {
        this(context, null);
    }

    public WalletSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        View v = View.inflate(context, R.layout.view_wallet_seek_bar, this);
        tv = (TextView) v.findViewById(R.id.tv_wallet_seek_bar_view);
        sb = (SeekBar) v.findViewById(R.id.sb_wallet_seek_bar_view);
        initData(context, attrs);
        initEvent(context);
    }

    private void initEvent(Context context) {
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int wid = (int) ((sbWidth * progress) / 100);
                resetTvLocation(wid);
                resetTvText(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                selectOk();
            }
        });
    }

    private void resetTvText(int progress) {
        int i = max - min;
        int i2 = i / count;
        int i1 = i2 * progress;
        int i3 = i1 * count;
        int i5 = i3 / 100 + min;
        if (count > 1) {
            int i6 = i5 / count * count;
            tv.setText(String.valueOf(i6) + des);
        } else {
            tv.setText(String.valueOf(i5) + des);
        }
    }


    //重置TextView 的位置
    private void resetTvLocation(int i) {
        tv.setPadding(i + (left / 2) + 5, 0, 0, 0);
    }

    private void initData(Context context, AttributeSet attrs) {
        final ViewTreeObserver observer = sb.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (observer.isAlive()) {
                    observer.removeOnPreDrawListener(this);
                }
                left = sb.getPaddingLeft();
                int right = sb.getPaddingRight();
                sbWidth = sb.getRight() - sb.getLeft() - left - right - 15;
                return true;
            }
        });
    }

    public String getProgress() {
        if (sb.getProgress() == 0) {
            return "";
        }
        String trim = tv.getText().toString().trim();
        int length = trim.length();
        if (!TextUtils.isEmpty(des)) {
            return trim.substring(0, length - 1);
        } else {
            return trim;
        }
    }

    public void setProgress(int progress) {
        sb.setProgress(progress);
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setDes(String s) {
        this.des = s;
    }

    private void selectOk() {
        if (walletSeekBarListener != null) {
            walletSeekBarListener.select();
        }
    }

    public interface WalletSeekBarListener {
        void select();
    }

    private WalletSeekBarListener walletSeekBarListener;

    public void setWalletSeekBarListener(WalletSeekBarListener walletSeekBarListener) {
        this.walletSeekBarListener = walletSeekBarListener;
    }
}
