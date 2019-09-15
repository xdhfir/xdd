package com.my.baselib.lib.view.mrecyclerview;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;


import androidx.recyclerview.widget.RecyclerView;

import com.my.baselib.lib.utils.UIUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * RecyclerView 的基类适配器
 */
public abstract class RBaseAdapter<T> extends RecyclerView.Adapter<RBaseViewHolder> {
    public static final int HEADER = 0;
    public static final int NORMAL = 1;
    public static final int FOOTER = 2;
    private View headerView;
    private View footerView;
    public boolean hasHeader = false;
    public boolean hasFooter = false;
    private List<T> mData = new ArrayList<>();
    public Context context = UIUtils.getContext();
    //定义一个集合，对ListViewz中Item的选择状态进行记录（方便做单选/多选）
    public HashMap<T, Boolean> map = new HashMap<T, Boolean>();
    //item点击事件监听器
    private ItemClickListener itemClickListener;

    /************************************************************/
    //抽象方法，子类必须继承
    public abstract int getLayoutId(int ViewType);

    public abstract void bindDataToView(RBaseViewHolder holder, T data, int position);

    /************************************************************/
    //添加头和尾
    public void addHeaderView(View v) {
        this.headerView = v;
    }

    public void addFooterView(View v) {
        this.footerView = v;
    }

    /***********************************************************/
    //扩展点击事件
    //传入被点击的Item位置，将该位置上的数据Item绑定状态为True（单选）
    public void singleClickItemToTrue(int position) {
        T v = mData.get(position);
        for (T i : map.keySet()) {
            if (i.equals(v)) {
                map.put(i, true);
            } else {
                map.put(i, false);
            }
        }
        this.notifyDataSetChanged();
    }

    //传入被点击的Item位置，将该位置上的数据Item绑定状态为True（多选）
    public void clickItemToTrue(int position) {
        T v = mData.get(position);
        for (T i : map.keySet()) {
            if (i.equals(v)) {
                if (map.get(i)) {
                    map.put(i, false);
                } else {
                    map.put(i, true);
                }
            }
        }
        this.notifyDataSetChanged();
    }

    //返回被选择的Item，如果没有则返回NULL//单选
    public T getSingleCheckedItem() {
        T v = null;
        for (T i : map.keySet()) {
            if (map.get(i)) {
                v = i;
                break;
            }
        }
        return v;
    }

    //返回被选择的位置，若没有，则返回-1//单选
    public int getSingleCheckedPosition() {
        T v = null;
        for (T i : map.keySet()) {
            if (map.get(i)) {
                v = i;
                break;
            }
        }

        if (v == null) {
            return -1;
        }

        int j = mData.size();
        int k = -1;
        for (int i = 0; i < j; i++) {
            if (v.equals(mData.get(i))) {
                k = i;
                break;
            }
        }
        return k;
    }

    //返回所有被选择的Item，如果没有则返回空集合//多选
    public List<T> getCheckedItem() {
        List<T> list = new ArrayList<>();
        for (T i : map.keySet()) {
            if (map.get(i)) {
                list.add(i);
            }
        }
        return list;
    }

    //返回全部被选择的位置，若没有，则返回空集合//多选
    public List<Integer> getCheckedPosition() {
        List<Integer> list = new ArrayList<>();
        for (T i : map.keySet()) {
            if (map.get(i)) {
                int j = mData.size();
                for (int n = 0; n < j; n++) {
                    if (i.equals(mData.get(n))) {
                        list.add(n);
                    }
                }
            }
        }
        return list;
    }

    //返回该Item是否被选择的状态
    public boolean getItemStatus(T v) {
        return map.get(v);
    }

    //传入集合全部置为不选中状态
    public void clearCheck(List<T> mData) {
        if (mData == null) {
            return;
        }
        for (int j = 0; j < mData.size(); j++) {
            T v = mData.get(j);
            map.put(v, false);
        }
        this.notifyDataSetChanged();
    }

    //全部置为不选中状态
    public void clearCheck() {
        for (T i : map.keySet()) {
            map.put(i, false);
        }
        this.notifyDataSetChanged();
    }

    //传入集合全部置为选中状态
    public void checkedAll(List<T> mData) {
        if (mData == null) {
            return;
        }
        for (int j = 0; j < mData.size(); j++) {
            T v = mData.get(j);
            map.put(v, true);
        }
        this.notifyDataSetChanged();
    }

    //全部置为选中状态
    public void checkedAll() {
        for (T i : map.keySet()) {
            map.put(i, true);
        }
        this.notifyDataSetChanged();
    }

