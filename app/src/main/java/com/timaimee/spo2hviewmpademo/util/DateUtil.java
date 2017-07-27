package com.timaimee.spo2hviewmpademo.util;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期操作工具类.
 *
 * @author shimiso http://blog.csdn.net/xuduzhoud/article/details/27526177
 */

public class DateUtil {

    private final static int TODAY = 0;
    private final static int YESTERDAY = -1;
    private final static int YESTERDAY_LAST = -2;
    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * yyyyMMddHHmmss
     *
     * @return
     */
    public static String getSystemTimeSimple() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
        return df.format(new Date());
    }


    public static String getToday() {
        String todayString = getoffetday(TODAY);
        return todayString;
    }

    public static String getYesterday() {
        String yesterdayString = getoffetday(YESTERDAY);
        return yesterdayString;
    }

    public static String getBeforeYesterday() {
        String beforYesString = getoffetday(YESTERDAY_LAST);
        return beforYesString;
    }


    /**
     * 获取当前日期的前一天 offset: -1 前一天 -2 前两天
     */
    public static String getoffetday(int offset) {
        Date date = new Date(); // 获取了当前日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, offset); // 在日历中找到他的前一天
        date = calendar.getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
        return df.format(date);
    }


    public static String getStringformDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    /**
     * DateUtil.getOffsetDate("2017-06-30", 1)  2017-07-01
     *
     * @param day
     * @param offset
     * @return
     */
    public static String getOffsetDate(String day, int offset) {

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, offset); // 在日历中找到他的前一天
        date = calendar.getTime();

        return format.format(date);

    }


    /***
     * 判断日期合法
     */
    public static boolean isDateVailt(String dateStr) {
        boolean isVailt = true;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
        Date time = null;
        try {
            sdf.setLenient(false);
            time = sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return isVailt;
    }

    /**
     * string -> date
     */
    public static Date str2Date(String str) {
        Date date = new Date();
        if (str == null || str.length() == 0) {
            return date;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            date = sdf.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;

    }

    /***
     * 得到星期几
     *
     * @param day
     * @return
     */
    public static int getDayofWeek(String day) {
        int position = 0;
        Date date = str2Date(day);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        position = calendar.get(Calendar.DAY_OF_WEEK);
        return position;
    }

    /**
     * 获取两个Date的相差多少天,day2-day1
     */
    public static int getDiffDaybetweenDate(String day1, String day2) {
        Date date1 = str2Date(day1);
        Date date2 = str2Date(day2);
        long diff = date2.getTime() - date1.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        return (int) days;
    }

    /**
     * 获取两个Date的相差多少周
     */
    public static int getDiffWeekbetweenDate(String day1, String day2) {
        int day = getDiffDaybetweenDate(day1, day2);
        return day / 7;

    }

    public static String getSpo2hTimeString(int time, boolean is24hour) {
        StringBuffer mBuffer = new StringBuffer();
        int hour = time / 60;
        int minute = time % 60;
        if (is24hour) {
            String hourStr = hour + "";
            if (hour < 10) {
                hourStr = "0" + hourStr;
            }
            String minuteStr = minute + "";
            if (minute < 10) {
                minuteStr = "0" + minuteStr;
            }
            mBuffer.append(hourStr + ":" + minuteStr);
        } else {
            String hourStr = "";
            if (hour == 0) {
                hourStr = 12 + "";
            } else {
                int i = hour % 12;
                if (i < 10) {
                    hourStr = "0" + i;
                }else{
                    hourStr = "" + i;
                }
            }
            String minuteStr = minute + "";
            if (minute < 10) {
                minuteStr = "0" + minuteStr;
            }
            String am = "am";
            if (hour > 12) {
                am = "pm";
            }
            mBuffer.append(hourStr + ":" + minuteStr + am);
        }
        return mBuffer.toString();
    }

}
