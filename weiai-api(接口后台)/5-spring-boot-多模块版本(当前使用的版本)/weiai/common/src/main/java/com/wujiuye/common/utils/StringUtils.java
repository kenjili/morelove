package com.wujiuye.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtils {

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean strIsNull(String str) {
        return str == null ? true : ("".equals(str.trim()) ? true : false);
    }

    /**
     * 判断字符串是否是邮箱
     *
     * @param string
     * @return
     */
    public static boolean isEmail(String string) {
        if (string == null)
            return false;
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p;
        Matcher m;
        p = Pattern.compile(regEx1);
        m = p.matcher(string);
        if (m.matches())
            return true;
        return false;
    }

    /**
     * 判断字符串是否是手机号码
     * @param phoneNumber
     * @return
     */
    public static boolean isPhoneNumber(String phoneNumber){
        if(phoneNumber==null)
            return false;
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        return phoneNumber.matches(regex);
    }


    /**
     * 日期字符串格式转Date
     * 如果出现异常返回系统当前时间的Date
     * @param datetime
     * @return
     */
    public static Date str2Date(String datetime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(datetime);
        } catch (ParseException e) {
            return new Date();
        }
    }

    /**
     * 日期Date转字符串格式
     * @param date
     * @return
     */
    public static String date2Str(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }


    /**
     * 必须是中文，且长度为2～8个中文字符
     * @param username
     * @return
     */
    public static boolean isUsername(String username){
        if(StringUtils.strIsNull(username))
            return false;
        if(username.length()<2&&username.length()>8)
            return false;
        return isChinese(username);
    }

    /**
     * 判断字符串是否复合密码要求
     * 即，不能是纯数字，也不能是纯字符，必须是数字与字母组合，且长度为6～18位
     * @param password
     * @return
     */
    public static boolean isPassword(String password){
        if(password==null)
            return false;
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$";
        return password.matches(regex);
    }


    /**
     * 判断字符串是否为中文
     * @param str
     * @return
     */
    public static boolean isChinese(String str){
        String regEx = "[\\u4e00-\\u9fa5]+";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }
}
