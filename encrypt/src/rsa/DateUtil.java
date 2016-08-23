package rsa;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * 日期工具
 * @author dinglei
 *
 */
public class DateUtil 
{
	
	public static final String F_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
	
	public static final String F_DATE8 = "yyyyMMdd";
	
	public static final String F_DATE10 = "yyyy-MM-dd";
	
	public static final String F_YEAR_MONTH = "yyyyMM";
	
	public static final String F_YEAR = "yyyy";
	
	public static final String F_MONTH = "MM";
	
	public static final String F_DAY = "dd";
	
	public static final String F_TIME8 = "HH:mm:ss"; 
	
	public static final String F_HOUR = "HH";
	
	public static final String F_MINITE = "mm";
	
	public static final String F_SECOND = "ss";
	
	/**
	 * 当前日期yyyy-MM-dd
	 * @return
	 */
	public static String getNowDate()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");    
		return sdf.format(new Date());
	}
	
	/**
	 * 获取指定的日期yyyyMMdd
	 * @return
	 */
	public static String getAppointedDate(String dateInput)
	{
		DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			return sdf.format(sdf.parse(dateInput));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 当前日期+时间yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getNowDateTime()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    
		return sdf.format(new Date());
	}
	
	/**
	 * 当前年yyyy
	 * @return
	 */
	public static String getNowYear()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");    
		return sdf.format(new Date());
	}
	
	/**
	 * 当前月份MM
	 * @return
	 */
	public static String getNowMonth()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("MM");    
		return sdf.format(new Date());
	}
	
	/**
	 * 当前天数dd
	 * @return
	 */
	public static String getNowDay()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd");    
		return sdf.format(new Date());
	}
	/**
	 * 当前日期  F_DATE8
	 * @return
	 */
	public static String getF_TIME8()
	{
		SimpleDateFormat sdf = new SimpleDateFormat(F_TIME8);    
		return sdf.format(new Date());
	}
	
	/**
	 * 当前时间  F_DATE8
	 * @return
	 */
	public static String getNowDate8()
	{
		SimpleDateFormat sdf = new SimpleDateFormat(F_DATE8);    
		return sdf.format(new Date());
	}
	
	/**
	 *  当前时间  F_DATE10
	 * @return
	 */
	public static String getNowDate10()
	{
		SimpleDateFormat sdf = new SimpleDateFormat(F_DATE10);    
		return sdf.format(new Date());
	}
	
	/**
	 * 当前年月yyyy-MM
	 * @return
	 */
	public static String getNowYearMonth()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");    
		return sdf.format(new Date());
	}
	
	/**
	 * 当前年月yyyyMM
	 * @return
	 */
	public static String getNowYearMonth1()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");    
		return sdf.format(new Date());
	}
	
	/**
	 * 当前年月yyyyMM的前N个月
	 * @return
	 */
	public static String getPreNowYearMonth(int n)
	{
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String time = sdf.format(date);
		String[] item = time.split("-");
		int year  = Integer.parseInt(item[0]);
		int month = Integer.parseInt(item[1]);
		int day   = Integer.parseInt(item[2]);
		if((month - n) <= 0){
			month = month + 12 - n;
			year = year -1;
		}else {
			month = month - n;
		}
		if(month>0&&month<10){
			time = year + "0" + month;
		}
		if(month>9&&month<12){
			time = year + month+"";
		}
		return time;
	}
	
	/**
	 * 根据格式显示时间串
	 * @param format
	 * @return
	 */
	public static String getNow(String format)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(format);    
		return sdf.format(new Date());
	}
	
	/**
	 * 根据微信时间转换时间字符串yyyy-MM-dd HH:mm:ss
	 * @param dateime
	 * @return
	 */
	public static String getDateTime(String wechatDateime)
	{
		long msgCreateTime = Long.parseLong(wechatDateime) * 1000L;  
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		return format.format(new Date(msgCreateTime));  
	}
	
	/**
	 * 获取用于微信回复信息时间
	 * @return
	 */
	public static long getWechatDateTime()
	{
		return new Date().getTime()/1000L;
	}
	/*
	 * 比较2个时间的大小 hh:mm:ss
	 */
	public static boolean compare_date(String date1, String date2){           
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        try {
               java.util.Date d1 = df.parse(date1);
               java.util.Date d2 = df.parse(date2);
               if (d1.getTime() >= d2.getTime()){
                        return true;
               }else{
            	   return false;
               }
        } catch (Exception exception) {
                exception.printStackTrace();
        }
        return false;
	  }
	
	/*
	 * 比较2个时间的大小 yyyy-mm-dd
	 */
	public static boolean compare_date2(String date1, String date2){           
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
               java.util.Date d1 = df.parse(date1);
               java.util.Date d2 = df.parse(date2);
               if (d1.getTime() >= d2.getTime()){
                   return true;
               }else{
            	   return false;
               }
        } catch (Exception exception) {
                exception.printStackTrace();
        }
        return false;
	}
	
	/*
	 * 比较2个时间的大小 yyyyMMdd
	 */
	public static boolean compare_date3(String date1, String date2){           
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
        	   if(date1.equals("") || date2.equals("")){
        		   return false;
        	   }
               java.util.Date d1 = df.parse(date1);
               java.util.Date d2 = df.parse(date2);
               if (d1.getTime() >= d2.getTime()){
                   return true;
               }else{
            	   return false;
               }
        } catch (Exception exception) {
                exception.printStackTrace();
        }
        return false;
	}
	
	/**
	 * 比较日期时间
	 * @param dateTime1
	 * @param dateTime2
	 * @return true dateTime1大于登入dateTime2
	 */
	public static boolean compareDateTime(String dateTime1, String dateTime2) {
		 DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
    	   if(StringUtils.isBlank(dateTime1) || StringUtils.isBlank(dateTime2)){
    		   return false;
    	   }
           java.util.Date d1 = df.parse(dateTime1);
           java.util.Date d2 = df.parse(dateTime2);
           if (d1.getTime() >= d2.getTime()){
               return true;
           }else{
        	   return false;
           }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
	}
	
	/**
	 * 获取与当天间隔了指定年数的日期。
	 * @param years 间隔年数。正整数代表向前，负整数代表向后。
	 * @return 时间。默认格式：yyyy-MM-dd。
	 */
	public static String getPreOrNextYear(int years, String dateFormat) {
		dateFormat = dateFormat==null?"yyyy-MM-dd":dateFormat;
		DateFormat format = new SimpleDateFormat(dateFormat);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.YEAR, years);
		return format.format(calendar.getTime());
	}
	/*
	 * 获取与当天间隔了指定年数的日期。
	 */
	public static String getPreOrNextYear(int years) {
		return getPreOrNextYear(years,null);
	}
	
	/**
	 * 获取与当天间隔了指定月份的日期。
	 * @param months 间隔月份。正整数代表向前，负整数代表向后。
	 * @return 时间。默认格式：yyyy-MM-dd。
	 */
	public static String getPreOrNextMonth(int months, String dateFormat) {
		dateFormat = dateFormat==null?"yyyy-MM-dd":dateFormat;
		DateFormat format = new SimpleDateFormat(dateFormat);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MONTH, months);
		return format.format(calendar.getTime());
	}
	/*
	 * 获取与当天间隔了指定月份的日期。
	 */
	public static String getPreOrNextMonth(int months) {
		return getPreOrNextMonth(months,null);
	}
	
	/**
	 * 获取与当天间隔了指定天数的日期。
	 * @param days 间隔天数。正整数代表向前，负整数代表向后。
	 * @return 时间。默认格式：yyyy-MM-dd。
	 */
	public static String getPreOrNextDate(int days, String dateFormat) {
		dateFormat = dateFormat==null?"yyyy-MM-dd":dateFormat;
		DateFormat format = new SimpleDateFormat(dateFormat);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, days);
		return format.format(calendar.getTime());
	}
	/*
	 * 获取与当天间隔了指定天数的日期。
	 */
	public static String getPreOrNextDate(int days) {
		return getPreOrNextDate(days,null);
	}
	
	/*
	 * 比较2个时间的大小 yyyy-mm-dd HH:mi:ss
	 */
	public static String getDistanceTimes(String beginTime, String endTime){   
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    long between = 0;
	    String DistanceTimes = "";
	    try {
	        java.util.Date begin = dfs.parse(beginTime);
	        java.util.Date end = dfs.parse(endTime);
	        between = (end.getTime() - begin.getTime());// 得到两者的毫数
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	    long day = between / (24 * 60 * 60 * 1000);
	    long hour = (between / (60 * 60 * 1000) - day * 24);
	    long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
	    long sec = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
	    //long ms = (between - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - sec * 1000);
	    if(day-0.000001>0){
	    	DistanceTimes += day + "天" ;
	    }
	    if(hour-0.000001>0){
	    	DistanceTimes += hour + "小时" ;
	    }
	    if(min-0.000001>0){
	    	DistanceTimes += min + "分" ;
	    }
	    if(sec-0.000001>0){
	    	DistanceTimes += sec + "" ;
	    }
	    return DistanceTimes;
	}
	
	/**
	 * 是否在时间段内
	 */
	public static boolean isAllowDate(String startStr, String endStr){
		boolean flag = false;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			String nowStr = sdf.format(new Date());
			Date nowDate = sdf.parse(nowStr);
			Date startDate = sdf.parse(startStr);
			Date endDate = sdf.parse(endStr);
			long start = startDate.getTime();
			long end = endDate.getTime();
			long now = nowDate.getTime();
			
			if(now-start>=0 && end-now>=0){
				flag = true;
			}
		}
		catch(Exception e){
			flag = false;
			e.printStackTrace();
		}
		return flag; 
	}

	/**
	 * 是否在时间段内,日期+时间
	 * @param startStr
	 * @param endStr
     * @return
     */
	public static boolean isAllowDateForTime(String startStr,String endStr){
		boolean falg = false;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
			String nowStr = sdf.format(new Date());
			Date nowDate = sdf.parse(nowStr);
			Date startDate = sdf.parse(startStr);
			Date endDate = sdf.parse(endStr);
			long start = startDate.getTime();
			long end = endDate.getTime();
			long now = nowDate.getTime();
			if(now-start>=0 && end-now>=0){
				falg = true;
			}
		}catch(Exception e){
			falg = false;
			e.printStackTrace();
		}
		return falg;
	}
	
	/**
	 * 天数转化为秒
	 */
	public static int DataFormatToMin(String days){
		if(StringUtils.isNotBlank(days)){
			int day = Integer.parseInt(days);
			days = (day * 24 * 60 * 60) + "";
		}
		return Integer.parseInt(days);
	}
	
	public static String dateFormat(String day,String format1,String format2) throws ParseException{
		SimpleDateFormat sdf1 = new SimpleDateFormat(format1);
		SimpleDateFormat sdf2 = new SimpleDateFormat(format2);
		Date date = sdf1.parse(day);
		String dateStr = sdf2.format(date);
		return dateStr;
	}

	public static String getDateByFormat(Date date, String format) {
		if(date == null) {
			return null;
		}
		if (format == null || format.equals("")) {
			format = F_DATE10;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

}
