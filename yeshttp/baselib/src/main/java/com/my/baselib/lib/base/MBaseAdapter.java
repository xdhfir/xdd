package com.my.baselib.lib.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.my.baselib.lib.utils.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 基类Adapter
 */
public abstract class MBaseAdapter<V> extends BaseAdapter {
    //定义一个集合，对ListView 需要的数据进行接收
    public List<V> mData = new ArrayList<V>();
    //定义一个集合，对ListViewz中Item的选择状态进行记录（方便做单选/多选）
    public HashMap<V, Boolean> map = new HashMap<V, Boolean>();
    //上下文，默认为全局上下文，可在构造是传入Activity
    public Context context = UIUtils.getContext();

    //空构造
    public MBaseAdapter() {

    }

    //包含数据构造
    public MBaseAdapter(List<V> mData) {
        this.mData.clear();
        this.mData.addAll(mData);
        this.map.clear();
        for (V v : mData) {
            map.put(v, false);
        }
    }

    //包含数据/上下文构造
    public MBaseAdapter(List<V> mData, Context context) {
        this.mData.clear();
        this.mData.addAll(mData);
        this.map.clear();
        for (V v : mData) {
            map.put(v, false);
        }
        this.context = context;
    }

    //重新设置数据
    @SuppressWarnings("unchecked")
    public void setData(List<V> mData) {
        this.mData.clear();
        this.mData.addAll(mData);
        this.map.clear();
        for (V v : mData) {
            map.put(v, false);
        }
        this.notifyDataSetChanged();
    }

    //清空数据
    public void clear() {
        this.mData.clear();
        this.map.clear();
        this.notifyDataSetChanged();
    }

    //获取ListView条目数
    @Override
    public int getCount() {
        if(mData!=null) {
            return mData.size();
        }
        return 0;
    }

    //获取该位置Item
    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseViewHolder baseViewHolder = BaseViewHolder.get(UIUtils.getContext(), convertView, idLayout());
        if (mData != null) {
            contactData2View(mData.get(position), baseViewHolder);
            contactData2View(mData.get(position), baseViewHolder,position);
        }
        return baseViewHolder.getConvertView();
    }



    protected void contactData2View(V v, BaseViewHolder baseViewHolder, int position) {

    }

    protected abstract void contactData2View(V item, BaseViewHolder baseViewHolder);

    protected abstract int idLayout();

    public HashMap<V, Boolean> getMap() {
        return map;
    }

    //传入被点击的Item位置，将该位置上的数据Item绑定状态为True（单选）
    public void singleClickItemToTrue(int position) {
        V v = mData.get(position);
        for (V i : map.keySet()) {
            if (i.equals(v)) {
                map.put(i, true);
            } else {
                map.put(i, false);
            }
        }
        this.notifyDataSetChanged();
    }

    //传入被点击的Item位置，将该位置上的数据Item绑定状态为True（多选），如果是false，则改成true
    public void clickItemToTrue(int position) {
        V v = mData.get(position);
        for (V i : map.keySet()) {
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

    //返回被选择的Item，如果没有则返回NULL
    public V getCheckedItem() {
        V v = null;
        for (V i : map.keySet()) {
            if (map.get(i)) {
                v = i;
            }
        }
        return v;
    }

    //返回所有被选择的Item
    public List<V> getAllCheckedItem(){
        List<V> list=new ArrayList<>();
        for (V i : map.keySet()) {
            if (map.get(i)) {
                list.add(i);
            }
        }
        return list;
    }

    //返回该Item是否被选择的状态
    public boolean getItemStatus(V v) {
        return map.get(v);
    }

    public void setCheckItem(V item) {
        for (V v : map.keySet()) {
            if (v.equals(item)) {
                map.put(v, true);
            } else {
                map.put(v, false);
            }
        }
        this.notifyDataSetChanged();
    }

    public void setCouponsCheckItem(V item) {
        for (V v : map.keySet()) {
            if (v.equals(item)) {
                if (map.get(v) == true) {
                    map.put(v, false);
                } else {
                    map.put(v, true);
                }
            } else {
                map.put(v, false);
            }
        }
        this.notifyDataSetChanged();
    }
    //选中条目置为不选中状态
    public void clearItemCheck(int position) {
        V v = mData.get(position);
        for (V i : map.keySet()) {
            if (i.equals(v)) {
                if (map.get(i)) {
                    map.put(i, false);
                } else {
                    map.put(i, false);
                }
            }
        }
        this.notifyDataSetChanged();
    }
    //全部置为不选中状态
    public void clearCheck(List<V> mData) {
        if (mData == null) {
            return;
        }
        for (int j = 0; j < mData.size(); j++) {
            V v = mData.get(j);
            for (V i : map.keySet()) {
                if (i.equals(v)) {
                    map.put(i, false);
                } else {
                    map.put(i, false);
                }
            }
        }
        this.notifyDataSetChanged();
    }
}
