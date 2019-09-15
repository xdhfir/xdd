package com.my.baselib.lib.base;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.my.baselib.lib.utils.IntentUtils;
import com.my.baselib.lib.utils.LogUtils;

import java.util.HashMap;

/**
 * Fragment基础类，App包下
 */
public abstract class BFragment extends Fragment {
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        init();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = initView();
        LogUtils.i(this.getTag(), "Fragment，onCreateView生命周期");
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        initActivity();
        initData();
        initEvent();
        super.onActivityCreated(savedInstanceState);
    }

    protected void initActivity(){}

    /**
     * @des 初始化
     * @call 子类可以选择性的覆写该方法
     */
    protected void init() {

    }

    /**
     * @des 初始化视图
     * @call 必须实现, 基类不知道具体实现, 定义成为抽象方法, 交给子类具体实现
     */
    protected abstract View initView();

    /**
     * @des 初始化数据
     * @call 子类可以选择性的覆写该方法
     */
    protected void initData() {

    }

    /**
     * @des 初始化监听
     * 子类可以选择性的覆写该方法
     */
    protected void initEvent() {

    }

    /**
     * @time 2017/2/17 9:44
     * @desc 从外部（网络，数据库，本地文件……）加载数据的方法
     */
    protected abstract int loadData();

    /**
     * @time 2017/4/14 9:40
     * @desc 跳转到activity
     */
    protected void toActivity(Class c,boolean clearOther,HashMap<String,Object> map) {
        IntentUtils.makeIntent(getActivity(),c,clearOther,map);
    }
}
