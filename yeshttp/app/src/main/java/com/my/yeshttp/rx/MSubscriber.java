package com.my.yeshttp.rx;

import android.app.Activity;

import com.my.baselib.lib.dialog.LoadingDialog;
import com.my.baselib.lib.utils.ToastUtils;
import com.my.yeshttp.base.DataResponse;

import rx.Subscriber;


/**
 * 自定义Subscriber,将dialog、complete、error、错误返回抽取出来，实现类中只进行Success的处理
 */
public abstract class MSubscriber<T> extends Subscriber<DataResponse<T>> {
    protected LoadingDialog dialog;
    protected boolean isShowMsg = false;
    protected boolean isShowError = false;

    /**
     * 传入activity，默认显示弹窗
     */
    public MSubscriber(Activity context) {
        if (context != null) {
            showDialog(context, null);
        }
    }

    /**
     * 传入activity，默认显示弹窗，传入text，改变弹窗显示文字
     */
    public MSubscriber(Activity context, String text) {
        if (context != null) {
            showDialog(context, text);
        }
    }

    /**
     * 是否在访问成功，但code不正确时弹出吐司
     */
    public MSubscriber(Activity context, boolean isShowMsg) {
        if (context != null) {
            showDialog(context, null);
        }
        this.isShowMsg = isShowMsg;
    }

    /**
     * 传入text;是否在访问成功，但code不正确时弹出吐司
     */
    public MSubscriber(Activity context, String text, boolean isShowMsg) {
        if (context != null) {
            showDialog(context, text);
        }
        this.isShowMsg = isShowMsg;
    }

    /**
     * 是否在访问成功，但code不正确时弹出吐司,以及是否在网络不正确时弹出错误吐司
     */
    public MSubscriber(Activity context, boolean isShowMsg, boolean isShowError) {
        if (context != null) {
            showDialog(context, null);
        }
        this.isShowMsg = isShowMsg;
        this.isShowError = isShowError;
    }

    /**
     * 传入text,是否在访问成功，但code不正确时弹出吐司,以及是否在网络不正确时弹出错误吐司
     */
    public MSubscriber(Activity context, String text, boolean isShowMsg, boolean isShowError) {
        if (context != null) {
            showDialog(context, text);
        }
        this.isShowMsg = isShowMsg;
        this.isShowError = isShowError;
    }

    /**
     * 显示弹窗的方法
     */
    private void showDialog(Activity context, String text) {
        dialog = new LoadingDialog(context);
        if (text == null) {
            dialog.setText("正在访问网络，请稍候...");
        } else {
            dialog.setText(text);
        }
        dialog.showDialog();
    }

    @Override
    public void onCompleted() {
        if (dialog != null) {
            dialog.dismissDialog();
        }
    }

    @Override
    public void onError(Throwable e) {
        if (dialog != null) {
            dialog.dismissDialog();
        }
        e.printStackTrace();
        if (isShowError) {
            ToastUtils.makeText("网络请求失败");
        }
        doError(e);
        makeError(null);
    }

    //选择性重写，处理error
    protected void doError(Throwable e) {

    }

    @Override
    public void onNext(DataResponse<T> data) {
        if (dialog != null) {
            dialog.dismissDialog();
        }
        if ("0".equals(data.status)) {
            doNext(data.data);
        } else {
            if (isShowMsg) {
                ToastUtils.makeText(data.message);
            }
            doOther(data);
            makeError(data);
        }
    }

    //选择性重写，处理网络访问成功，但不是正确的返回结果时
    protected void doOther(DataResponse<T> data) {

    }

    protected abstract void doNext(T data);

    protected void makeError(DataResponse<T> data) {

    }
}
