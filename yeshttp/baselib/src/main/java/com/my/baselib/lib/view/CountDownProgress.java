package com.my.baselib.lib.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.my.baselib.R;


/**
 * 自定义圆形倒计时
 */
public class CountDownProgress extends View {

    //定义一些常量(大小写字母切换快捷键 Ctrl + Shift + U)
    private static final int DEFAULT_CIRCLE_SOLIDE_COLOR = Color.parseColor("#FFFFFF");
    private static final int DEFAULT_CIRCLE_STROKE_COLOR = Color.parseColor("#D1D1D1");
    private static final int DEFAULT_CIRCLE_STROKE_WIDTH = 3;
    private static final int DEFAULT_CIRCLE_RADIUS = 20;

    private static final int PROGRESS_COLOR = Color.parseColor("#F76E6B");
    private static final int PROGRESS_WIDTH = 3;
    private static final int TEXT_COLOR = Color.parseColor("#F76E6B");
    private static final int TEXT_SIZE = 14;

    //默认圆
    private int defaultCircleSolideColor = DEFAULT_CIRCLE_SOLIDE_COLOR;
    private int defaultCircleStrokeColor = DEFAULT_CIRCLE_STROKE_COLOR;
    private int defaultCircleStrokeWidth = dp2px(DEFAULT_CIRCLE_STROKE_WIDTH);
    private int defaultCircleRadius = dp2px(DEFAULT_CIRCLE_RADIUS);//半径
    //进度条
    private int progressColor = PROGRESS_COLOR;
    private int progressWidth = dp2px(PROGRESS_WIDTH);

    //最里层的文字
    private int textColor = TEXT_COLOR;
    private int textSize = sp2px(TEXT_SIZE);

    //画笔
    private Paint defaultCriclePaint;
    private Paint progressPaint;
    private Paint textPaint;

    //圆弧开始的角度
    private float mStartSweepValue = -90;
    //当前的角度
    private float currentAngle;
    //提供一个外界可以设置的倒计时数值，毫秒值
    private long countdownTime;
    //中间文字描述
    private String textDesc;

    //额外距离
    private float extraDistance = 0.7F;

    public CountDownProgress(Context context) {
        this(context, null);
    }

    public CountDownProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CountDownProgress);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = typedArray.getIndex(i);
            defaultCircleSolideColor = typedArray.getColor(attr, defaultCircleSolideColor);
            defaultCircleStrokeColor = typedArray.getColor(attr, defaultCircleStrokeColor);
            defaultCircleStrokeWidth = (int) typedArray.getDimension(attr, defaultCircleStrokeWidth);
            defaultCircleRadius = (int) typedArray.getDimension(attr, defaultCircleRadius);
            progressColor = typedArray.getColor(attr, progressColor);
            progressWidth = (int) typedArray.getDimension(attr, progressWidth);
            textColor = typedArray.getColor(attr, textColor);
            textSize = (int) typedArray.getDimension(attr, textSize);
        }
        //回收typedArray对象
        typedArray.recycle();
        //设置画笔
        setPaint();
    }

    private void setPaint() {
        //默认圆
        defaultCriclePaint = new Paint();
        defaultCriclePaint.setAntiAlias(true);//抗锯齿
        defaultCriclePaint.setDither(true);//防抖动
        defaultCriclePaint.setStyle(Paint.Style.STROKE);
        defaultCriclePaint.setStrokeWidth(defaultCircleStrokeWidth);
        defaultCriclePaint.setColor(defaultCircleStrokeColor);//这里先画边框的颜色，后续再添加画笔画实心的颜色
        //默认圆上面的进度弧度
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setDither(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(progressWidth);
        progressPaint.setColor(progressColor);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);//设置画笔笔刷样式

        //文字画笔
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize;
        int heightSize;
        int strokeWidth = Math.max(defaultCircleStrokeWidth, progressWidth);
        if (widthMode != MeasureSpec.EXACTLY) {
            widthSize = getPaddingLeft() + defaultCircleRadius * 2 + strokeWidth + getPaddingRight();
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        }
        if (heightMode != MeasureSpec.EXACTLY) {
            heightSize = getPaddingTop() + defaultCircleRadius * 2 + strokeWidth + getPaddingBottom();
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (canvas == null) {
            return;
        }
        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());
        //画默认圆
        canvas.drawCircle(defaultCircleRadius, defaultCircleRadius, defaultCircleRadius, defaultCriclePaint);
        //画进度圆弧
        canvas.drawArc(new RectF(0, 0, defaultCircleRadius * 2, defaultCircleRadius * 2), mStartSweepValue, 360 * currentAngle, false, progressPaint);
        //获取文字的长度的方法
        float textWidth = textPaint.measureText(textDesc);
        float textHeight = (textPaint.descent() + textPaint.ascent()) / 2;
        canvas.drawText(textDesc, defaultCircleRadius - textWidth / 2, defaultCircleRadius - textHeight, textPaint);
        canvas.restore();
    }

    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    /**
     * sp 2 px
     *
     * @param spVal
     * @return
     */
    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getResources().getDisplayMetrics());

    }

    //提供一个外界可以设置的倒计时数值
    public void setCountdownTime(long countdownTime) {
        this.countdownTime = countdownTime;
        textDesc = countdownTime / 1000 + "″";
    }

    //属性动画
    public void startCountDownTime(final OnCountdownFinishListener countdownFinishListener) {
        setClickable(false);
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1.0f);
        //动画时长，让进度条在CountDown时间内正好从0-360走完，这里由于用的是CountDownTimer定时器，倒计时要想减到0则总时长需要多加1000毫秒，所以这里时间也跟着+1000ms
        animator.setDuration(countdownTime + 1000);
        animator.setInterpolator(new LinearInterpolator());//匀速
        animator.setRepeatCount(0);//表示不循环，-1表示无限循环
        //添加监听器,监听动画过程中值的实时变化(animation.getAnimatedValue()得到的值就是0-1.0)
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                currentAngle = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        //开启动画
        animator.start();
        //还需要另一个监听，监听动画状态的监听器
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //倒计时结束的时候，需要通过自定义接口通知UI去处理其他业务逻辑
                if (countdownFinishListener != null) {
                    countdownFinishListener.countdownFinished();
                }
                if (countdownTime > 0) {
                    setClickable(true);
                } else {
                    setClickable(false);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        //调用倒计时操作
        countdownMethod();
    }

    //倒计时的方法
    private void countdownMethod() {
        new CountDownTimer(countdownTime + 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //         Log.e("time",countdownTime+"");
                countdownTime = countdownTime - 1000;
                //textDesc = countdownTime/1000 + "″";
                textDesc = "跳过";
                //刷新view
                invalidate();
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    //通过自定义接口通知UI去处理其他业务逻辑
    public interface OnCountdownFinishListener {
        void countdownFinished();
    }

}
