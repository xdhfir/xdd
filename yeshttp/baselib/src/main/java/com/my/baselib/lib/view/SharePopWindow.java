package com.my.baselib.lib.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.my.baselib.R;
import com.my.baselib.lib.utils.ActivityBackGroundUtils;

import java.util.ArrayList;


/**
 * Created by hp on 2017/1/10.
 */
public class SharePopWindow extends PopupWindow {
    private Activity mContext;
    private GridView gd;
    private TextView cancel;


    public SharePopWindow(ItemClickListener itemListener, final Activity mContext) {
        this(mContext, null);
        this.itemClickListener = itemListener;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_popouwidow_share, null);
        gd = (GridView) view.findViewById(R.id.gd);
        cancel = (TextView) view.findViewById(R.id.cancel_share);
        ShareAdapter shareAdapter = new ShareAdapter(mContext);
        gd.setAdapter(shareAdapter);
        this.setContentView(view);
        this.setWidth(MViewPager.LayoutParams.MATCH_PARENT);
        this.setHeight(MViewPager.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.AnimationActivity);
        /*ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);*/
        gd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(itemClickListener!=null){
                    itemClickListener.click(parent,view,position,id);
                }
                dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                ActivityBackGroundUtils.setActivityBackground(mContext,1f);
            }
        });

    }

    public SharePopWindow(Activity mContext, AttributeSet attributeSet) {
        super(mContext, attributeSet);
        this.mContext = mContext;
        View view = View.inflate(mContext, R.layout.view_popouwidow_share, null);
        gd = (GridView) view.findViewById(R.id.gd);
        cancel = (TextView) view.findViewById(R.id.cancel_share);
        ShareAdapter shareAdapter = new ShareAdapter(mContext);
        gd.setAdapter(shareAdapter);
        this.setContentView(view);
        this.setWidth(MViewPager.LayoutParams.MATCH_PARENT);
        this.setHeight(MViewPager.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.AnimationActivity);
        /*ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);*/
    }

    public interface ItemClickListener {
        void click(AdapterView<?> parent, View view, int position, long id);
    }

    private ItemClickListener itemClickListener;

    public class ShareAdapter extends BaseAdapter {
        private ArrayList<String> mNameList = new ArrayList<String>();
        private ArrayList<Drawable> mDrawableList = new ArrayList<Drawable>();
        private LayoutInflater mInflater;
        private Context mContext;

        public ShareAdapter(Context context) {
            mContext = context;
            initData();
        }

        private void initData() {
            mNameList.add("微信好友");
            mNameList.add("朋友圈");
            mNameList.add("QQ好友");
            mNameList.add("QQ空间");
            mNameList.add("新浪微博");
            mNameList.add("复制链接");
            mDrawableList.add(mContext.getResources().getDrawable(R.mipmap.base_wechat));
            mDrawableList.add(mContext.getResources().getDrawable(R.mipmap.base_wechat_cir));
            mDrawableList.add(mContext.getResources().getDrawable(R.mipmap.base_qq));
            mDrawableList.add(mContext.getResources().getDrawable(R.mipmap.base_qq_zone));
            mDrawableList.add(mContext.getResources().getDrawable(R.mipmap.base_sina));
            mDrawableList.add(mContext.getResources().getDrawable(R.mipmap.base_copy));
        }

        @Override
        public int getCount() {
            return mNameList.size();
        }

        @Override
        public Object getItem(int position) {
            return mNameList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ItemViewTag viewTag;
            if (convertView == null) {
                 mInflater = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.item_grid_share, null);
                viewTag = new ItemViewTag((ImageView) convertView.findViewById(R.id.img_platform),
                        (TextView) convertView.findViewById(R.id.name_platform));
                convertView.setTag(viewTag);
            } else {
                viewTag = (ItemViewTag) convertView.getTag();
            }
            viewTag.mName.setText(mNameList.get(position));
            viewTag.mIcon.setBackgroundDrawable(mDrawableList.get(position));
            return convertView;
        }

        class ItemViewTag {
            protected ImageView mIcon;
            protected TextView mName;

            public ItemViewTag(ImageView icon, TextView name) {
                this.mName = name;
                this.mIcon = icon;
            }
        }

    }


}
