package com.my.baselib.lib.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.baselib.R;


/**
 * 正在加载中的弹窗
 */
public class LoadingDialog {
    private Activity activity;
    private final Dialog dialog;
    private final TextView mTv;

    public LoadingDialog(Activity activity) {
        this.activity = activity;
        dialog = new Dialog(activity, R.style.loadingDialog);
        dialog.setContentView(R.layout.view_dialog_loading_dialog);
        dialog.setCanceledOnTouchOutside(false);
        ImageView loading = (ImageView) dialog.findViewById(R.id.anim_loading);
        AnimationDrawable anim = (AnimationDrawable) loading.getBackground();
        anim.start();
        mTv = (TextView) dialog.findViewById(R.id.msg_dialog_loading_dialog);
    }

    public void showDialog() {
        if (!dialog.isShowing() && activity != null && !activity.isFinishing()) {
            try {
                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void dismissDialog() {
        if (dialog.isShowing()) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //判断Dialog是否正在显示中
    public boolean isDialogShowing() {
        return dialog.isShowing();
    }

    //给Dialog设置文字
    public void setText(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        mTv.setText(text);
    }

    public void setCancel(boolean isCancel) {
        if (dialog == null) {
            return;
        }
        dialog.setCancelable(isCancel);
    }

}
