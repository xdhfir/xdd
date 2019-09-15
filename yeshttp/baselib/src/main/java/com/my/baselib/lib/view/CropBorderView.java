package com.my.baselib.lib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 *
 */
public class CropBorderView extends View {
    private Paint mPaint;
    private int mHorizontalPadding;//default
    private int mVerticalPadding;
    private int mBorderWidth = 1;
    private int mCropWidth;

    public CropBorderView(Context context) {
        this(context, null);
    }

    public CropBorderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CropBorderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.i("CropBorderView", "mHorizontalPadding=" + mHorizontalPadding);
        mHorizontalPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources().getDisplayMetrics());
        mBorderWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mBorderWidth, getResources().getDisplayMetrics());
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mCropWidth = (getWidth() - 2 * mHorizontalPadding);
        mVerticalPadding = (getHeight() - mCropWidth) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.parseColor("#aa000000"));
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, mHorizontalPadding, getHeight(), mPaint);
        canvas.drawRect(mHorizontalPadding, 0, mHorizontalPadding + mCropWidth, mVerticalPadding, mPaint);
        canvas.drawRect(mHorizontalPadding + mCropWidth, 0, getWidth(), getHeight(), mPaint);
        canvas.drawRect(mHorizontalPadding, mVerticalPadding + mCropWidth, mHorizontalPadding + mCropWidth, getHeight(), mPaint);
        mPaint.setColor(Color.parseColor("#ffffff"));
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(mHorizontalPadding, mVerticalPadding, mHorizontalPadding + mCropWidth, mVerticalPadding + mCropWidth, mPaint);
    }

    /**
     *
     */
    public void setHorizontalPadding(int padding) {
        this.mHorizontalPadding = padding;
    }

}