    /************************************************************/
    @Override
    public RBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER) {
            return new RBaseViewHolder(headerView) {
            };
        } else if (viewType == FOOTER) {
            return new RBaseViewHolder(footerView);
        } else {
            return RBaseViewHolder.get(parent, getLayoutId(viewType));
        }
    }

    @Override
    public void onBindViewHolder(RBaseViewHolder holder,int position) {
        if (hasHeader && !hasFooter) {
            //只有header
            if (position != 0) {
                bindDataToView(holder, mData.get(position - 1), position - 1);
            }
        }

        if (!hasHeader && hasFooter) {
            //只有footer
            if (position != mData.size()) {
                bindDataToView(holder, mData.get(position), position);
            }
        }

        if (hasHeader && hasFooter) {
            //既有header，又有footer
            if (position != 0 && position != (mData.size() + 1)) {
                bindDataToView(holder, mData.get(position - 1), position - 1);
            }
        }

        if (!hasHeader && hasFooter) {
            //没有header,没有footer
            bindDataToView(holder, mData.get(position), position);
        }
        //点击事件
        final NumberCount numberCount = new NumberCount();
        numberCount.number=position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    if (hasHeader && !hasFooter) {
                        //只有header
                        if (numberCount.number != 0) {
                            itemClickListener.click(numberCount.number - 1);
                        }
                    }

                    if (!hasHeader && hasFooter) {
                        //只有footer
                        if (numberCount.number != mData.size()) {
                            itemClickListener.click(numberCount.number);
                        }
                    }

                    if (hasHeader && hasFooter) {
                        //既有header，又有footer
                        if (numberCount.number != 0 && numberCount.number != (mData.size() + 1)) {
                            itemClickListener.click(numberCount.number - 1);
                        }
                    }

                    if (!hasHeader && hasFooter) {
                        //没有header,没有footer
                        itemClickListener.click(numberCount.number);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (hasHeader && hasFooter) {
            return mData.size() + 2;
        } else if (!hasFooter && !hasHeader) {
            return mData.size();
        } else {
            return mData.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasFooter || hasHeader) {
            if (!hasHeader) {
                //只有footer
                if (position == mData.size()) {
                    //footer
                    return FOOTER;
                } else {
                    //normal
                    return NORMAL;
                }
            }

            if (!hasFooter) {
                //只有header
                if (position == 0) {
                    return HEADER;
                } else {
                    return NORMAL;
                }
            }

            //既有header，又有footer
            if (position == 0) {
                return HEADER;
            } else if (position == (mData.size() + 1)) {
                return FOOTER;
            } else {
                return NORMAL;
            }
        } else {
            //不存在Header,也不存在Footer
            return NORMAL;
        }
    }

    /*************************************************************/
    public interface ItemClickListener {
        void click(int position);
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /************************************************************/
    public RBaseAdapter() {

    }

    public RBaseAdapter(boolean hasHeader, boolean hasFooter) {
        this.hasHeader = hasHeader;
        this.hasFooter = hasFooter;
    }

    public RBaseAdapter(boolean hasHeader, boolean hasFooter, Activity activity) {
        this.hasHeader = hasHeader;
        this.hasFooter = hasFooter;
        this.context = activity;
    }

    public RBaseAdapter(List<T> data) {
        this.mData = data;
        for (T t : mData) {
            map.put(t, false);
        }
    }

    public RBaseAdapter(boolean hasHeader, boolean hasFooter, List<T> data) {
        this.hasHeader = hasHeader;
        this.hasFooter = hasFooter;
        this.mData = data;
        for (T t : mData) {
            map.put(t, false);
        }
    }

    public void setData(List<T> data) {
        this.mData.clear();
        this.mData.addAll(data);
        this.map.clear();
        for (T t : data) {
            map.put(t, false);
        }
        this.notifyDataSetChanged();
    }

    public void addData(List<T> data) {
        this.mData.addAll(data);
        for (T t : data) {
            this.map.put(t, false);
        }
        this.notifyDataSetChanged();
    }

    public void removeData(List<T> data) {
        this.mData.removeAll(data);
        for (T t : data) {
            this.map.remove(t);
        }
        this.notifyDataSetChanged();
    }

    public void removeData(int position) {
        this.mData.remove(position);
        this.notifyDataSetChanged();
    }

    public List getData() {
        return mData;
    }

    public void swap(int from, int to) {
        Collections.swap(mData, from, to);
    }

    class NumberCount{
        public int number;
    }
}
