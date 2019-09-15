package com.my.baselib.lib.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

/**
 * 处理图片的工具类
 */
public class ImageUtils {
    private static final int IMAGE_KEY = 0X99;

    public static void encode(File file1, File file2) {
        try {
            FileInputStream inputStream = new FileInputStream(file1);
            FileOutputStream outputStream = new FileOutputStream(file2);
            int read;
            if ((read = inputStream.read()) > -1) {
                outputStream.write(read ^ IMAGE_KEY);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
    *   @time 2017/4/1 10:34
    *   @desc 压缩图片，返回bitmap
    */
    public static Bitmap getImage (Bitmap bitmap, int size) throws Exception {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 960f;//这里设置高度为800 设置分辨率
        float ww = 640f;//这里设置宽度为480 设置分辨率
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
            if ((newOpts.outWidth / ww) > be) {
                be += 1;
            }

        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
            if ((newOpts.outHeight / hh) > be) {
                be += 1;
            }
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        return compressImage(bitmap, size);//压缩好比例大小后再进行质量压缩
    }

    private static Bitmap compressImage(Bitmap image, int size) throws Exception{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ((baos.toByteArray().length / 1024) >= size) { //循环判断如果压缩后图片是否大于等于size,大于等于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 5;//每次都减少5
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
    }

    /**
     * 根据图片路径进行压缩图片
     *
     * @param srcPath 图片路径
     * @param size    压缩到的大小
     * @return 返回图片压缩后的路径
     */
    public static String getImage(String srcPath, int size, boolean flag) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 960f;//这里设置高度为800 设置分辨率
        float ww = 640f;//这里设置宽度为480 设置分辨率
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
            if ((newOpts.outWidth / ww) > be) {
                be += 1;
            }

        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
            if ((newOpts.outHeight / hh) > be) {
                be += 1;
            }
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        bitmap = rotateBitmapByExif(srcPath, bitmap);
        return compressImage(srcPath, bitmap, size, flag);//压缩好比例大小后再进行质量压缩
    }

    /**
     * 压缩图片
     *
     * @param image
     * @param size
     * @param flag  true，表示不生成临时文件，false表示生成对应的临时文件
     * @return
     */
    private static String compressImage(String srcPath, Bitmap image, int size, boolean flag) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ((baos.toByteArray().length / 1024) >= size) { //循环判断如果压缩后图片是否大于等于size,大于等于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 5;//每次都减少5
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        }
        //ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        //Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片

        File src = new File(srcPath);
        if (!flag) {
            //File parentDir=src.getParentFile();
            String parentDir = FileUtils.getCacheDir();
            src = new File(parentDir, src.getName());//设置图片保存路径

        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(src);
            fos.write(baos.toByteArray());
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(fos);
        }
        return src.getAbsolutePath();
    }


    /**
     * 大图片压缩后并保存
     *
     * @param srcPath 图片所在路径
     * @param flag    true，原样裁剪，false 裁剪生成temp图片
     * @return
     */
    public static String saveImage(String srcPath, boolean flag) {
        return getImage(srcPath, 200, flag);
    }


    /**
     * 上传图片，该方法在应该在子线程中执行
     * 上传格式:http://junsucc.com/app/index.jsp?act=upload&username=j-3&sign=9283bffeb1956cddc3e528bcda0bd1f5
     * 下载格式:http://junsucc.com/app/服务器返回的图片地址
     *
     * @param url      请求地址
     * @param filePath 文件在手机中路径
     * @return
     * @throws IOException
     */
    public static String uploadImage(String url, String filePath) throws IOException {
        String savePath = null;
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识
        // 随机生成
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        /**
         * 第一部分
         */
        URL urlObj = new URL(url);


        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

        /**
         * 设置关键值
         */
        con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
        // con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false); // post方式不能使用缓存
        // 设置请求头信息 con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");

        // 设置边界 String BOUNDARY = "----------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary="
                + BOUNDARY);

        // 请求正文信息
        // 第一部分：
        StringBuilder sb = new StringBuilder();
        sb.append("--"); // ////////必须多两道线
        sb.append(BOUNDARY);
        sb.append("\r\n");
        //System.out.println(file.getName());
        sb.append("Content-Disposition: form-data;name=\"file\";filename=\""
                + file.getName() + "\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");

        byte[] head = sb.toString().getBytes("utf-8");

        // 获得输出流
        OutputStream out = new DataOutputStream(con.getOutputStream());
        out.write(head);

        // 文件正文部分
        DataInputStream in = new DataInputStream(new

                FileInputStream(file));
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();

        // 结尾部分
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
        out.write(foot);


        out.flush();
        out.close();

        /**
         * 读取服务器响应，必须读取,否则提交不成功
         */
        if (con.getResponseCode() == 200) {
            InputStream input = con.getInputStream();

            String ss = "";
            int data = 0;
            while ((data = input.read()) != -1) {
                ss += (char) data;
            }
            try {
                JSONObject obj = new JSONObject(ss);
                String code = obj.optString("code");
                savePath = obj.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return savePath;

    }

    /**
     * 服务器返回的url,如果格式不正确返回null
     *
     * @param logo 服务器的logo地址
     * @return
     */
    public static String getThumbUrl(String logo) {

        if (TextUtils.isEmpty(logo) || !logo.contains("/")) {
            return null;
        }
        String[] split = logo.split("/");
        logo = split[0] + "/" + "thumb_" + split[1];
        return logo;
    }

    /**
     * 旋转图片显示
     *
     * @param
     * @return
     */
    public static Bitmap rotateBitmapByExif(String srcPath, Bitmap bitmap) {
        ExifInterface exif;
        Bitmap newBitmap = null;
        try {
            exif = new ExifInterface(srcPath);
            if (exif != null) { // 读取图片中相机方向信息
                int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);
                int digree = 0;
                switch (ori) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        digree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        digree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        digree = 270;
                        break;
                }
                if (digree != 0) {
                    Matrix m = new Matrix();
                    m.postRotate(digree);
                    newBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                            bitmap.getWidth(), bitmap.getHeight(), m, true);
                    recycleBitmap(bitmap);
                    return newBitmap;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 回收位图对象
     *
     * @param bitmap
     * @return
     */
    public static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            System.gc();
            bitmap = null;
        }
    }
}

