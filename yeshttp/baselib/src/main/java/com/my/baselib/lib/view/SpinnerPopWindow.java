package com.my.baselib.lib.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.my.baselib.R;

import java.util.List;

/**
 * 下拉框控件
 */
public class SpinnerPopWindow<T> extends PopupWindow {

    private LayoutInflater inflater;
    private ListView mListView;
    private List<T> list;
    private MyAdapter mAdapter;

    public SpinnerPopWindow(Context context, List<T> list, ItemClickListener clickListener) {
        super(context);
        inflater = LayoutInflater.from(context);
        this.list = list;
        itemClickListener = clickListener;
        init(clickListener);
    }

    public interface ItemClickListener {
        void click(AdapterView<?> parent, View view, int position, long id);
    }

    private ItemClickListener itemClickListener;

    private void init(ItemClickListener clickListener) {
        View view = inflater.inflate(R.layout.spiner_window_layout, null);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);
        mListView = (ListView) view.findViewById(R.id.pop_listview);
        mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SpinnerPopWindow.this.dismiss();
                if (itemClickListener != null) {
                    itemClickListener.click(parent, view, position, id);
                }
            }
        });
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (list == null)
                return 0;
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            if (list == null)
                return 0;
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            if (list == null)
                return 0;
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.spiner_item_layout, null);
                holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvName.setText(getItem(position).toString());
            return convertView;
        }
    }

    private class ViewHolder {
        private TextView tvName;
    }
}



