package com.my.baselib.lib.view.loadmorelistview;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;


import com.my.baselib.R;
import com.my.baselib.lib.utils.LogUtils;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 上拉加载的ListView
 */
public class LoadMoreListView extends LinearLayout {
    private PtrClassicFrameLayout mPt;
    private RefreshListViewHeader mRefreshListViewHeader;
    private ListView mLv;
    private RefreshAndLoadMoreListener mRefreshAndLoadMoreListener;
    private View mFootView;
    private LinearLayout mFoot;
    private boolean isLoadMore = false;
    private boolean isRefresh = false;
    private Context context;
    private boolean isShowFoot = true;

    public LoadMoreListView(Context context) {
        this(context, null);
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(context);
        initEvent();
    }

    //设置listview边距和divider
    public void setDivider(int hight) {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        params.setMargins(0, 0, 0, 0);
        mLv.setLayoutParams(params);
        mLv.setDividerHeight(hight);
    }

    private void initView(Context context) {
        View v = View.inflate(context, R.layout.view_load_more_listview, this);
        mPt = (PtrClassicFrameLayout) v.findViewById(R.id.pt_view_load_more_list_view);
        if (mRefreshListViewHeader == null) {
            mRefreshListViewHeader = new RefreshListViewHeader(context);
        }
        mPt.setHeaderView(mRefreshListViewHeader);

        mLv = (ListView) v.findViewById(R.id.lv_view_load_more_list_view);

        mFootView = View.inflate(context, R.layout.foot_refresh_listview, null);
        ImageView loading = (ImageView) mFootView.findViewById(R.id.anmi_foot);
        AnimationDrawable anim = (AnimationDrawable) loading.getBackground();
        anim.start();
        mFoot = (LinearLayout) mFootView.findViewById(R.id.foot_refresh_list_view);
        mLv.addFooterView(mFootView);
        //初始化设置Foot为不显示
        mFoot.setVisibility(View.GONE);
    }

    private void initEvent() {
        mPt.addPtrUIHandler(mRefreshListViewHeader);
        mPt.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //进行数据的刷新/ /TODO
                if (mRefreshAndLoadMoreListener != null && !isRefresh) {
                    isRefresh = true;
                    mRefreshAndLoadMoreListener.refresh();
                }
                LogUtils.i("LoadMoreListView", "refresh");
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                //判断当前是否可以进行下拉刷新
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });

        mLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (!isShowFoot) {
                    return;
                }

                if (view.getLastVisiblePosition() == view.getCount() - 1 && !isLoadMore && view.getFirstVisiblePosition() != 0) {
                    //加载更多功能的代码
                    synchronized (LoadMoreListView.class) {
                        if (mRefreshAndLoadMoreListener != null && !isLoadMore) {
                            isLoadMore = true;
                            mFoot.setVisibility(View.VISIBLE);
                            // mLv.setSelection(mLv.getBottom());
                            mRefreshAndLoadMoreListener.loadMore();
                            LogUtils.i("LoadMoreListView", "LoadMore");
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view,
                                 int firstVisibleItem,
                                 int visibleItemCount,
                                 int totalItemCount) {
            }
        });
    }

    public void setAdapter(BaseAdapter baseAdapter) {
        mLv.setAdapter(baseAdapter);
    }

    /**
     * 重载,设定默认选择位置
     *
     * @param baseAdapter
     * @param position
     */
    public void setAdapter(BaseAdapter baseAdapter, int position) {
        mLv.setAdapter(baseAdapter);
        mLv.setSelection(position);
    }

    public void refreshComplete() {
        mPt.refreshComplete();
        isRefresh = false;
    }

    public void loadMoreComplete() {
        //进行延时，保证不同时多次加载
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                SystemClock.sleep(1000);
                subscriber.onNext("sleepOver");
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        mFoot.setVisibility(View.GONE);
                        isLoadMore = false;
                    }
                });
    }

    public void setSelection(int position){
        if(position>=0 && position<mLv.getCount()){
            mLv.setSelection(position);
        }
    }

    public void scrollToTop(){
        if(!mLv.isStackFromBottom()){
            mLv.setStackFromBottom(true);
        }
        mLv.setStackFromBottom(false);
    }

    public interface RefreshAndLoadMoreListener {
        void refresh();

        void loadMore();
    }

    public void setRefreshAndLoadMoreListener(RefreshAndLoadMoreListener refreshAndLoadMoreListener) {
        mRefreshAndLoadMoreListener = refreshAndLoadMoreListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mLv.setOnItemClickListener(listener);
    }

    public void setLoadMore(boolean loadMore) {
        isShowFoot = loadMore;
    }

    public void setRefreash(boolean refresh) {
        isRefresh = refresh;
    }
}
