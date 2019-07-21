package com.example.zuer02.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class DateUtil {

    public final static String FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";

    public final static String FORMAT_STRING_YMD = "yyyy-MM-dd";

    public final static String[] REPLACE_STRING = new String[]{"GMT+0800", "GMT+08:00"};

    public final static String SPLIT_STRING = "(中国标准时间)";

    public static Date gmtToDate(String dateString) throws RuntimeException{
        try {
            if(dateString!=null&&!dateString.equals("")){
                dateString = dateString.split(Pattern.quote(SPLIT_STRING))[0].replace(REPLACE_STRING[0], REPLACE_STRING[1]);
                SimpleDateFormat sf1 = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss z", Locale.US);
                Date date = sf1.parse(dateString);
                return date;
            }else{
                return null;
            }

        } catch (Exception e) {
            throw new RuntimeException("时间转化格式错误" + "[dateString=" + dateString + "]" + "[FORMAT_STRING=" + FORMAT_STRING + "]");
        }
    }
    //yyyy-MM-dd
    public static String gmtToStringYMD(String dateString){
        Date date=gmtToDate(dateString);
        if(date!=null){
            SimpleDateFormat sf1=new SimpleDateFormat(FORMAT_STRING_YMD);
            String strDate=sf1.format(date);
            return strDate;
        }else{
            return "";
        }

    }
    //yyyy-MM-dd
    public static Date StringYMDToDate(String dateString) throws RuntimeException{

        if(dateString==null){
            return null;
        }
        SimpleDateFormat sf1=new SimpleDateFormat(FORMAT_STRING_YMD);
        try {

            return sf1.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("日期转换异常");
        }
    }

    public static  String  getYear(Date date){

        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
        int year=cal.get(Calendar.YEAR);
        return  year+"";
    }

    public static void main(String[] args) {
        System.out.println(gmtToStringYMD("Tue Jul 01 2019 13:12:00 GMT+0800 (中国标准时间)"));
    }
}
