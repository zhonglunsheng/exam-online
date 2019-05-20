package com.exam.online.util;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * @author zhonglunsheng
 * @Description 时间转换工具
 * @create 2019-02-18 16:43
 */
public class DateTimeUtil {

    /**
     * 转换格式默认 yyyy-MM-dd HH:mm:ss
     */
    public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";



    public static Date strToDate(String dateTimeStr,String formatStr){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatStr);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    public static String dateToStr(Date date,String formatStr){
        if(date == null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(formatStr);
    }

    public static Date strToDate(String dateTimeStr){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    public static String dateToStr(Date date){
        if(date == null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD_FORMAT);
    }

    public static String getTimeDifference(String from, String to){
        Date fromTime = strToDate(from);
        Date toTime = strToDate(to);
        Long timeTotal = toTime.getTime() - fromTime.getTime();
        int hour = (int) (timeTotal/(1000 * 60 * 60));
        int minute = (int) (timeTotal - hour * 60 * 60 * 1000) / (1000 * 60);
        return String.format("%d:%d", hour,minute);
    }

    public static Boolean whoIsBig(String source, String target){
        Date sourceTime = strToDate(source);
        Date targetTime = strToDate(target);
        return sourceTime.getTime() - targetTime.getTime() > 0;
    }



    public static void main(String[] args) {
//        System.out.println(DateTimeUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
//        System.out.println(DateTimeUtil.strToDate("2010-01-01 11:11:11","yyyy-MM-dd HH:mm:ss"));

        System.out.println(getTimeDifference("2019-10-01 12:00:00", "2019-10-01 14:00:00"));

    }


}
