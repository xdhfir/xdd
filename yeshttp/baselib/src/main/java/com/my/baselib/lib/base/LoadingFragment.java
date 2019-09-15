package com.my.baselib.lib.base;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.my.baselib.R;
import com.my.baselib.lib.utils.UIUtils;
import com.my.baselib.lib.view.LEESView;


/**
 *
 */
public abstract class LoadingFragment extends BFragment{
    public LEESView mView;
    public Button mReTryBt;
    public static final int LOADING=0;
    public static final int SUCCESS=1;
    public static final int ERROR=2;
    public static final int EMPTY=3;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(UIUtils.getContext(), R.layout.base_activity_loading, null);
        mView = (LEESView) view.findViewById(R.id.lees_activity_load);
        mReTryBt = mView.getReTryBt();
        mReTryBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = loadData();
                check(i);
            }
        });
        mView.showLoading();//初始化为加载画面
        mView.setSuccessView(initView());
        int i = loadData();
        check(i);
        return view;
    }

    private void check(int i) {
        switch (i){
            case LOADING:
                mView.showLoading();
                break;
            case SUCCESS:
                mView.showSuccess();
                break;
            case EMPTY:
                mView.showEmpty();
                break;
            case ERROR:
                mView.showError();
                break;
        }
    }

    protected abstract int loadData();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    /**
     * 初始化成功界面视图
     * 必须实现,基类不知道具体实现,定义成为抽象方法,交给子类具体实现
     */
    protected abstract View initView();
}
