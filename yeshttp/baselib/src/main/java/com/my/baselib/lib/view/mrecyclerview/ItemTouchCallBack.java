package com.my.baselib.lib.view.mrecyclerview;


import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.my.baselib.lib.utils.UIUtils;

import java.util.Collections;
import java.util.List;

/**
 * item的按下事件回调
 */
public class ItemTouchCallBack<T> extends ItemTouchHelper.Callback {
    private RBaseAdapter mAdapter;
    private List<T> mData;
    private int selectColor = 0x888888;
    private int realColor = 0x333333;
    private int actionType = 2;

    public ItemTouchCallBack(RBaseAdapter mAdapter, List<T> mData) {
        this.mAdapter = mAdapter;
        this.mData = mData;
    }

    public ItemTouchCallBack(RBaseAdapter mAdapter, List<T> mData, int selectColor, int realColor) {
        this(mAdapter, mData);
        this.realColor = realColor;
        this.selectColor = selectColor;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        //int swipeFlag = ItemTouchHelper.START | ItemTouchHelper.END;//左滑、右滑，删除
        int swipeFlag = ItemTouchHelper.END;//右滑，删除
        int position = viewHolder.getAdapterPosition();
        if (mAdapter.hasHeader) {
            if (position == 0) {
                return 0;
            }

            if (mAdapter.hasFooter) {
                if (position == (mData.size() + 1)) {
                    return 0;
                }
            }
        } else if (mAdapter.hasFooter) {
            if (position == mData.size()) {
                return 0;
            }
        }
        return makeMovementFlags(dragFlag, swipeFlag);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int from = viewHolder.getAdapterPosition();
        int to = target.getAdapterPosition();
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }

        if (mAdapter.hasHeader) {
            if (from >= 1 && from <= mData.size() && to >= 1 && to <= mData.size()) {
                Collections.swap(mData, from - 1, to - 1);
                mAdapter.notifyItemMoved(from, to);
                mAdapter.swap(from - 1, to - 1);
            }
        } else {
            if (from >= 0 && from < mData.size() && to >= 0 && to < mData.size()) {
                Collections.swap(mData, from, to);
                mAdapter.notifyItemMoved(from, to);
                mAdapter.swap(from, to);
            }
        }
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        if (mAdapter.hasHeader) {
            position = position - 1;
        }
        if (position < mData.size() && position >= 0) {
            mData.remove(position);
            mAdapter.removeData(position);
        }
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        actionType = actionState;
        if (actionType == 0) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(100);
                        UIUtils.postTaskSafely(new Runnable() {
                            @Override
                            public void run() {
                                if (mAdapter != null) {
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
    }
}
