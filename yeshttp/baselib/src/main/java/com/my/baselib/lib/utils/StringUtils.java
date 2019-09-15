package com.my.baselib.lib.utils;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ww
 */
public class StringUtils {

    public static String formatRequest(String s){
        if(TextUtils.isEmpty(s)){
            return "NULL";
        }else{
            return s;
        }
    }

    /**
     * @time 2017/2/17 13:38
     * @desc 隐藏手机号码中间4位
     */
    public static String addStarToPhone(String s) {
        if (!TextUtils.isEmpty(s) && s.length() == 11) {
            String start = s.substring(0, 3);
            String end = s.substring(7);
            return start + "****" + end;
        } else {
            return "";
        }
    }

    public static String saveDotTail2(String s) {
        if (s.contains(".")) {
            int i = s.lastIndexOf(".");
            int j = s.length() - i;
            if (j == 1) {
                return s + "00";
            }
            if (j == 2) {
                return s + "0";
            }
            if (j > 3) {
                return s.substring(0, i + 3);
            }
            return s;
        } else {
            return s + ".00";
        }
    }

    /**
     * @time 2017/3/1 15:18
     * @desc 给姓名添加*号
     */
    public static String addStarToName(String s) {
        if (!TextUtils.isEmpty(s) && s.length() >= 2) {
            String start = s.substring(0, 1);
            String end = "";
            if (s.length() > 2) {
                end = s.substring(2);
            }
            return start + "*" + end;
        } else {
            return "";
        }
    }

    //判断字符串是否符合手机号码的规则
    public static boolean isMobileNo(String mobiles) {
        Pattern p = Pattern.compile("[1][34578]\\d{9}");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    //将字符串输出到一个txt中
    public static void saveFile(String str) {
        String filePath = null;
        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (hasSDCard) {
            filePath = Environment.getExternalStorageDirectory().toString() + File.separator + "string.txt";
        } else
            filePath = Environment.getDownloadCacheDirectory().toString() + File.separator + "string.txt";
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(str.getBytes());
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //保留小数点后两位
    public static String formateString(String formate) {
        double d = Double.valueOf(formate);
        DecimalFormat decimalFormat = new DecimalFormat("###0.00");
        formate = decimalFormat.format(d);
        return formate;
    }

    //对字符串进行加1处理，主要针对我的账单功能，服务端返回0的问题
    public static String addString(String addString) {
        int i = Integer.valueOf(addString);
        i = i + 1;
        addString = String.valueOf(i);
        return addString;
    }
}