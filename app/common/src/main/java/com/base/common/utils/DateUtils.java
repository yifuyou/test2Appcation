package com.base.common.utils;

import android.text.TextUtils;
import android.text.format.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;



public class DateUtils {

    public static final String simpleFormat = "yyyy-MM-dd";
    public static final String defaultFormat = "yyyy-MM-dd HH:mm:ss";


    /**
     *
     * 日期 距离今天的天数
     * @param date   日期字符串
     * @return  0今天   >0 明天以后   <0  昨天以前
     */
    public static int countDays(String date) {
        long t = Calendar.getInstance().getTime().getTime();
        long t1 = dateStringToLong(date);
        return (int) (t / 1000 - t1 / 1000) / 3600 / 24;
    }



    //获取周几，或者星期几  比如：周一，星期二
    public static String getWeekDay(String date, String format, String back) {
        int in = getWeekDay(dateStringToLong(date, format));
        if (in == 1) return back + "日";
        else return back + JavaMethod.intSimpleParse(in - 1);
    }

    public static String getWeekDay(String date, String back) {
        int in = getWeekDay(dateStringToLong(date));
        if (in == 1) return back + "日";
        else return back + JavaMethod.intSimpleParse(in - 1);
    }

    //获取一个星期的第几天，星期天是第1天，为1
    public static int getWeekDay(long date) {
        if (date == 0) date = System.currentTimeMillis();
        Calendar ca = Calendar.getInstance();
        ca.setTimeInMillis(date);
        return ca.get(Calendar.DAY_OF_WEEK);
    }

    public static int getYear(long date) {
        if (date == 0) date = System.currentTimeMillis();
        Calendar ca = Calendar.getInstance();
        ca.setTimeInMillis(date);
        return ca.get(Calendar.YEAR);
    }

    public static int getMonth(long date) {
        if (date == 0) date = System.currentTimeMillis();
        Calendar ca = Calendar.getInstance();
        ca.setTimeInMillis(date);
        return ca.get(Calendar.MONTH);
    }

    public static int getDay(long date) {
        if (date == 0) date = System.currentTimeMillis();
        Calendar ca = Calendar.getInstance();
        ca.setTimeInMillis(date);
        return ca.get(Calendar.DAY_OF_MONTH);
    }

    //获取几月，从0开始
    public static int getMonth(String date, String format) {
        return getMonth(dateStringToLong(date, format));
    }

    public static int getNowDay() {
        Calendar ca = Calendar.getInstance();
        ca.setTimeInMillis(System.currentTimeMillis());
        return ca.get(Calendar.DAY_OF_MONTH);
    }

    public static int getNowYear() {
        return getYear(0);
    }


    public static int getNowMonth() {
        return getMonth(getNowDateString());
    }

    //获取几月，从0开始
    public static int getMonth(String date) {
        return getMonth(dateStringToLong(date));
    }


    /**
     * 格式化Date为String
     */
    public static String parseDate(String format, Date... date) {
        Date time;
        if (date.length == 0) time = new Date();
        else time = date[0];
        return getSimpleDateFormat(format).format(time);
    }

    public static Date parseDates(String date, String... format) {
        SimpleDateFormat sf;
        if (format.length == 0) sf = concludeDateFormat(date);
        else sf = getSimpleDateFormat(format[0]);
        return new Date(dateStringToLong(date, sf));
    }

    public static String parseCalendar(String format, Calendar calendar) {
        if (calendar == null) calendar = Calendar.getInstance();
        return getSimpleDateFormat(format).format(calendar.getTime());
    }

    public static Calendar parseCalendar(String dateStr, String... format) {
        long date = dateStringToLong(dateStr, format.length > 0 ? format[0] : concludeDateFormatString(dateStr));
        Calendar ca = Calendar.getInstance();
        ca.setTimeInMillis(date);
        return ca;
    }


    /**
     * 格式化Date为String
     *
     * @param format 返回时间的格式
     * @param date   需要格式化的时间，如果不填写则为当前时间
     * @return 返回格式化的字符串
     */
    public static String parseTime(String format, long... date) {
        long time;
        if (date.length == 0) time = System.currentTimeMillis();
        else time = date[0];
        return getSimpleDateFormat(format).format(new Date(time));
    }

    public static String parseTime(String format, String date) {
        return parseTime(format, date, defaultFormat);
    }

