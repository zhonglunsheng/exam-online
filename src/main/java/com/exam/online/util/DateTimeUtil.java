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




    public static void main(String[] args) {
        System.out.println(DateTimeUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
        System.out.println(DateTimeUtil.strToDate("2010-01-01 11:11:11","yyyy-MM-dd HH:mm:ss"));

    }


}
