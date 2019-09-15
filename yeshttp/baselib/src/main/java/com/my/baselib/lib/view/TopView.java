package com.my.baselib.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.my.baselib.R;

/**
 * create by Administrator at 2017/2/21
 * description: 自定义标题栏
 */
public class TopView extends RelativeLayout implements View.OnClickListener {
    private Context mContext;
    private TextView mTx;
    private RelativeLayout mAbc;
    private RelativeLayout mBack;
    private RelativeLayout view;
    private RelativeLayout mContain;

    public TopView(Context context) {
        this(context, null);
    }

    public TopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        view = (RelativeLayout)View.inflate(mContext, R.layout.view_top_view, this);
        mBack = (RelativeLayout) view.findViewById(R.id.back_view_top_view);
        mAbc = (RelativeLayout) view.findViewById(R.id.error_view_top_view);
        mTx = (TextView) view.findViewById(R.id.name_view_top_view);
        mContain = (RelativeLayout) view.findViewById(R.id.content_view_top_view);
        initData(attrs);
        initEvent();
    }

    public void setBackVisiable(int flag){
        mBack.setVisibility(flag);
    }

    public void setErrorVisiable(int flag){
        mAbc.setVisibility(flag);
    }

    private void initEvent() {
        mBack.setOnClickListener(this);
        mAbc.setOnClickListener(this);
    }

    private void initData(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs,
                R.styleable.TopView);

        int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            if (typedArray.getIndex(i) == R.styleable.TopView_top_name) {
                CharSequence text = typedArray.getText(i);
                mTx.setText(text);
            }

            if (typedArray.getIndex(i) == R.styleable.TopView_have_back) {
                boolean aBoolean = typedArray.getBoolean(i, false);
                if (aBoolean) {
                    mBack.setVisibility(View.VISIBLE);
                } else {
                    mBack.setVisibility(View.GONE);
                }
            }

            if (typedArray.getIndex(i) == R.styleable.TopView_is_x_show) {
                boolean aBoolean = typedArray.getBoolean(i, false);
                if (aBoolean) {
                    mAbc.setVisibility(View.VISIBLE);
                } else {
                    mAbc.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (topViewListener == null) {
            return;
        }
        int i = v.getId();
        if (i == R.id.back_view_top_view) {
            topViewListener.click(0);
        } else if (i == R.id.error_view_top_view) {
            topViewListener.click(1);
        }
    }

    /**
     * @time 2017/2/21 15:16
     * @desc TopView的点击事情回调，0代表是返回箭头，1代表是X按钮
     */
    public interface OnTopViewListener {
        void click(int flag);
    }

    private OnTopViewListener topViewListener;

    public void setTopViewListener(OnTopViewListener topViewListener) {
        this.topViewListener = topViewListener;
    }

    public void setBackColor(int color){
       mContain.setBackgroundColor(color);
    }

    public void setText(String text){
        mTx.setText(text);
    }
}
