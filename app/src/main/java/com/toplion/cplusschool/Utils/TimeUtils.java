package com.toplion.cplusschool.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.util.Log;

/**
 * 时间类
 *
 * @author liyb
 */
public class TimeUtils {
    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds   精确到秒的字符串
     * @param formatStr
     * @return
     */
    public static String timeStampToDate(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.parseLong(seconds) * 1000));
    }

    /**
     * 直接转换为时间格式
     *
     * @param seconds
     * @return
     */
    public static Date secondsToDate(String seconds) {
        return new Date(Long.parseLong(seconds) * 1000);
    }

    /**
     * 获取当前月月份
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public static int getMonth() {
        return new Date().getMonth();
    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds   精确到秒的字符串
     * @param formatStr
     * @return
     */
    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date(Long.parseLong(seconds) * 1000L));
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param date   字符串日期
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String date2TimeStamp(String date_str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date_str).getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @return
     */
    public static String getMealTLength(String startTime, String buy) {
        long timeStamp = 1454909484;
        //String date = timeStamp2Date(timeStamp+"", "yyyy.MM.dd");
        timeStamp = Long.parseLong(date2TimeStamp(startTime, "yyyy.MM.dd"));
        String date = timeStamp2Date(timeStamp + "", "yyyy.MM.dd");
        System.out.println(timeStamp);
        int month = Integer.parseInt(timeStamp2Date(timeStamp + "", "MM"));
        int count = 0;
        if (buy.equals("0")) {
            count = 1;
        } else if (buy.equals("b2")) {
            count = 2;
        } else if (buy.equals("b3")) {
            count = 3;
        } else if (buy.equals("bt")) {
            count = 6;
        }
        if (!"bt".equals(buy)) {
            //month = 4;
            String endDate = "";
            for (int i = 0; i < count; i++) {

                switch (month) {
                    case 1:
                    case 3:
                    case 5:
                    case 7:
                    case 8:
                    case 10:
                    case 12:
                        // 31天
                        timeStamp += 31 * 24 * 60 * 60;
                        //endDate = timeStamp2Date(timeStamp+"", "yyyy.MM.dd");
                        break;
                    case 4:
                    case 6:
                    case 9:
                    case 11:
                        // 30天
                        timeStamp += 30 * 24 * 60 * 60;
                        //endDate = timeStamp2Date(timeStamp+"", "yyyy.MM.dd");
                        break;
                    case 2:
                        // 29天
                        timeStamp += 29 * 24 * 60 * 60;
                        //endDate = timeStamp2Date(timeStamp+"", "yyyy.MM.dd");
                        break;
                }
                month++;
                if (month > 12) {
                    month = 1;
                }
            }
            //最后时间减一
            timeStamp = timeStamp - 24 * 60 * 60;
            endDate = timeStamp2Date(timeStamp + "", "yyyy.MM.dd");
            return date + " - " + endDate;
        } else {
            if (month <= 7 && month > 2) {
                return date + " - " + date.substring(0, 4) + ".7.31";
            } else {
                Log.d("TimeUtils======", date + "....." + date.substring(0, 4));
                return date + " - " + (Integer.parseInt(date.substring(0, 4)) + 1) + ".1.31";
            }
        }
    }

    // 计算时间
    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 59) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    /**
     * 获取两个日期之间的天数
     *
     * @param before
     * @param after
     * @return
     */
    public static double getDistanceOfTwoDate(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
    }

    /**
     * 由出生日期获得年龄
      * @param birthDayStr
     * @return
     * @throws Exception
     */
    public static int getAge(String birthDayStr) throws Exception {

        Date birthDay = stringToDate(birthDayStr,"yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) {
            throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            }else{
                age--;
            }
        }
        return age;
    }
    private final static int[] dayArr = new int[] { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };
    private final static String[] constellationArr = new String[] {  "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座"};

    /**
     * 根据日期获取星座
     * @return
     */
    public static String getConstellation(String birthDayStr) throws Exception {
        if (birthDayStr == null) {
            return "";
        }
        Date birthDay = stringToDate(birthDayStr,"yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(birthDay);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        if (day < dayArr[month]) {
            month = month - 1;
        }
        if (month >= 0) {
            return constellationArr[month];
        }
        // default to return 魔羯
        return constellationArr[11];
    }
    /**
     * 将字符串型日期转换成日期
     *
     * @param dateStr    字符串型日期
     * @param dateFormat 日期格式
     * @return
     */
    public static Date stringToDate(String dateStr, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        try {
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }
}
