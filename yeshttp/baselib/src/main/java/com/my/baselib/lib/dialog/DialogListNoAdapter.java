package com.my.baselib.lib.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.my.baselib.R;
import com.my.baselib.lib.base.MBaseAdapter;


/**
 * create by Administrator at 2017/5/2
 * description:
 */
public class DialogListNoAdapter implements View.OnClickListener {
    private Activity activity;
    private Dialog dialog;
    private final TextView title;
    private final TextView cancel;
    private final ListView list;
    private final ImageView back;
    private final Button bt;

    public DialogListNoAdapter(Activity activity, MBaseAdapter adapter, int type, boolean isCancelShow, boolean isBackShow, boolean isBtShow) {
        this.activity = activity;
        dialog = new Dialog(activity, R.style.dialog);
        dialog.setContentView(R.layout.dialog_list);
        title = (TextView) dialog.findViewById(R.id.title_text);
        cancel = (TextView) dialog.findViewById(R.id.img_cancel);
        list = (ListView) dialog.findViewById(R.id.select_list);
        back = (ImageView) dialog.findViewById(R.id.img_back);
        bt = (Button) dialog.findViewById(R.id.bt_);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onItemClickListener != null) {
                    onItemClickListener.click(position);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if (isBackShow) {
            back.setVisibility(View.VISIBLE);
        } else {
            back.setVisibility(View.GONE);
        }

        if (isBtShow) {
            bt.setVisibility(View.VISIBLE);
        } else {
            bt.setVisibility(View.GONE);
        }

        if (isCancelShow) {
            cancel.setVisibility(View.VISIBLE);
        } else {
            cancel.setVisibility(View.GONE);
        }
        back.setOnClickListener(this);
        cancel.setOnClickListener(this);
        bt.setOnClickListener(this);

        if (type == 0) {
            Window window = dialog.getWindow();
            if (dialog != null && window != null) {
                window.getDecorView().setPadding(0, 0, 0, 0);
                WindowManager.LayoutParams attr = window.getAttributes();
                if (attr != null) {
                    attr.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    attr.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    attr.gravity = Gravity.BOTTOM;//设置dialog 在布局中的位置
                    window.setAttributes(attr);
                }
            }
        }
    }

    public void showDialog() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void setTitle(String s) {
        title.setText(s);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (onClickListener == null) {
            return;
        }
        if (i == R.id.img_cancel) {
            onClickListener.click(1);
        } else if (i == R.id.img_back) {
            onClickListener.click(0);
        } else if (i == R.id.bt_) {
            onClickListener.click(3);
        }
    }

    public interface OnItemClickListener {
        void click(int i);
    }

    OnItemClickListener onItemClickListener;

    public void setOnItmeClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnClickListener {
        void click(int i);
    }

    OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
