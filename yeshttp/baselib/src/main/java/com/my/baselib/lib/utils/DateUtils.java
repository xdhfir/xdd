package com.my.baselib.lib.utils;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
public class DateUtils {
    public static String formatDate(String time){
        try{
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d1=new Date(Long.parseLong(time));
            String t1=format.format(d1);
            return t1;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static String formatDate(long time){

        try{
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d1=new Date(time);
            String t1=format.format(d1);
            return t1;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static String formatMessageDate(String time){
        try{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Date d1 = new Date(Long.parseLong(time));
            String t1 = format.format(d1);
            return t1;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }



    public static String formatStringDate(String time){
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            date = format.parse(time);
            String t1 = format.format(date);
            return t1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String formatCouponsDate(String time){
        try{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = new Date(Long.parseLong(time));
            String t1 = format.format(d1);
            return t1;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }


    public static int compareDate(String DATE1, String DATE2) {


        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public static String formatMessageDate(long time){
        try{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date d1 = new Date(time);
            String t1 = format.format(d1);
            return t1;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 将一个时间戳转换成提醒时间字符串
     * @param timeStamp
     * @return
     */
    public static String convertTimeToFormat(long timeStamp) {
        long curTime =System.currentTimeMillis()  ;
        String currentTime     = formatMessageDate(curTime);
        String[] s_year = currentTime.split("-");
        String currentyear = s_year[0];//当前年份
        String[] s_curTime = currentTime.split(" ");
        String cur_day = s_curTime[0];//当前日期天

        String s     = formatMessageDate(timeStamp);
        if (TextUtils.isEmpty(s)){
            return "";
        }
        String[] split = s.split(" ");
        String s1 = split[0];//年月日
        String s2 = split[1];//时分
        String[] split1 = s1.split("-");
        String year = split1[0];
        String month;
        if (split1[1].startsWith("0")){
            month = split1[1].replace("0","");
        }else {
            month = split1[1];
        }
        String day;
        if (split1[2].startsWith("0")){
            day = split1[2].replace("0","");
        }else {
            day = split1[2];
        }
        StringBuilder sb = new StringBuilder();

        long time = (curTime - timeStamp)/(long) 1000;
        if (time < 3600 * 24 && time >= 0) {
            if (cur_day.equals(s1)){
                return s2;
            }else {//小于24小时但不是同一天
                return "昨天" + s2;
            }

        }else if (time >= 3600 * 24 && time < 3600 * 24 * 2) {//若为昨天（昨天的0点-24点），显示“昨天+时间”
            return sb.append(month).append("月").append(day).append("日").toString();

        } else if (time >= 3600 * 24 * 2 && time < 3600 * 24 * 30 * 12) {//若为前天至当年，显示“x月x日”
            if (currentyear.equals(year)){
                return sb.append(month).append("月").append(day).append("日").toString();
            }else {//间隔小于12个月但不是同一年
                return sb.append(year).append("年").append(month).append("月").append(day).append("日").toString();
            }
        } else if (time >= 3600 * 24 * 30 * 12) {//若为非当年，显示“xxxx年x月x日”

            return sb.append(year).append("年").append(month).append("月").append(day).append("日").toString();
        } else {
            return s2;
        }
    }

}
