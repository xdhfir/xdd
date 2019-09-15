package com.my.baselib.lib.base;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;

/**
 * 基类ViewHolder
 */
public class BaseViewHolder {
    private final SparseArray<View> mViews;
    private View mConvertView;

    private BaseViewHolder(Context context ,int layoutId) {
        this.mViews = new SparseArray<View>();
        mConvertView =View.inflate(context,layoutId,null);
        mConvertView.setTag(this);
    }

    public static BaseViewHolder get(Context context, View convertView,
                                  int layoutId) {
        if (convertView == null) {
            return new BaseViewHolder(context,layoutId);
        }
        return (BaseViewHolder) convertView.getTag();
    }

    public View getConvertView() {
        return mConvertView;
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }
}
