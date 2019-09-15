package com.my.baselib.lib.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.my.baselib.R;
import com.orhanobut.logger.Logger;

/**
 * APK下载的弹窗，显示进度条
 */
public class ApkDownLoadDialog {
    private Activity activity;
    private final Dialog dialog;
    private final ProgressBar mPb;
    private final Button mBt;

    public ApkDownLoadDialog(Activity activity) {
        this.activity = activity;
        dialog = new Dialog(activity, R.style.dialog);
        dialog.setContentView(R.layout.view_dialog_apk_download_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(keyListener);
        mPb = (ProgressBar) dialog.findViewById(R.id.pb_dialog_apk_download_dialog);
        mBt = (Button) dialog.findViewById(R.id.bt_dialog_apk_download_dialog);
    }

    public void showDialog() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void dismissDialog() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void setOnClickListener(View.OnClickListener listener) {
        if(listener==null){
            return;
        }
        mBt.setOnClickListener(listener);
    }

    public void setProgress(int i){
        if(i>=0&&i<=100){
            mPb.setProgress(i);
        }
    }

    //获取按钮的ID
    public int getBtId(){
        return R.id.bt_dialog_apk_download_dialog;
    }

    //设置按钮的文字
    public void setBtText(String text){
        if(TextUtils.isEmpty(text)){
            Logger.d("ApkDownLoadDialog", "设置文字为空");
            return;
        }
        mBt.setText(text);
    }

    //获取按钮的文字
    public String getBtText(){
        return mBt.getText().toString().trim();
    }

    public boolean isShow(){
        return dialog.isShowing();
    }

    DialogInterface.OnKeyListener keyListener = new DialogInterface.OnKeyListener(){
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            return keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0;
        }
    } ;
}
