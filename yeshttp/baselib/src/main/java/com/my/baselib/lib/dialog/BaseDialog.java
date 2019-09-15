package com.my.baselib.lib.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.my.baselib.R;
import com.my.baselib.lib.utils.LogUtils;

import java.util.HashMap;
/**
 *
 */
public class BaseDialog {
    private Activity mActivity;
    private Dialog dialog;
    private HashMap<Integer, View> map = new HashMap<>();

    public BaseDialog(Activity activity, int layoutId) {
        this.mActivity = activity;
        dialog = new Dialog(mActivity, R.style.dialog);
        View view = View.inflate(activity, layoutId, null);
        dialog.setContentView(view);
        map.clear();
        try {
            getAllViews((ViewGroup) view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAllViews(ViewGroup viewGroup) {
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt instanceof ViewGroup) {
                try {
                    getAllViews((ViewGroup) childAt);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            int id = childAt.getId();
            if (id != -1) {
                map.put(id, childAt);
            }
        }
    }

    public String getText(int id) {
        View view = map.get(id);
        try {
            if (view instanceof Button) {
                Button v = (Button) view;
                return v.getText().toString().trim();
            }
            if (view instanceof EditText) {
                EditText v = (EditText) view;
                return v.getText().toString().trim();
            }
            if (view instanceof TextView) {
                TextView v = (TextView) view;
                return v.getText().toString().trim();
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }

    //设置控件文字
    public void setText(String msg, int id) {
        View view = map.get(id);
        try {
            if (view instanceof TextView) {
                TextView v = (TextView) view;
                v.setText(msg);
            }
            if (view instanceof EditText) {
                EditText v = (EditText) view;
                v.setText(msg);
            }
            if (view instanceof Button) {
                Button v = (Button) view;
                v.setText(msg);
            }
        } catch (Exception e) {
            LogUtils.e("BaseDialog", "该控件不可设置文字");
        }
    }

    /**
     * @time 2017/7/13 10:41
     * @desc 设置点击监听
     */
    public void setOnClickListener(View.OnClickListener listener) {
        try {
            for (Integer i : map.keySet()) {
                View view = map.get(i);
                if (!view.hasOnClickListeners()) {
                    view.setOnClickListener(listener);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    *   @time 2017/7/13 11:26
    *   @desc 弹窗显示
    */
    //0 全屏
    //1 居于底部
    public void show(int type){
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        switch (type){
            case 0:
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.height =  WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(params);
                break;
            case 1:
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(params);
                window.setGravity(Gravity.BOTTOM);
                break;
        }
        show();
    }

    /**
     * @time 2017/7/13 9:47
     * @desc 弹窗显示
     */
    public void show() {
        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @time 2017/7/13 9:47
     * @desc 弹窗消失
     */
    public void dismiss() {
        try {
            dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
