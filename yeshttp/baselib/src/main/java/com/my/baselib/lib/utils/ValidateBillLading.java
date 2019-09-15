package com.my.baselib.lib.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateBillLading {
    /**
     * 用户名校验
     */
    public static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,17}$";

    //身份证日期格式校验

    public static final String REGEX_DATE = "\\d{4}/([[0]\\d{1}]|[1][12])/(([0][1-9])|([12]\\d{1})|([3][01]))";
    //密码校验

    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$";
    //手机号校验

    //public static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0,1,5-9]))\\d{8}$";
    public static final String REGEX_MOBILE = "^(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$";
    // 邮箱校验

    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    //中文校验

    public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";
    //数字校验

    public static final String REGEX_NUMBER = "[0-9]{1,100}";
    // 身份证校验

    public static final String REGEX_ID_CARD = "(^\\d{15}$)|(^\\d{17}([0-9]|X)$)";
    // URL校验

    //   public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
    // IP校验

    public static final String REGEX_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";

    //中文校验

    public static final String REGEX_CHINESENAME = "^([\\u4e00-\\u9fa5]){1,15}$";

    // 正整数

    public static final String REGEX_NUM = "^\\d*[1-9]\\d*$";

    //固话

    public static final String REGEX_TEL = "([0-9]{3,4})?[0-9]{7,8}";
    // 邮编
    public static final String REGEX_ZIP_CODE = "^[0-9]\\d{5}$";

    //不能输入任意字符

    public static final String REGEX_STR = "^([\u4e00-\u9fa5a-zA-Z0-9]+)$";
    //校验16到19位的纯数字，比如：银行卡
    public static final String REGEX_BANK_STR = "^[0-9]\\d{15,18}$";
    //校验电话区号，3到4位数字
    public static final String REGEX_AREACODE = "^[0-9]\\d{2,3}$";
    public static void main(String[] args) {
        System.out.println(Pattern.matches(REGEX_DATE, "2016/11/11"));
    }

    // @return

    public static boolean isNotStr(String str) {
        return Pattern.matches(REGEX_STR, str);
    }
    // @return

    public static boolean isDate(String str) {
        return Pattern.matches(REGEX_DATE, str);
    }
    // @return

    public static boolean isStr(String str) {
        str = str.replaceAll("（", "").replaceAll("）", "").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("-", "");
        return Pattern.matches(REGEX_STR, str);
    }
    // @return

    public static boolean isStr2(String str) {
        str = str.replaceAll("\\·", "");
        return Pattern.matches(REGEX_STR, str);
    }

    // 只能输入中文，可以有·

    public static boolean isChinese(String str) {
        str = str.replaceAll("\\·", "");
        return Pattern.matches(REGEX_CHINESE, str);
    }

    //只能输入中文
    public static boolean isCN(String str){
        return Pattern.matches(REGEX_CHINESENAME, str);
    }

    // 手机或座机

    public static boolean isTelOrPhone(String tel) {
        if (Pattern.matches(REGEX_TEL, tel) || Pattern.matches(REGEX_MOBILE, tel)) {
            return true;
        }
        return false;
    }


    public static boolean isZipCode(String str) {
        return Pattern.matches(REGEX_ZIP_CODE, str);
    }

    public static boolean isTel(String tel) {
        return Pattern.matches(REGEX_TEL, tel);
    }

    public static boolean isNumber(String number) {
        return Pattern.matches(REGEX_NUM, number);
    }

    public static boolean isUsername(String username) {
        return Pattern.matches(REGEX_USERNAME, username);
    }

    public static boolean isPassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }

    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }

    public static boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }

    public static boolean isNum(String num) {
        return Pattern.matches(REGEX_NUMBER, num);
    }

    public static boolean isIDCard(String idCard) {
        return Pattern.matches(REGEX_ID_CARD, idCard);
    }

	 /*  public static boolean isURL(String url){
		   return Pattern.matches(REGEX_URL, url);
	   }*/

    public static boolean isIPAddr(String ipAddr) {
        return Pattern.matches(REGEX_IP_ADDR, ipAddr);
    }

    public static boolean isChineseName(String name) {
        name = name.replaceAll("\\·", "");
        return Pattern.matches(REGEX_CHINESENAME, name);
    }

    //校验银行卡号
    public static boolean isBankNum(String name) {
        return Pattern.matches(REGEX_BANK_STR, name);
    }

    //校验是否是区号
    public static boolean isAreaCode(String areaCode){
        return  Pattern.matches(REGEX_AREACODE,areaCode);
    }

    public static boolean checkName(String userName) {
        String regex = "([a-z]|[A-Z]|[0-9]|[\\u4e00-\\u9fa5])+_";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(userName);
        return m.matches();
    }

}
