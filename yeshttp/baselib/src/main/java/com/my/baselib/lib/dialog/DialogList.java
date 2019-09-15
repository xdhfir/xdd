package com.my.baselib.lib.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.my.baselib.R;
import com.my.baselib.lib.base.BaseViewHolder;
import com.my.baselib.lib.base.MBaseAdapter;

import java.util.List;

/**
 * create by Administrator at 2017/5/2
 * description:
 */
public class DialogList {
    private Activity activity;
    private Dialog dialog;
    private final TextView title;
    private final ImageView cancel;
    private final ListView list;
    private final DialogListAdapter dialogListAdapter;

    public DialogList(Activity activity) {
        this.activity = activity;
        dialog = new Dialog(activity, R.style.dialog);
        dialog.setContentView(R.layout.dialog_list_0);
        title = (TextView) dialog.findViewById(R.id.title_text);
        cancel = (ImageView) dialog.findViewById(R.id.img_cancel);
        list = (ListView) dialog.findViewById(R.id.select_list);
        dialogListAdapter = new DialogListAdapter();
        list.setAdapter(dialogListAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
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

        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        window.setGravity(Gravity.BOTTOM);
    }

    public void showDialog() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public void setTitle(String s) {
        title.setText(s);
    }

    public void setData(List list) {
        dialogListAdapter.setData(list);
    }

    private class DialogListAdapter extends MBaseAdapter {
        @Override
        protected void contactData2View(Object item, BaseViewHolder baseViewHolder) {
            String item1 = (String) item;
            TextView tv = baseViewHolder.getView(R.id.tv_item_dialog_list);
            tv.setText(item1);
        }

        @Override
        protected int idLayout() {
            return R.layout.item_dialog_list;
        }
    }

    public interface OnItemClickListener {
        void click(int i);
    }

    OnItemClickListener onItemClickListener;

    public void setOnItmeClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
