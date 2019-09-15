package com.my.baselib.lib.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.my.baselib.R;
import com.my.baselib.lib.utils.UIUtils;


/**
 *
 */
public class StepView extends View {

    private int mWidth;
    private int mHeight;
    private Paint circlePaint;
    private Paint circlePaintGray;
    private Paint linePaint;
    private Paint linePaintGray;
    private Paint textPaint;
    private Rect textBound;

    private int mRadius = UIUtils.dip2px(14);
    private int leftEdge;
    private int textMargin;
    private int circleMargin;
    private int stateNum = 4;
    private int lineWidth;

    private int[] reachStates = new int[]{1, 0, 0, 0};//到达状态
    private int[] doneStates = new int[]{0, 0, 0, 0};//完成状态
    private String[] texts = new String[]{"身份信息", "人脸识别", "绑储蓄卡","详细资料"};
    /*private int TEXT_COLOR = 0xFF34393e;
    private int LINE_COLOR = 0xffffcead;
    private int CIRCLE_COLOR = 0xffffae77; */
    private int TEXT_COLOR = 0xFF34393e;
    private int LINE_COLOR = 0xff5cbd18;
    private int CIRCLE_COLOR = 0xff5cbd18;
    private int GRAY=0xffb5b5b5;
    private Bitmap doneIcon;

    public StepView(Context context) {
        this(context, null);
    }

    public StepView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(CIRCLE_COLOR);
        circlePaint.setStrokeWidth(4);

        circlePaintGray = new Paint();
        circlePaintGray.setAntiAlias(true);
        circlePaintGray.setColor(GRAY);
        circlePaintGray.setStrokeWidth(4);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(LINE_COLOR);
        linePaint.setStrokeWidth(4);

        linePaintGray = new Paint();
        linePaintGray.setAntiAlias(true);
        linePaintGray.setColor(GRAY);
        linePaintGray.setStrokeWidth(4);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(TEXT_COLOR);
        textPaint.setTextSize(UIUtils.dip2px(14));
        textBound = new Rect();
        leftEdge = (int)textPaint.measureText(texts[0]) / 2 - mRadius + 40;
        textMargin = UIUtils.dip2px(10);
        circleMargin = 2;
        doneIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.base_done_white);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        int textHeight = (int) Math.ceil(fm.descent - fm.top);
        int desiredHeight = (int) Math.ceil(mRadius * 2 + textHeight + textMargin + circleMargin);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = widthMode == MeasureSpec.EXACTLY ? widthSize : getSuggestedMinimumWidth();
        int height = heightMode == MeasureSpec.EXACTLY ? heightSize : desiredHeight;

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        lineWidth = (mWidth - leftEdge * 2 - mRadius * 2 * stateNum) / (stateNum - 1);

        for (int i = 0; i < stateNum; i++) {
            int xCircle = i * lineWidth + mRadius * (2 * i + 1) + leftEdge;//圆心x坐标
            int xLineStart = (i - 1) * lineWidth + mRadius * 2 * i + leftEdge+10;//画线开始的地方x坐标
            if (i > 0) {
                if(reachStates[i]==1){
                    canvas.drawLine(xLineStart, mHeight - mRadius, xCircle - mRadius-10, mHeight - mRadius, linePaint);
                }else{
                    canvas.drawLine(xLineStart, mHeight - mRadius, xCircle - mRadius-10, mHeight - mRadius, linePaintGray);
                }
            }
            if (doneStates[i] == 1) {
                circlePaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(xCircle, mHeight - mRadius - circleMargin, mRadius, circlePaint);
            } else {
                if(reachStates[i]==0){
                    circlePaintGray.setStyle(Paint.Style.STROKE);
                    canvas.drawCircle(xCircle, mHeight - mRadius - circleMargin, mRadius, circlePaintGray);
                }else{
                    circlePaint.setStyle(Paint.Style.STROKE);
                    canvas.drawCircle(xCircle, mHeight - mRadius - circleMargin, mRadius, circlePaint);
                }
            }

            textPaint.getTextBounds(texts[i], 0, texts[i].length(), textBound);
            float xText = xCircle - textPaint.measureText(texts[i]) / 2;
            canvas.drawText(texts[i], xText, textBound.height(), textPaint);

            if (doneStates[i] == 1) {
                canvas.drawBitmap(doneIcon, xCircle - doneIcon.getWidth() / 2, mHeight - mRadius - circleMargin - doneIcon.getHeight() / 2, null);
            }
        }
    }

    public void setStep(int step) {
        if (step == 0) {
            reachStates = new int[]{1, 0, 0, 0};
            doneStates = new int[]{0, 0, 0, 0};
        }

        if (step == 1) {
            reachStates = new int[]{1, 1, 0, 0};
            doneStates = new int[]{1, 0, 0, 0};
        }

        if (step == 2) {
            reachStates = new int[]{1, 1, 1, 0};
            doneStates = new int[]{1, 1, 0, 0};
        }

        if (step == 3) {
            reachStates = new int[]{1, 1, 1, 1};
            doneStates = new int[]{1, 1, 1, 0};
        }

        if (step == 4) {
            reachStates = new int[]{1, 1, 1, 1};
            doneStates = new int[]{1, 1, 1, 1};
        }
        invalidate();
    }
}
