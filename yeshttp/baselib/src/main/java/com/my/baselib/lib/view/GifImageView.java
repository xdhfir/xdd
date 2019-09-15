package com.my.baselib.lib.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.my.baselib.R;

import java.io.InputStream;
import java.lang.reflect.Field;

/**
 * Created by hp on 2017/1/13.
 * 播放动画的类
 *
 */
@SuppressLint("AppCompatCustomView")
public class GifImageView extends ImageView implements View.OnClickListener {

    private Movie mMovie;//播放动画需要用到的，系统类
    private int mImageWidth;//动画的imageview的宽度
    private int mImageHeight;//动画imageview的高度
    private long mMovieStart = 0;// 播放开始
    private boolean isAutoPlay;//是否自动播放
    private Bitmap mStartPlay;//开始按钮
    private boolean isPlaying=false;//记录是否正在播放
    private float mScale;

    /**
     * 适配宽高
     */
    private int mMeasuredGifWidth;
    private int mMeasuredGifHeight;

    public GifImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }
    public GifImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GifImageView(Context context) {
        super(context);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.GifImageView);
        // 通过反射拿布局中src的资源id,所以gif文件需要放在布局的src中
        int resourceId = getResourceId(attributes, context, attrs);
        if (resourceId != 0) {
            // 说明是gif动画
            // 1.将resourcesId变成流
            // 2.用Move来decode解析流
            // 3.获得bitmap的长宽
            InputStream is = getResources().openRawResource(resourceId);
            mMovie = Movie.decodeStream(is);
            if (mMovie != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                mImageWidth = bitmap.getWidth();
                mImageHeight = bitmap.getHeight();
                // 用完释放
                bitmap.recycle();
                // 获得是否允许自动播放，如果不允许自动播放，则初始化播放按钮
                isAutoPlay = attributes.getBoolean(R.styleable.GifImageView_auto_play, false);
                if (!isAutoPlay) {
                    mStartPlay = BitmapFactory.decodeResource(getResources(),R.mipmap.base_start_play);
                    setOnClickListener(this);
                }
            }
        }
        //回收资源
        if (attributes != null) {
            attributes.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mMovie != null) {
		/*
		 * Calculate horizontal scaling
		 */
            float scaleW = 1f;
            int measureModeWidth = MeasureSpec.getMode(widthMeasureSpec);
            if (measureModeWidth != MeasureSpec.UNSPECIFIED) {
                int maximumWidth = MeasureSpec.getSize(widthMeasureSpec);
                scaleW = (float) mImageWidth / (float) maximumWidth;
            }
		/*
		 * calculate vertical scaling
		 */
            float scaleH = 1f;
            int measureModeHeight = MeasureSpec.getMode(heightMeasureSpec);
            if (measureModeHeight != MeasureSpec.UNSPECIFIED) {
                int maximumHeight = MeasureSpec.getSize(heightMeasureSpec);
                scaleH = (float) mImageHeight / (float) maximumHeight;
            }
		/*
		 * calculate overall scale
		 */
            mScale = 1f / Math.max(scaleH, scaleW);
            mMeasuredGifWidth = (int) (mImageWidth * mScale);
            mMeasuredGifHeight = (int) (mImageHeight * mScale);
            setMeasuredDimension(mMeasuredGifWidth, mMeasuredGifHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mMovie == null) {
            // mMovie等于null，说明是张普通的图片，则直接调用父类的onDraw()方法
            super.onDraw(canvas);
        } else {
            // mMovie不等于null，说明是张GIF图片
            if (isAutoPlay) {
                // 如果允许自动播放，就播放
                playMovie(canvas);
                invalidate();
            } else {
                // 不允许自动播放的话
                // 1.判断是否正在播放
                // 2.获得第一帧的图像
                // 3.然后添加播放按钮
                if (isPlaying) {
                    // 如果正在播放就playmoive继续播放
                    if (playMovie(canvas)) {
                        isPlaying = false;
                    }
                    invalidate();
                } else {
                    // 第一帧
                    mMovie.setTime(0);
                    //canvas.save(Canvas.MATRIX_SAVE_FLAG);
                    canvas.save();
                    canvas.scale(mScale, mScale);
                    mMovie.draw(canvas, 0, 0);// 画
                    canvas.restore();
                    // 绘制开始按钮
                    int offsetW = (mMeasuredGifWidth - mStartPlay.getWidth()) / 2;
                    int offsetH = (mMeasuredGifHeight - mStartPlay.getHeight()) / 2;
                    canvas.drawBitmap(mStartPlay, offsetW, offsetH, null);
                }
            }
        }
    }

    /**
     * 播放gif动画
     *
     * @param canvas
     */
    private boolean playMovie(Canvas canvas) {
        // 1.获取播放的时间
        // 2.如果开始start=0，则认为是开始
        // 3.记录播放的时间
        // 4.设置进度
        // 5.画动画
        // 6.如果时间大于了播放的时间，则证明结束
        long now = SystemClock.uptimeMillis();
        if (mMovieStart == 0) {
            mMovieStart = now;
        }
        int duration = mMovie.duration();
        if (duration == 0) {
            duration = 1000;
        }
        //记录gif播放了多少时间
        int relTime = (int) ((now - mMovieStart) % duration);
        mMovie.setTime(relTime);// 设置时间
        //canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.save();
        canvas.scale(mScale, mScale);
        mMovie.draw(canvas, 0, 0);// 画
        canvas.restore();
        if ((now - mMovieStart) >= duration) {
            // 结束
            mMovieStart = 0;
            return true;
        }
        return false;
    }



    /**
     * 通过反射拿布局中src的资源id
     *
     * @param attrs
     * @param context
     * @param attributes
     */
    private int getResourceId(TypedArray attributes, Context context, AttributeSet attrs) {
        try {
            Field filed = TypedArray.class.getDeclaredField("mValue");
            filed.setAccessible(true);
            TypedValue typeValue = (TypedValue) filed.get(attributes);
            return typeValue.resourceId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==getId()){
            isPlaying=true;
            invalidate();
        }
    }

}
