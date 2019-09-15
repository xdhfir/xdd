package com.my.baselib.lib.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.my.baselib.R;


/**
 * 版本更新的彈窗，是否選擇更新
 */
public class YesOrNoDialog {
    public Activity activity;
    private final Dialog dialog;
    private final TextView mTitle;
    private final TextView text;
    private final Button mYes;
    private final Button mNo;

    public YesOrNoDialog(Activity activity) {
        this.activity = activity;
        dialog = new Dialog(activity, R.style.dialog);
        dialog.setContentView(R.layout.view_dialog_yes_or_no_dialog);
        dialog.setCanceledOnTouchOutside(false);
        mTitle = (TextView) dialog.findViewById(R.id.title_dialog_yes_or_no_dialog);
        text = (TextView) dialog.findViewById(R.id.text_dialog_yes_or_no_dialog);
        mYes = (Button) dialog.findViewById(R.id.yes_dialog_yes_or_no_dialog);
        mNo = (Button) dialog.findViewById(R.id.no_dialog_yes_or_no_dialog);
    }

    public void setTitle(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText(msg);
    }

    public void setContent(String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        text.setVisibility(View.VISIBLE);
        text.setText(content);
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
        if (listener == null) {
            return;
        }
        mYes.setOnClickListener(listener);
        mNo.setOnClickListener(listener);
    }
}
