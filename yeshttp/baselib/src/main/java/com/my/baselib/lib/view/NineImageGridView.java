package com.my.baselib.lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.my.baselib.R;
import com.my.baselib.lib.utils.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 这是一个自定义View,点击可添加图片item，或者删除图片item
 */
public class NineImageGridView extends GridView {
    private int itemW=80;
    private int itemH=80;
    private int num=3;
    private float h2w=1;
    private int mLimitNumber=3;//默认等于9
    private List<File> mData = new ArrayList<>();
    private MAdapter mAdapter;
    private Context context;

    public NineImageGridView(Context context) {
        this(context, null);
    }

    public NineImageGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        mData.add(new File("abc"));
        if(mAdapter==null){
            mAdapter=new MAdapter();
        }
        this.setAdapter(mAdapter);
    }

    //必须使用，内部无法获取到这两个属性
    public void setWidthAndColumns(int wid,int col){
        num=col;
        int paddingLeft = this.getPaddingLeft();
        int paddingRight = this.getPaddingRight();
        itemW=(wid-paddingLeft-paddingRight)/num;
        itemH=(int)(itemW*h2w);
    }

    //设置图片个数，默认为3
    public void setLimitNumber(int i){
        mLimitNumber=i;
    }

    public List<File> getFile(){
        List<File> data=new ArrayList<>();
        for (File f:mData){
            if(f.exists()){
                data.add(f);
            }
        }
        return data;
    }

    //设置高宽比，默认为1
    public void setItemH2W(float i){
        h2w=i;
    }

    //从相机或相册获取图片之后，将图片设置GridView中
    public void setImage(File file,int i){
        if(i>=mData.size()){
            LogUtils.e("NineImageGridView", "设置图片角标越界");
            return;
        }
        mData.remove(i);
        mData.add(i, file);

        if(mData.size()<mLimitNumber){
            mData.add(new File("abc"));
        }
        mAdapter.notifyDataSetChanged();
    }

    class MAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if(mData!=null) {
                return mData.size();
            }
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder=null;
            if(convertView==null){
                viewHolder=new ViewHolder();
                convertView=View.inflate(context, R.layout.item_nine_image_grid_view,null);
                viewHolder.parent= (RelativeLayout) convertView.findViewById(R.id.parent_item_nine_image_grid_view);
                viewHolder.content= (ImageView) convertView.findViewById(R.id.content_item_nine_image_grid_view);
                viewHolder.delete= (ImageView) convertView.findViewById(R.id.delete_item_nine_image_grid_view);
                convertView.setTag(viewHolder);
            }else{
                viewHolder= (ViewHolder) convertView.getTag();
            }

            ///自适应item的宽高
            ViewGroup.LayoutParams params =viewHolder.content.getLayoutParams();
            params.height=itemH;
            params.width=itemW;
            viewHolder.content.setLayoutParams(params);

            final File file = mData.get(position);

            if(file.length()==0){
                //说明这张图片不存在，应该显示为添加图片样式
                viewHolder.delete.setVisibility(View.GONE);//隐藏删除按钮
                Glide.with(context).load(R.mipmap.base_add).into(viewHolder.content);
            }else{
                viewHolder.delete.setVisibility(View.VISIBLE);//显示删除按钮
                Glide.with(context).load(file).into(viewHolder.content);
            }

            viewHolder.delete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mData.remove(position);
                    mAdapter.notifyDataSetChanged();
                    int count=0;
                    for (File f:mData){
                        if(f.exists()){
                            count++;
                        }
                    }
                    if(count==2){
                        mData.add(new File("abc"));
                    }
                }
            });
            return convertView;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        class ViewHolder{
            RelativeLayout parent;
            ImageView content;
            ImageView delete;
        }
    }
}