    public static String parseTime(String format, String date, String dateFormat) {
        if (UIUtils.isEmpty(date)) return "";
        try {
            return DateUtils.getSimpleDateFormat(format).
                    format(DateUtils.getSimpleDateFormat(dateFormat).parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String getDayNext(long time, String format) {
        return getDay(format, 1, time);
    }

    public static String getHourNext(long time, String format) {
        return getHour(time, format, 1);
    }


    public static String getHourNext(String time, String format) {
        return getHour(dateStringToLong(time, format), format, 1);
    }

    /**
     * 获取下一年的毫秒数
     *
     * @param time
     * @param num  1下一年  -1 去年
     * @return
     */
    public static long getYearsNext(long time, int num) {
        if (time == 0) time = System.currentTimeMillis();
        Calendar ca = Calendar.getInstance();
        ca.setTimeInMillis(time);
        ca.add(Calendar.YEAR, num);
        return ca.getTimeInMillis();
    }

    /**
     * 获取@minute 分钟后的时间
     *
     * @time 基准时间
     * 返回时间为长整型
     */
    public static long getMinute(long time, int minute) {
        if (time == 0) time = System.currentTimeMillis();
        Calendar ca = Calendar.getInstance();
        ca.setTimeInMillis(time);
        ca.add(Calendar.MINUTE, minute);
        return ca.getTimeInMillis();
    }

    /**
     * 获取@minute 分钟后的时间
     *
     * @time 基准时间
     * @format 返回时间的格式
     */
    public static String getMinute(long time, String format, int minute) {
        return getSimpleDateFormat(format).format(getMinute(time, minute));
    }

    public static long getHour(long time, int num) {
        if (time == 0) time = System.currentTimeMillis();
        Calendar ca = Calendar.getInstance();
        ca.setTimeInMillis(time);
        ca.add(Calendar.HOUR, num);
        return ca.getTimeInMillis();
    }

    public static String getHour(long time, String format, int num) {
        return getSimpleDateFormat(format).format(getHour(time, num));
    }

    public static String getMonth(String format, int num, long... date) {
        long time;
        if (date.length == 0) time = System.currentTimeMillis();
        else time = date[0];

        Calendar ca = Calendar.getInstance();
        ca.setTimeInMillis(time);
        ca.add(Calendar.MONTH, num);
        return getSimpleDateFormat(format).format(ca.getTime());
    }


    public static String getDay(String format, int num, long... date) {
        long time;
        if (date.length == 0) time = System.currentTimeMillis();
        else time = date[0];

        Calendar ca = Calendar.getInstance();
        ca.setTimeInMillis(time);
        ca.add(Calendar.DATE, num);
        return getSimpleDateFormat(format).format(ca.getTime());
    }

    public static Long getDayLong(int num, long... date) {
        long time;
        if (date.length == 0) time = System.currentTimeMillis();
        else time = date[0];
        Calendar ca = Calendar.getInstance();
        ca.setTimeInMillis(time);
        ca.add(Calendar.DATE, num);
        return ca.getTimeInMillis();
    }


    /**
     * 返回时间
     *
     * @return
     */
    public static String getStringTime(long surplusTime) {
        long time = surplusTime / 1000;
        long sTime = time / 60;
        long ss = time % 60;
        long mm = sTime % 60;
        long hh = sTime / 60;
        String showSs = ss > 9 ? String.valueOf(ss) : "0" + ss;
        String showMm = mm > 9 ? String.valueOf(mm) : "0" + mm;
        String showHh = hh > 9 ? String.valueOf(hh) : "0" + hh;
        return showHh + ":" + showMm + ":" + showSs;
    }

    //获取相差天数
    public static int getSubtractDay(String startDate, String endDate, String format, boolean isTrueDay) {
        long startT = dateStringToLong(startDate, format); // 定义开始时间
        long endT = dateStringToLong(endDate, format); // 定义结束时间
        return getSubtractDay(startT, endT, isTrueDay);
    }

    /**
     * 1,如果结束时间大于开始时间
     * 计算的时间差天数  2020-04-27 23:52:00       2020-04-28 03:00:00  这两个时间相差不足24个小时是0天
     * 如果要相差为一天的话  结束时间设为 2020-04-28 23:59:59
     * <p>
     * 2，如果开始时间大于结束时间
     * 则要将开始时间设为今天的00：00：00
     *
     * @param startDate_long
     * @param endDate_long
     * @param isTrueDay      //是否是真实的时间差
     * @return
     */
    public static int getSubtractDay(long startDate_long, long endDate_long, boolean isTrueDay) {
        if (endDate_long == 0) endDate_long = System.currentTimeMillis();
        if (startDate_long == 0) startDate_long = System.currentTimeMillis();
        if (isTrueDay) {
            if (endDate_long > startDate_long) {
                String date = dateLongToString(endDate_long, simpleFormat) + "23:59:59";
                endDate_long = dateStringToLong(date);
            } else {
                String date = dateLongToString(startDate_long, simpleFormat) + "00:00:00";
                startDate_long = dateStringToLong(date);
            }
        }
        Calendar ca = Calendar.getInstance();
        ca.setTimeInMillis(endDate_long);
        long ss = (endDate_long - startDate_long) / (1000 * 60 * 60 * 24);
        return (int) ss;
    }

    public static long getSubtractTimeInMillis(long start, long end) {
        return end - start;
    }

    public static long getSubtractTimeInMillis(String start, String end, String format) {
        return getSubtractTimeInMillis(dateStringToLong(start, format), dateStringToLong(end, format));
    }

    //日期字符,转换成long
    public static long dateStringToLong(String dateString, String format) {
        return dateStringToLong(dateString, getSimpleDateFormat(format));
    }

    /**
     * dateString  可以是常用的日期类型
     *
     * @param dateString
     * @return
     */
    public static long dateStringToLong(String dateString) {
        return dateStringToLong(dateString, concludeDateFormat(dateString));
    }

    public static long dateStringToLong(String dateString, SimpleDateFormat format) {
        if (dateString == null) dateString = "";
        dateString = dateString.replaceAll("\\s+", " ");
        Date date = null; // 定义时间类型
        try {
            date = format.parse(dateString); // 将字符型转换成日期型
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return date == null ? 0 : date.getTime(); // 返回毫秒数
    }


    public static String dateStringToString(String date, String format, String toFormat) {
        return dateLongToString(dateStringToLong(date, format), toFormat);
    }

    public static String dateStringToString(String date, String toFormat) {
        String format = concludeDateFormatString(date);
        return dateLongToString(dateStringToLong(date, format), toFormat);
    }


    public static String getNowDateString(String format) {
        return dateLongToString(0, getSimpleDateFormat(format));
    }

    public static String getNowDateString() {
        return dateLongToString(0, getSimpleDateFormat(""));
    }

    public static String getNowDateString_simple() {
        return dateLongToString(0, getSimpleDateFormat(simpleFormat));
    }

    //long型日期，转换字符型日期
    public static String dateLongToString(long date_long, String format) {
        return dateLongToString(date_long, getSimpleDateFormat(format));
    }

    public static String dateLongToString(long date_long) {
        return dateLongToString(date_long, concludeDateFormat());
    }

    public static String dateLongToString(long date_long, SimpleDateFormat format) {
        return format.format(date_long == 0 ? new Date() : new Date(date_long));
    }

    //获取SimpleDateFormat
    public static SimpleDateFormat getSimpleDateFormat(String format) {
        if (TextUtils.isEmpty(format)) format = defaultFormat;
        SimpleDateFormat simpleFormat = new SimpleDateFormat(format, Locale.CHINA);
        simpleFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        return simpleFormat;
    }

    /**
     * 日期中必须包含 有是年开头，否则为null
     *
     * @param dateString
     * @return
     */
    public static SimpleDateFormat concludeDateFormat(String... dateString) {
        return getSimpleDateFormat(concludeDateFormatString(dateString));
    }

    public static String concludeDateFormatString(String... dateString) {
        String format = "";
        String date = dateString.length == 0 ? "" : dateString[0];
        if (date == null) date = "";
        //多个空格合并
        date = date.replaceAll("\\s+", " ");

//        if (!"".equals(date)) {
        if (date.length() > 0) {
            if (date.contains("年")) {
                format = "yyyy年";
            }

            if (date.contains("月")) {
                format += "MM月";
                if (!format.contains("年")) return null;
            }

            if (date.contains("日")) {
                format += "dd日";
            } else if (format.length() > 0) {
                if (date.length() == 10) format += "dd";
                else if (date.length() > 10) format += "dd ";
            }

            String spec = "";
            //1970-01-01 15:30:00
            if (date.contains("-")) {
                spec = "-";
            }
            //1970/01/01 15:30:00
            else if (date.contains("/")) {
                spec = "/";
            }
            //1970.01.01 15:30:00
            else if (date.contains(".")) {
                spec = ".";
            }

            if (!"".equals(spec)) {
                int start = date.indexOf(spec);
                int end = date.lastIndexOf(spec);

                //只有一个分割符  1970-11  或11-14
                if (end - start == 0) {
                    //1970-11
                    if (start == 4) {
                        format = "yyyy" + spec + "MM";
                    }
                    //11-14
                    else if (start == 2) {
                        format = "MM" + spec + "dd";
                    }
                } else {
                    format = "yyyy" + spec + "MM" + spec + "dd";
                }
            }
            //format 和 spec同时为空 19901114
            else if ("".equals(format)) {
                format = "yyyyMMdd";
            }


            //时间判断
            if (date.contains(":")) {
                int start = date.indexOf(":");
                int end = date.lastIndexOf(":");
                int count = end - start;


                switch (count) {
                    case 0://13:58
                        format += "HH:mm";
                        break;
                    case 3://13:58:23
                        format += "HH:mm:ss";
                        break;
                    case 6://13:58:23:978
                        format += "HH:mm:ss:SSS";
                        break;
                }
            }
        } else format = defaultFormat;

        return format;
    }


    /**
     * 比赛详情头部时间
     *
     * @param date
     * @return
     */
    public static String formatMatchDetailTime(long date, boolean isLiving) {
        if (isLiving) {
            return getSimpleDateFormat("yyyy-MM-dd 直播中").format(new Date(date));
        } else {
            if (isSameDay(date, System.currentTimeMillis())) {
                //今天
                return getSimpleDateFormat("yyyy-MM-dd 今天 HH:mm").format(new Date(date));
            } else if (isYesterday(date)) {
                //昨天
                return getSimpleDateFormat("yyyy-MM-dd 昨天 HH:mm").format(new Date(date));
            } else if (isTomorrow(date)) {
                //明天
                return getSimpleDateFormat("yyyy-MM-dd 明天 HH:mm").format(new Date(date));
            } else {
                //其他显示日期加时间
                return getSimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(date));
            }
        }
    }

    /**
     * 是否在同一天
     *
     * @param time1 时间(毫秒)
     * @param time2 时间(毫秒)
     * @return
     */
    public static boolean isSameDay(long time1, long time2) {
        Time time = new Time();
        time.set(time1);

        int timeYear = time.year;
        int timeMonth = time.month;
        int timeMonthDay = time.monthDay;

        time.set(time2);

        return (timeYear == time.year)
                && (timeMonth == time.month)
                && (timeMonthDay == time.monthDay);
    }

    /**
     * 是否在同一月
     *
     * @param time1 时间(毫秒)
     * @param time2 时间(毫秒)
     * @return
     */
    public static boolean isSameMonth(long time1, long time2) {
        Time time = new Time();
        time.set(time1);

        int timeYear = time.year;
        int timeMonth = time.month;

        time.set(time2);

        return (timeYear == time.year)
                && (timeMonth == time.month);
    }

    /**
     * 是否同一年
     *
     * @param time1 时间(毫秒)
     * @param time2 时间(毫秒)
     * @return
     */
    public static boolean isSameYear(long time1, long time2) {
        Time time = new Time();
        time.set(time1);

        int timeYear = time.year;

        time.set(time2);

        return (timeYear == time.year);
    }

    /**
     * 是否是昨天
     *
     * @param when 时间(毫秒)
     * @return
     */
    public static boolean isYesterday(long when) {
        Time time = new Time();
        time.set(when);

        int thenYear = time.year;
        int thenMonth = time.month;
        int thenMonthDay = time.monthDay;

        time.set(System.currentTimeMillis() - (1000 * 3600 * 24));

        return (thenYear == time.year)
                && (thenMonth == time.month)
                && (thenMonthDay == time.monthDay);
    }

    /**
     * 是否是明天
     *
     * @param when 时间(毫秒)
     * @return
     */
    public static boolean isTomorrow(long when) {
        Time time = new Time();
        time.set(when);

        int thenYear = time.year;
        int thenMonth = time.month;
        int thenMonthDay = time.monthDay;

        time.set(System.currentTimeMillis() + (1000 * 3600 * 24));

        return (thenYear == time.year)
                && (thenMonth == time.month)
                && (thenMonthDay == time.monthDay);
    }

    /**
     * 计算比赛时间
     *
     * @return
     */
    public static String getMatchTime(int state, long startBallTime) {
        int matchTime;
        long newTime = System.currentTimeMillis() / 1000;
        if (state > 1 && state <= 3) {
            matchTime = (int) ((newTime - startBallTime) / 60 + 1);
            if (matchTime > 45) {
                return "45+";
            } else {
                return matchTime + "";
            }
        } else if (state > 3 && state <= 8) {
            matchTime = (int) ((newTime - startBallTime) / 60 + 1 + 45);
            if (matchTime > 90) {
                return "90+";
            } else {
                return matchTime + "";
            }
        }
        return "0";
    }

    public static String getMatchSecondTime(String matchSecond, long time) {
        if (UIUtils.isNotEmpty(matchSecond)) {
            int matchTime;
            long newTime = System.currentTimeMillis();
            matchTime = (int) ((newTime - time)) / 1000;
            int miao = Integer.valueOf(matchSecond);
            miao = miao - matchTime;
            if (miao <= 0) {
                return " 完";
            }
            int MM = miao / 60;
            int ss = miao % 60;
            return " " + MM + ":" + ss;
        }
        return "";
    }

}
