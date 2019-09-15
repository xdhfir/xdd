package com.my.baselib.lib.view.mrecyclerview;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

/**
 *  基类ViewHolder，RecyclerView使用
 */
public class RBaseViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> array;
    public View convertView;

    public RBaseViewHolder(View v) {
        super(v);
        array = new SparseArray<>();
        convertView = v;
    }

    public static RBaseViewHolder get(ViewGroup parent,int layoutId){
        View convert= LayoutInflater.from(parent.getContext()).inflate(layoutId,parent,false);
        return new RBaseViewHolder(convert);
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int id){
        View view = array.get(id);
        if(view==null){
            view=convertView.findViewById(id);
            array.put(id,view);
        }
        return (T)view;
    }
}
