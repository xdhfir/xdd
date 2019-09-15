package com.my.baselib.lib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.my.baselib.R;

/**
 * 弹窗--接受或不接受
 */
public class AcceptOrNotDialog extends Dialog{

    private TextView mTitle;
    private TextView mContent;
    private Button mYes;
    private Button mNo;

    public AcceptOrNotDialog(Context context) {
        this(context, R.style.expand_dialog);
    }

    public AcceptOrNotDialog(Context context, int theme) {
        super(context, theme);
        initView(context);
        initData();
        initEvent();
    }

    private void initView(Context context) {
        this.setContentView(R.layout.view_dialog_accept_or_not_picker);
        mTitle = (TextView) findViewById(R.id.title_dialog_accept_or_not_picker);
        mContent = (TextView) findViewById(R.id.content_dialog_accept_or_not_picker);
        mYes = (Button) findViewById(R.id.yes_dialog_accept_or_not_picker);
        mNo = (Button) findViewById(R.id.no_dialog_accept_or_not_picker);
    }

    private void initData() {

    }

    private void initEvent() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = v.getId();
                if (i == R.id.yes_dialog_accept_or_not_picker) {
                    if (mAcceptOrNotDialogListener != null) {
                        mAcceptOrNotDialogListener.check(true);
                    }

                } else if (i == R.id.no_dialog_yes_or_no_dialog) {
                    if (mAcceptOrNotDialogListener != null) {
                        mAcceptOrNotDialogListener.check(false);
                    }

                }
                AcceptOrNotDialog.this.dismiss();
            }
        };
        mYes.setOnClickListener(listener);
        mNo.setOnClickListener(listener);
    }

    public void setTitle(String title){
        mTitle.setText(title);
    }

    public void setContent(String content){
        mContent.setText(content);
    }
    //回调接口
    public interface AcceptOrNotDialogListener{
        void check(boolean flag);
    }
    private AcceptOrNotDialogListener mAcceptOrNotDialogListener;
    public void setAcceptOrNotDialogListener(AcceptOrNotDialogListener acceptOrNotDialogListener){
        this.mAcceptOrNotDialogListener=acceptOrNotDialogListener;
    }
    public void setContentCenter(){
        mContent.setGravity(Gravity.CENTER);
    }
}
