package com.my.baselib.lib.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.my.baselib.R;
import com.my.baselib.lib.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 轮播图
 */
public class MBanner extends RelativeLayout {
    private Context mContext;
    private View v;
    private ViewPager mVp;
    private LinearLayout mLl;
    private View mDot;
    private boolean isShowDot = false;
    private boolean isExpandRun = false;

    private List mData = new ArrayList<>();
    private MyPagerAdapter myPagerAdapter;
    private Drawable drawable;
    private boolean flag;

    public MBanner(Context context) {
        this(context, null);
    }

    public void setDefaultImge(Drawable drawable) {
        this.drawable = drawable;
    }

    public void isAuToRun(boolean flag) {
        this.flag = flag;
    }

    public MBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        v = View.inflate(context, R.layout.m_banner, this);

        mVp = (ViewPager) v.findViewById(R.id.vp_m_banner);
        mLl = (LinearLayout) v.findViewById(R.id.ll_dot_m_banner);
        mDot = v.findViewById(R.id.dot_red_m_banner);

        if (myPagerAdapter == null) {
            myPagerAdapter = new MyPagerAdapter();
        }
        mVp.setAdapter(myPagerAdapter);
        v.setVisibility(View.GONE);
        initEvent();
        mVp.setCurrentItem(0);
    }

    private void initEvent() {
        mVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mData.size() > 0) {
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mDot.getLayoutParams();
                    layoutParams.leftMargin = Math.round(UIUtils.dip2px(18) * (position % mData.size()));
                    mDot.setLayoutParams(layoutParams);
                    if (pagerListener != null) {
                        pagerListener.onPagerChange(position);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public interface PagerChangeListener {
        void onPagerChange(int position);
    }

    private PagerChangeListener pagerListener;

    public void setPagerChangeListener(PagerChangeListener pagerListener) {
        this.pagerListener = pagerListener;
    }

    public void setData(List data, boolean isShowDot, boolean isExpandRun) {
        this.isExpandRun = isExpandRun;
        this.isShowDot = isShowDot;
        this.mData.clear();
        this.mData.addAll(data);
        v.setVisibility(View.VISIBLE);
        if (isShowDot) {
            addDot();
        } else {
            mDot.setVisibility(View.GONE);
        }
        if (mData.size() < 1) {
            Log.i("MBanner", "集合内无数据");
            return;
        }

        myPagerAdapter.notifyDataSetChanged();
    }


    private void addDot() {
        for (int i = 0; i < mData.size(); i++) {
            View v = new View(mContext);
            int dis = UIUtils.dip2px(9);
            v.setBackgroundResource(R.drawable.circle_gray);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dis, dis);
            if (i != 0) {
                //不是第一个点
                //设置左边距10
                lp.leftMargin = dis;
            }
            v.setLayoutParams(lp);
            //添加到容器中
            mLl.addView(v);
        }
        if (flag) {
            handler.sendEmptyMessageDelayed(0, 2000);
        }
    }

    @SuppressWarnings("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            //让ViewPager滑到下一页
            int currentItem = mVp.getCurrentItem();
            mVp.setCurrentItem((currentItem + 1));
            //延时，循环用handler
            handler.sendEmptyMessageDelayed(0, 3000);
        }
    };

    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            if (isExpandRun) {
                return Integer.MAX_VALUE;
            } else {
                return mData.size();
            }
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            if (mData.size() > 0) {
                ImageView v = new ImageView(mContext);
                v.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.clear(v);
                Glide.with(mContext).load(mData.get(position % mData.size()))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(v);

                v.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (itemClickListener != null) {
                            itemClickListener.click(position);
                        }
                    }
                });
                container.addView(v);
                return v;
            } else {
                return null;
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    public interface OnItemClickListener {
        void click(int position);
    }

    private OnItemClickListener itemClickListener;

    public void setItemClickListener(OnItemClickListener onItemClickListener) {
        this.itemClickListener = onItemClickListener;
    }
}
