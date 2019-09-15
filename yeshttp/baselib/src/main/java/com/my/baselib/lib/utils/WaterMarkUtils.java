package com.my.baselib.lib.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;


import com.my.baselib.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 *
 */
public class WaterMarkUtils {
    public static void addWaterToBitmap(Bitmap bitmap,String path,Activity context,String location){
       // String location = LocationUtils.getLocation(context);
        String date = DateUtils.formatDate(System.currentTimeMillis() + "");
        String[] split = date.split(":");
        int width = bitmap.getWidth(), hight = bitmap.getHeight();
        Bitmap icon = Bitmap.createBitmap(width, hight, Bitmap.Config.ARGB_8888); // 建立一个空的BItMap
        Canvas canvas = new Canvas(icon);// 初始化画布绘制的图像到icon上
        Paint photoPaint = new Paint(); // 建立画笔
        photoPaint.setDither(true); // 获取跟清晰的图像采样
        photoPaint.setFilterBitmap(true);// 过滤一些
        Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());// 创建一个指定的新矩形的坐标
        Rect dst = new Rect(0, 0, width, hight);// 创建一个指定的新矩形的坐标
        canvas.drawBitmap(bitmap, src, dst, photoPaint);// 将photo 缩放或则扩大到
        Paint textPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);// 设置画笔
        textPaint1.setTextSize(20.0f);// 字体大小
        textPaint1.setTypeface(Typeface.DEFAULT_BOLD);// 采用默认的宽度
        textPaint1.setColor(Color.RED);// 采用的颜色

        Paint textPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);// 设置画笔
        textPaint2.setTextSize(20.0f);// 字体大小
        textPaint2.setTypeface(Typeface.DEFAULT_BOLD);// 采用默认的宽度
        textPaint2.setColor(Color.RED);// 采用的颜色

        Paint textPaint3 = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);// 设置画笔
        textPaint3.setTextSize(20.0f);// 字体大小
        textPaint3.setTypeface(Typeface.DEFAULT_BOLD);// 采用默认的宽度
        textPaint3.setColor(Color.RED);// 采用的颜色
        int ScreenWidth = context.getWindowManager().getDefaultDisplay().getWidth();
        int ScreenHeight = context.getWindowManager().getDefaultDisplay().getHeight();
        canvas.drawText(location, 20, hight - 50, textPaint1);// 绘制上去字，开始未知x,y采用那只笔绘制
        canvas.drawText("佰仟金融"+split[0], 20, hight-30,textPaint2);// 绘制上去字，开始未知x,y采用那只笔绘制
        canvas.drawText(split[1], 10, hight-10, textPaint3);// 绘制上去字，开始未知x,y采用那只笔绘制
        canvas.save();
        canvas.restore();
        saveFile(icon, path);
    }
    public static void saveFile(Bitmap bitmap, String fileName) {
        File dirFile = new File(fileName);
        // 检测图片是否存在
        if (dirFile.exists()) {
            dirFile.delete(); // 删除原图片
        }
        File myCaptureFile = new File(fileName);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            // 100表示不进行压缩，70表示压缩率为30%
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //给Bitmap添加水印
    public static Bitmap watermarkBitmap(Bitmap bitmap, String title, Context context, BitmapFactory.Options options) {
        if (bitmap == null) {
            return null;
        }
        Bitmap watermark = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_baselib, options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        // 需要处理图片太大造成的内存超过的问题,这里我的图片很小所以不写相应代码了
        Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        cv.drawBitmap(bitmap, 0, 0, null);// 在 0，0坐标开始画入src
        // 加入图片
        if (watermark != null) {
            int ww = watermark.getWidth();
            int wh = watermark.getHeight();
            Rect src = new Rect();// 图片
            Rect dst = new Rect();// 屏幕位置及尺寸
            src.left = 0; // 0,0
            src.top = 0;
            src.right = w;// 是桌面图的宽度，
            src.bottom = h;// 是桌面图的高度
            // 下面的 dst 是表示 绘画这个图片的位置
            dst.left = 0; // 绘图的起点X位置
            dst.top = 0; // 相当于 桌面图片绘画起点的Y坐标
            dst.right = ww + w - 60; // 表示需绘画的图片的右上角
            dst.bottom = wh + h - 60; // 表示需绘画的图片的右下角
            cv.drawBitmap(watermark, src, dst, null);// 在src的右下角画入水印
            src = null;
            dst = null;
        }
        // 加入文字
        if (title != null) {
            String familyName = "宋体";
            Typeface font = Typeface.create(familyName, Typeface.BOLD);
            TextPaint textPaint = new TextPaint();
            textPaint.setColor(Color.RED);
            textPaint.setTypeface(font);
            textPaint.setTextSize(22);
            textPaint.setAlpha(50);
            cv.drawText(title, 40, h - 30, textPaint);
        }
        //cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        cv.save();// 保存
        cv.restore();// 存储
        watermark.recycle();
        bitmap.recycle();
        return recycleBitmap(newb);
    }

    public static Bitmap recycleBitmap(Bitmap bitmap) {
        if (bitmap == null || bitmap.getConfig() != null) {
            return bitmap;
        }
        Bitmap newBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, false);
        bitmap.recycle();
        return newBitmap;
    }
}