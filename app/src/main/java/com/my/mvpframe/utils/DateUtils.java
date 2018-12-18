package com.my.mvpframe.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Create by jzhan on 2018/12/18
 * 各种时间转换
 */
public class DateUtils {


    /**
     * 获取当前时间
     */
    public static long getCurrentTimeLong() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentTimeString() {
        return getCurrentTimeString(null);
    }

    public static String getCurrentTimeString(String formatType) {
        return new SimpleDateFormat(getFormatType(formatType), Locale.CHINA).format(new Date());
    }

    /*获取时间格式*/
    private static String getFormatType(String formatType) {
        String default_formatType = "yyyy-MM-dd HH:mm:ss";
        return TextUtils.isEmpty(formatType) ? default_formatType : formatType;
    }

    /**
     * 获取当前时间
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static Date getCurrentTimeDate() {
        return getCurrentTimeDate(null);
    }

    public static Date getCurrentTimeDate(String formatType) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(getFormatType(formatType), Locale.CHINA);
        String dateString = formatter.format(new Date());
        ParsePosition pos = new ParsePosition(8);
        return formatter.parse(dateString, pos);
    }

    /**
     * long 转换 String
     */
    public static String longToString(long time) {
        return longToString(time, null);
    }

    /**
     * long 转换 String
     */
    public static String longToString(long time, String formatType) {
        return new SimpleDateFormat(getFormatType(formatType), Locale.CHINA).format(time);
    }

    /**
     * long 转换 Date
     */
    public static Date longToDate(long time) {
        Date dateOld = new Date(time); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime); // 把String类型转换为Date类型
        return date;
    }

    /**
     * String 转换 long
     */
    public static long stringToLong(String time) {
        Date date = stringToDate(time);
        return dateToLong(date);
    }

    /**
     * String 转换 date
     */
    public static Date stringToDate(String time) {
        return stringToDate(time, null);
    }

    /**
     * String 转换 date
     */
    public static Date stringToDate(String time, String formatType) {
        SimpleDateFormat formatter = new SimpleDateFormat(getFormatType(formatType), Locale.CHINA);
        Date date = null;
        try {
            date = formatter.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * Date 转换 long
     */
    public static long dateToLong(Date date) {
        return date == null ? 0 : date.getTime();
    }

    /**
     * Date 转换 String
     */
    public static String dateToString(Date date, String formatType) {
        return new SimpleDateFormat(getFormatType(formatType), Locale.CHINA).format(date);
    }

    /**
     * Date 转换 String
     */
    public static String dateToString(Date date) {
        return dateToString(date, null);
    }

}
