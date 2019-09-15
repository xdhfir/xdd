package com.my.baselib.lib.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.my.baselib.R;


/**
 * 版本更新的彈窗，是否選擇更新
 */
public class VersionUpdateDialog {
    public Activity activity;
    private final Dialog dialog;
    private final TextView mContent;
    private final Button mYes;
    private final Button mNo;

    public VersionUpdateDialog(Activity activity) {
        this.activity = activity;
        dialog = new Dialog(activity, R.style.dialog);
        dialog.setContentView(R.layout.view_dialog_version_update_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(keylistener);
        mContent = (TextView) dialog.findViewById(R.id.content_dialog_version_update_dialog);
        mYes = (Button) dialog.findViewById(R.id.yes_dialog_version_update_dialog);
        mNo = (Button) dialog.findViewById(R.id.no_dialog_version_update_dialog);
    }

    public void showDialog(){
        if(!dialog.isShowing()){
            dialog.show();
        }
    }
    public void dismissDialog(){
        if(dialog.isShowing()){
            dialog.dismiss();
        }
    }

    public void setOnClickListener(View.OnClickListener listener){
        if(listener==null){
            return;
        }
        mYes.setOnClickListener(listener);
        mNo.setOnClickListener(listener);
    }

    public void setContent(String content){
        if(TextUtils.isEmpty(content)){
            return;
        }
        mContent.setText(content);
    }

    public boolean isShow(){
        return dialog.isShowing();
    }

    DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener(){
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            return keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0;
        }
    } ;
}
