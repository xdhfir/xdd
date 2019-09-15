package com.my.yeshttp.httpmanager;

import android.app.Activity;
import com.my.yeshttp.base.DataResponse;
import com.my.yeshttp.rx.MSubscriber;


/**
 * create by Administrator at 2017/3/3
 * description: 网络请求的回调预处理
 */
public abstract class AppSubscriber<T> extends MSubscriber<T> {
    public AppSubscriber(Activity context) {
        super(context);
    }

    public AppSubscriber(Activity context, String text) {
        super(context, text);
    }

    public AppSubscriber(Activity context, boolean isShowMsg) {
        super(context, isShowMsg);
    }

    public AppSubscriber(Activity context, String text, boolean isShowMsg) {
        super(context, text, isShowMsg);
    }

    public AppSubscriber(Activity context, boolean isShowMsg, boolean isShowError) {
        super(context, isShowMsg, isShowError);
    }

    public AppSubscriber(Activity context, String text, boolean isShowMsg, boolean isShowError) {
        super(context, text, isShowMsg, isShowError);
    }

    @Override
    public void onNext(DataResponse<T> data) {
        if ("1111".equals(data.status)) {
            isVersioning=false;
            //登录信息丢失,强制跳转登录界面
            if (dialog != null) {
                dialog.dismissDialog();
            }
            if(isLogining){
                return;
            }
            synchronized (AppSubscriber.class){
                if(isLogining){
                    return;
                }
                toLogin();
                isLogining=true;
            }
        }else if("2222".equals(data.status)){//版本检查作为强制更新
            isLogining=false;
            if(isVersioning){
                return;
            }
            synchronized (AppSubscriber.class){
                if(isVersioning){
                    return;
                }
                checkVersion();
                isVersioning=true;
            }
        } else {
            isVersioning=false;
            isLogining=false;
            super.onNext(data);
        }
    }

    @Override
    protected void doError(Throwable e) {
        super.doError(e);
        //腾讯bugly上报
        //CrashReport.postCatchedException(e);
    }

    private void checkVersion(){
        /*Activity activity = ActivityTask.getInstance().currentActivity();
        VersionUpdateManager.getInstance().update(activity, new VersionUpdateManager.VersionUpdateListener() {
            @Override
            public void isForce(boolean flag) {
                if(flag){
                    ToastUtils.makeText("新版本有较大改动，请先更新再重新启动APP");
                    ActivityTask.getInstance().popAllActivity();
                }
            }
        });*/
    }

    private static boolean isLogining=false;
    private static boolean isVersioning=false;

    private void toLogin() {
        /*Intent intent = new Intent();
        intent.setAction("mmt.wallet.login");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        BaseApplication.getContext().startActivity(intent);*/
    }
}
