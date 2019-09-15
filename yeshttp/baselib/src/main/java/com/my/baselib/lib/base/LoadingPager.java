package com.my.baselib.lib.base;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.my.baselib.R;
import com.my.baselib.lib.utils.UIUtils;

/**
 * 通用页面
 */
public abstract class LoadingPager extends FrameLayout {
    public static final int STATE_LOADING = 0;                // 加载中
    public static final int STATE_EMPTY = 1;                // 空
    public static final int STATE_ERROR = 2;                // 错误
    public static final int STATE_SUCCESS = 3;                // 成功
    public static final int STATE_NONE = 4;                // 初始化状态
    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;
    private View mSuccessView;
    public int mCurState = STATE_NONE;//利用该值对页面进行判定
    public Context mContext;

    public LoadingPager(Context context) {
        this(context, null);
    }

    public LoadingPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initCommonViews();
    }

    /**
     * 初始化常规视图(加载页面,错误页面,空页面)
     * LoadingPager初始化的时候
     */
    protected void initCommonViews() {
        // ① 加载页面
        mLoadingView = View.inflate(UIUtils.getContext(), R.layout.pager_loading, null);
        ImageView loading = (ImageView) mLoadingView.findViewById(R.id.anmi);
        AnimationDrawable anim = (AnimationDrawable) loading.getBackground();
        anim.start();

        this.addView(mLoadingView);

        // ② 错误页面
        mErrorView = View.inflate(UIUtils.getContext(), R.layout.pager_error, null);
        this.addView(mErrorView);

        // ③ 空页面
        mEmptyView = View.inflate(UIUtils.getContext(), R.layout.pager_empty, null);
        this.addView(mEmptyView);

        refreshUIByState();
    }

    /**
     * 根据当前状态,显示不同的ui
     * 1.LoadingPager初始化的时候
     * 2.触发加载数据之前,状态被重置
     * 3.触发加载数据,数据加载完成之后,得到数据加载结果,会重新刷新UI
     */
    protected void refreshUIByState() {
        // 控制空视图的显示/隐藏
        mEmptyView.setVisibility((mCurState == STATE_EMPTY) ? View.VISIBLE : View.GONE);

        // 控制错误视图的显示/隐藏
        mErrorView.setVisibility((mCurState == STATE_ERROR) ? View.VISIBLE : View.GONE);

        mErrorView.findViewById(R.id.error_btn_retry).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //重新加载数据
                loadData();
            }
        });

        // 控制加载视图的显示/隐藏
        mLoadingView.setVisibility((mCurState == STATE_LOADING) || (mCurState == STATE_NONE) ? View.VISIBLE : View.GONE);

        // 数据加载成功了.而且成功视图还没有
        if (mCurState == STATE_SUCCESS && mSuccessView == null) {
            mSuccessView = initSuccessView();
            this.addView(mSuccessView);
        }

        if (mSuccessView != null) {
            // 控制加载视图的显示/隐藏
            mSuccessView.setVisibility((mCurState == STATE_SUCCESS) ? View.VISIBLE : View.GONE);
        }
    }

    public void triggerLoadData() {
        // 没有成功&&没有正在加载
        if (mCurState != STATE_SUCCESS && mCurState != STATE_LOADING) {
            // 重置当前的状态
            mCurState = STATE_LOADING;
            refreshUIByState();
            loadData();
        }
    }

    protected abstract View initSuccessView();

    protected abstract void loadData();

    protected void onDestroy() {

    }
}
