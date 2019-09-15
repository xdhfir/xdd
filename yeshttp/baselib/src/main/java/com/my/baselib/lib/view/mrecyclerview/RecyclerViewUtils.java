package com.my.baselib.lib.view.mrecyclerview;

import android.annotation.SuppressLint;
import android.content.Context;


import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.List;

/**
 *
 */
public class RecyclerViewUtils {
    //线性
    @SuppressWarnings("unchecked")
    public static void setLinearLayoutRecyclerViewH(Context context, List data, RecyclerView recyclerView, RBaseAdapter adapter, boolean isAnimation) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter.setData(data);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper helper=new ItemTouchHelper(new ItemTouchCallBack<>(adapter,data));
        helper.attachToRecyclerView(recyclerView);
        if (isAnimation) {
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }
    @SuppressLint("WrongConstant")
    @SuppressWarnings("unchecked")
    public static void setLinearLayoutRecyclerViewV(Context context,List data, RecyclerView recyclerView, RBaseAdapter adapter, boolean isAnimation) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter.setData(data);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper helper=new ItemTouchHelper(new ItemTouchCallBack<>(adapter,data));
        helper.attachToRecyclerView(recyclerView);
        if (isAnimation) {
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }
    //grid
    @SuppressWarnings("unchecked")
    public static void setGridLayoutRecyclerViewH(Context context,List data, int count,RecyclerView recyclerView, RBaseAdapter adapter, boolean isAnimation) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,count);
        gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter.setData(data);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper helper=new ItemTouchHelper(new ItemTouchCallBack<>(adapter,data));
        helper.attachToRecyclerView(recyclerView);
        if (isAnimation) {
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }

    @SuppressLint("WrongConstant")
    @SuppressWarnings("unchecked")
    public static void setGridLayoutRecyclerViewV(Context context,List data, int count,RecyclerView recyclerView, RBaseAdapter adapter, boolean isAnimation) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,count);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter.setData(data);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper helper=new ItemTouchHelper(new ItemTouchCallBack<>(adapter,data));
        helper.attachToRecyclerView(recyclerView);
        if (isAnimation) {
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }

    //瀑布流
    @SuppressWarnings("unchecked")
    public static void setStaggeredLayoutRecyclerViewH(Context context,List data, int count,RecyclerView recyclerView, RBaseAdapter adapter, boolean isAnimation) {
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(count,StaggeredGridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        adapter.setData(data);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper helper=new ItemTouchHelper(new ItemTouchCallBack<>(adapter,data));
        helper.attachToRecyclerView(recyclerView);
        if (isAnimation) {
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }
    @SuppressWarnings("unchecked")
    public static void setStaggeredLayoutRecyclerViewV(Context context,List data, int count,RecyclerView recyclerView, RBaseAdapter adapter, boolean isAnimation) {
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(count,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        adapter.setData(data);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper helper=new ItemTouchHelper(new ItemTouchCallBack<>(adapter,data));
        helper.attachToRecyclerView(recyclerView);
        if (isAnimation) {
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }
}
