package com.my.baselib.lib.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具类
 */
public class Md5Utils {

    // first argument is the password need MD5
    // second argument is algorithm
    // third argument is separate symbol
    public String toMd5(String original, String separator) {
        try {
            String result;
            byte[] bytes = original.getBytes();
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(bytes);
            result = toHexString(algorithm.digest(), separator);
            return result;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String toHexString(byte[] bytes, String separator) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", 0xFF & b)).append(separator);
        }
        return hexString.toString();
    }

    /** Calculate MD5 sum of a file */
    static final public String calcMD5(File file){
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");

            FileInputStream input = new FileInputStream(file);
            byte[] buf = new byte[1024];

            while (input.available() > 0) {
                int res = input.read(buf, 0, buf.length);

                md.update(buf, 0, res);
            }
            input.close();

            byte[] md5 = md.digest();

            return bytesToHexString(md5);
        }catch(Exception e){
            e.printStackTrace();
        }

        return ""+file.length();
    }

    /**
     * Convert an array of bytes to a string of hexadecimal numbers
     */
    static final private String bytesToHexString(byte[] array) {
        StringBuffer res = new StringBuffer();

        for (int i = 0; i < array.length; i++) {
            int val = array[i] + 256;
            String b = "00" + Integer.toHexString(val);
            int len = b.length();
            String sub = b.substring(len - 2);

            res.append(sub);
        }

        return res.toString();
    }

    private static String getMD5(byte[] source)
    {
        String md5 = null;
        char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest();

            char str[] = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++)
            {
                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            md5 = new String(str);

        } catch (Exception e)
        {
            e.printStackTrace();
            //logger.error("加密失败",e);
            return null;
        }
        return md5;
    }

    /**
     * md 5加密入口
     */
    public static String md5(String str) {
        return getMD5(str.getBytes());
    }
}