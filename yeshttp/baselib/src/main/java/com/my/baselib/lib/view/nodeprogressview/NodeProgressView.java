package com.my.baselib.lib.view.nodeprogressview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.my.baselib.R;
import com.my.baselib.lib.utils.UIUtils;

import java.util.List;

/**
 *  仿物流进度条
 */
public class NodeProgressView extends View {

    float width;
    float nodeRadius;

    Paint paint;

    Context context;

    /**
     * 节点间隔
     */
    int nodeInterval;

    /**
     * 边距
     */
    int left = 20;
    int top = 30;

    int dWidth;
    int dHeight;


    public NodeProgressView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public NodeProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NodeProgressView);
        width = typedArray.getDimension(R.styleable.NodeProgressView_width, 5);
        nodeRadius = typedArray.getDimension(R.styleable.NodeProgressView_nodeRadius, 10);
        init();
    }
    public NodeProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.base_sliver));
        paint.setAntiAlias(true);

        nodeInterval = dip2px(context, 80);

        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);

        dWidth = wm.getDefaultDisplay().getWidth();
        dHeight = wm.getDefaultDisplay().getHeight();
    }
    NodeProgressAdapter nodeProgressAdapter;
    /**
     * 设置适配数据
     */
    public void setNodeProgressAdapter(NodeProgressAdapter nodeProgressAdapter) {
        this.nodeProgressAdapter = nodeProgressAdapter;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (nodeProgressAdapter == null || nodeProgressAdapter.getCount() == 0)
            return;
        List<LogisticsData> data = nodeProgressAdapter.getData();
        //画左侧边线
        float heighttt=0;
        for (LogisticsData d:data){
            TextPaint textPaint = new TextPaint();
            textPaint.setColor(getResources().getColor(R.color.base_orange));
            textPaint.setTextSize(UIUtils.dip2px(13));
            textPaint.setAntiAlias(true);
            StaticLayout layout = new StaticLayout((d).getContent()+"", textPaint, (int) (dWidth  - UIUtils.dip2px(65)), Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
            heighttt=heighttt+layout.getHeight()+UIUtils.dip2px(45);
        }
        canvas.drawRect(left, top+UIUtils.dip2px(5), width + left, heighttt + top, paint);
        //定义高度，作为文字和圆圈的指示标
        float h=0;
        for (int i = 0; i < nodeProgressAdapter.getCount(); i++) {
            if (i == 0) {
                //文字内容
                TextPaint textPaint = new TextPaint();
                textPaint.setColor(getResources().getColor(R.color.base_orange));
                textPaint.setTextSize(UIUtils.dip2px(13));
                textPaint.setAntiAlias(true);
                StaticLayout layout = new StaticLayout(((LogisticsData)data.get(i)).getContent()+"", textPaint, (int) (dWidth  - UIUtils.dip2px(65)), Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
                int height = layout.getHeight();
                //时间
                Paint mPaint = new Paint();
                mPaint.setAntiAlias(true);
                mPaint.setColor(getResources().getColor(R.color.base_orange));
                mPaint.setTextSize(UIUtils.dip2px(10));
                canvas.drawText(((LogisticsData) data.get(i)).getTime() + "", left * 2 + nodeRadius * 2 + 10, height+UIUtils.dip2px(10) + top, mPaint);
                //写文字内容
                canvas.save();
                canvas.translate(left * 2 + nodeRadius * 2 + 10, UIUtils.dip2px(10));
                layout.draw(canvas);
                canvas.restore();//重置

                //画圆
                canvas.drawCircle(width / 2 + left, i * nodeInterval + top+UIUtils.dip2px(10), nodeRadius + 2, mPaint);
                mPaint.setStyle(Paint.Style.STROKE);//设置为空心
                mPaint.setStrokeWidth(8);//空心宽度
                mPaint.setAlpha(88);
                canvas.drawCircle(width / 2 + left, i * nodeInterval + top + UIUtils.dip2px(10), nodeRadius + 4, mPaint);

                h=h+UIUtils.dip2px(18)+height;
            } else {
                paint.setColor(getResources().getColor(R.color.base_gray_3));
                canvas.drawCircle(width / 2 + left,h + top+UIUtils.dip2px(10), nodeRadius, paint);
                //画线
                canvas.drawLine(left * 2 + nodeRadius * 2, h + top, dWidth, h + top, paint);
                //文字
                TextPaint textPaint = new TextPaint();
                textPaint.setColor(getResources().getColor(R.color.base_gray_3));
                textPaint.setTextSize(UIUtils.dip2px(13));
                textPaint.setAntiAlias(true);
                StaticLayout layout = new StaticLayout(((LogisticsData)data.get(i)).getContent()+"", textPaint, (int) (dWidth - UIUtils.dip2px(65)), Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
                int height = layout.getHeight();
                //时间
                paint.setTextSize(UIUtils.dip2px(10));
                canvas.drawText(((LogisticsData)data.get(i)).getTime()+"", left * 2 + nodeRadius * 2 + 10, h+height+top+UIUtils.dip2px(23), paint);
                //文字换行
                canvas.save();//很重要，不然会样式出错
                canvas.translate(left * 2 + nodeRadius * 2 + 10, top + h + UIUtils.dip2px(10));
                layout.draw(canvas);
                canvas.restore();//重置
                h=h+UIUtils.dip2px(35)+height;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (nodeProgressAdapter == null || nodeProgressAdapter.getCount() == 0)
            return;
        List<LogisticsData> data = nodeProgressAdapter.getData();
        float heighttt=0;
        //设置画布的大小
        for (LogisticsData d:data){
            TextPaint textPaint = new TextPaint();
            textPaint.setColor(getResources().getColor(R.color.base_orange));
            textPaint.setTextSize(UIUtils.dip2px(13));
            textPaint.setAntiAlias(true);
            StaticLayout layout = new StaticLayout((d).getContent()+"", textPaint, (int) (dWidth  - UIUtils.dip2px(65)), Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
            heighttt=heighttt+layout.getHeight()+UIUtils.dip2px(35);
        }
        setMeasuredDimension(widthMeasureSpec, (int) (heighttt + top-UIUtils.dip2px(10)));
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
