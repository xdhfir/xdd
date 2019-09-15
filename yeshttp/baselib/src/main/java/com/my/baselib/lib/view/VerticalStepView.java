package com.my.baselib.lib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.my.baselib.lib.utils.UIUtils;


/**
 *
 */
public class VerticalStepView extends View{

    private Paint circlePaint;
    private Paint linePaint;
    private Paint textPaint;

    private int mRadius;
    private int topMargin;
    private int leftMargin;
    private int textLeftMargin;
    private int lineHeight;

    private String[] texts =new String[]{"第1步: 拨打运营商号码","第2步: 选择人工服务","第3步: 要求重置服务密码","第4步: 按照指引重置密码"};
    private int TEXT_COLOR = 0xFF8b8b8b;
    private int LINE_COLOR = 0xffe8e8e8;
    private int CIRCLE_COLOR = 0xffe8e8e8;

    public VerticalStepView(Context context) {
        this(context, null);
    }

    public VerticalStepView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalStepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(CIRCLE_COLOR);
        circlePaint.setStyle(Paint.Style.FILL);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(LINE_COLOR);
        linePaint.setStrokeWidth(1);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(TEXT_COLOR);
        textPaint.setTextSize(UIUtils.dip2px(15));

        mRadius = UIUtils.dip2px(5);
        lineHeight = UIUtils.dip2px(30);
        topMargin = UIUtils.dip2px(5);
        leftMargin = UIUtils.dip2px(15);
        textLeftMargin = UIUtils.dip2px(10);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredHeight = (int) Math.ceil(mRadius * 8 + lineHeight * 3 + topMargin * 2);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = widthMode == MeasureSpec.EXACTLY ? widthSize : getSuggestedMinimumWidth();
        int height = heightMode == MeasureSpec.EXACTLY ? heightSize : desiredHeight;

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < 4; i++) {
            int xCircle = mRadius + leftMargin;
            int yCircle = i * lineHeight + mRadius * (2 * i + 1) + topMargin;//圆心y坐标
            int yLineStart = (i - 1) * lineHeight + mRadius * 2 * i + topMargin;//画线开始的地方y坐标
            if (i > 0){
                canvas.drawLine(xCircle, yLineStart, xCircle, yCircle - mRadius, linePaint);
            }
            canvas.drawCircle(xCircle, yCircle, mRadius, circlePaint);

            Paint.FontMetrics fm = textPaint.getFontMetrics();
            float xText = xCircle + mRadius + textLeftMargin;
            float yText = yCircle - (fm.bottom - fm.top)/2 - fm.top;
            canvas.drawText(texts[i], xText , yText, textPaint);
        }
    }
}
