package com.my.baselib.lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.my.baselib.R;

/**
 * 这是一个基础布局，有loading，empty，error，success四个页面，
 * 方法：error页面重试按钮
 */
public class LEESView extends FrameLayout {
    public Context mContext;
    private View v;
    private FrameLayout mLoading;
    private FrameLayout mError;
    private FrameLayout mEmpty;
    private FrameLayout mSuccess;
    private Button mBt;

    public LEESView(Context context) {
        this(context, null);
    }
    public LEESView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        v = View.inflate(context, R.layout.view_lees_view, this);
        initView();
    }
    //将重试按钮暴露出来
    public Button getReTryBt(){
        return mBt;
    }

    public void setSuccessView(View v){
        if(mSuccess==null){
            return;
        }
        mSuccess.removeAllViews();
        mSuccess.addView(v);
    }

    private void initView() {
        mLoading = (FrameLayout) v.findViewById(R.id.loading_lees_view);
        mError = (FrameLayout) v.findViewById(R.id.error_lees_view);
        mEmpty = (FrameLayout) v.findViewById(R.id.empty_lees_view);
        mSuccess = (FrameLayout) v.findViewById(R.id.success_lees_view);

        mBt = (Button) v.findViewById(R.id.bt_error_lees_view);
    }

    public void showLoading(){
        mLoading.setVisibility(View.VISIBLE);
        mSuccess.setVisibility(View.GONE);
        mEmpty.setVisibility(View.GONE);
        mError.setVisibility(View.GONE);
    }
    public void showError(){
        mLoading.setVisibility(View.GONE);
        mSuccess.setVisibility(View.GONE);
        mEmpty.setVisibility(View.GONE);
        mError.setVisibility(View.VISIBLE);
    }
    public void showSuccess(){
        mLoading.setVisibility(View.GONE);
        mSuccess.setVisibility(View.VISIBLE);
        mEmpty.setVisibility(View.GONE);
        mError.setVisibility(View.GONE);
    }
    public void showEmpty(){
        mLoading.setVisibility(View.GONE);
        mSuccess.setVisibility(View.GONE);
        mEmpty.setVisibility(View.VISIBLE);
        mError.setVisibility(View.GONE);
    }
}
