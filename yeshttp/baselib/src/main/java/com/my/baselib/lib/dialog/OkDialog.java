package com.my.baselib.lib.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.baselib.R;
import com.my.baselib.lib.utils.LogUtils;


/**
 * 正在加载中的弹窗
 */
public class OkDialog {
    private Activity activity;
    private final Dialog dialog;
    private final ImageView mIv;
    private final Button mBt;
    private final TextView mTv;
    private final TextView mTitle;

    public OkDialog(Activity activity) {
        this.activity = activity;
        dialog = new Dialog(activity, R.style.dialog);
        dialog.setContentView(R.layout.view_dialog_ok_dialog);
        dialog.setCanceledOnTouchOutside(false);
        mIv = (ImageView) dialog.findViewById(R.id.iv_dialog_ok_dialog);
        mBt = (Button) dialog.findViewById(R.id.bt_dialog_ok_dialog);
        mTv = (TextView) dialog.findViewById(R.id.tv_dialog_ok_dialog);
        mTitle = (TextView) dialog.findViewById(R.id.title_dialog_ok_dialog);
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

    public void setImageStatus(boolean flag){
        mIv.setVisibility(flag?View.VISIBLE:View.GONE);
    }

    public void setTitletatus(boolean flag){
        mTitle.setVisibility(flag?View.VISIBLE:View.GONE);
    }

    //判断Dialog是否正在显示中
    public boolean isDialogShowing(){
        return dialog.isShowing();
    }

    //给Dialog设置文字
    public void setButton(String text){
        if(TextUtils.isEmpty(text)){
            LogUtils.d("LoadingDialog", "输入为空");
            return;
        }
        mBt.setText(text);
    }

    //给Dialog设置图片
    public void setImage(Bitmap bitmap){
        if(bitmap==null){
            LogUtils.e("OkDialog","BitMap对象为空");
            return;
        }
        mIv.setImageBitmap(bitmap);
    }
    //设置文字
    public void setContent(String text){
        if(TextUtils.isEmpty(text)){
            LogUtils.d("LoadingDialog", "输入为空");
            return;
        }
        mTv.setText(text);
    }

    public void setTitle(String text){
        if(TextUtils.isEmpty(text)){
            LogUtils.d("LoadingDialog", "输入为空");
            return;
        }
        mTitle.setText(text);
    }

    //给BUTTON设置点击监听
    public void setClickListener(View.OnClickListener clickListener){
        if(clickListener==null){
            LogUtils.e("OkDialog","监听器为空");
            return;
        }
        mBt.setOnClickListener(clickListener);
    }
}
