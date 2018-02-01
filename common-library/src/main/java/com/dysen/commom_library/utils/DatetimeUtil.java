package com.dysen.commom_library.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 时间计算类
 * @author DengGuang
 */
@SuppressLint("SimpleDateFormat")
public class DatetimeUtil {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 计算时间方法
	 * @param beginDayStr 开始时间
	 * @param endDayStr   结束时间
	 * @return
	 */
	public static long subDay(String beginDayStr, String endDayStr){
		Date beginDate = null;
		Date endDate = null;
		
		long day = 0;
		try {
			beginDate = sdf.parse(beginDayStr);
			endDate = sdf.parse(endDayStr);
			
			// 计算天数
			day = (endDate.getTime()-beginDate.getTime())/(24*60*60*1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		// 当天入住、当天离开算1天时间
		if(day == 0){
			day = 1;
		}
		
		return day;
	}
	
	/**
	 * 获取当天日期
	 * @return
	 */
	public static String getTodayStr(){
		SimpleDateFormat format4 = new SimpleDateFormat("yyyyMMddhhmmss");
		
		// 订单编号
		Calendar c = Calendar.getInstance();
		Date today = c.getTime();
		String todayStr = format4.format(today);
		return todayStr;
	}
	/**
	 * 获取当天日期
	 * @return
	 */
	public static String getTodayDate(){
		SimpleDateFormat format4 = new SimpleDateFormat("yyyyMMdd");
		
		Calendar c = Calendar.getInstance();
		Date today = c.getTime();
		String todayStr = format4.format(today);
		return todayStr;
	}
	
	public static String getTodayF(){
		SimpleDateFormat format4 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		Calendar c = Calendar.getInstance();
		Date today = c.getTime();
		String todayStr = format4.format(today);
		return todayStr;
	}



	/**
	 * 获取当前时分秒
	 * @return
	 */
	public static Map<String, String> getDatetimes(){
		SimpleDateFormat f_yyyy = new SimpleDateFormat("yyyy");
		SimpleDateFormat f_MM = new SimpleDateFormat("MM");
		SimpleDateFormat f_dd = new SimpleDateFormat("dd");
		SimpleDateFormat f_HH = new SimpleDateFormat("HH");
		SimpleDateFormat f_mm = new SimpleDateFormat("mm");
		SimpleDateFormat f_ss = new SimpleDateFormat("ss");
		Calendar c = Calendar.getInstance();
		Date today = c.getTime();
		String d_yyyy = f_yyyy.format(today);	// 年
		String d_MM = f_MM.format(today);		// 月
		String d_dd = f_dd.format(today);		// 日
		String t_HH = f_HH.format(today);		// 时
		String t_mm = f_mm.format(today);		// 分
		String t_ss = f_ss.format(today);		// 秒
		Map<String, String> datetimes = new HashMap<String, String>();
		datetimes.put("yyyy", d_yyyy);
		datetimes.put("MM", d_MM);
		datetimes.put("dd", d_dd);
		datetimes.put("HH", t_HH);
		datetimes.put("mm", t_mm);
		datetimes.put("ss", t_ss);
		
		return datetimes;
	}

	public static Map<String, String> getDateMapByStringValue(String  strData){
		SimpleDateFormat f_yyyy = new SimpleDateFormat("yyyy");
		SimpleDateFormat f_MM = new SimpleDateFormat("MM");
		SimpleDateFormat f_dd = new SimpleDateFormat("dd");
		SimpleDateFormat f_HH = new SimpleDateFormat("HH");
		SimpleDateFormat f_mm = new SimpleDateFormat("mm");
		SimpleDateFormat f_ss = new SimpleDateFormat("ss");


		Date today =strToDate(strData);
		String d_yyyy = f_yyyy.format(today);	// 年
		String d_MM = f_MM.format(today);		// 月
		String d_dd = f_dd.format(today);		// 日
		String t_HH = f_HH.format(today);		// 时
		String t_mm = f_mm.format(today);		// 分
		String t_ss = f_ss.format(today);		// 秒
		Map<String, String> datetimes = new HashMap<String, String>();
		datetimes.put("yyyy", d_yyyy);
		datetimes.put("MM", d_MM);
		datetimes.put("dd", d_dd);
		datetimes.put("HH", t_HH);
		datetimes.put("mm", t_mm);
		datetimes.put("ss", t_ss);

		return datetimes;
	}
	
	/****
	 * 获取系统当前的日期
	 * 
	 * @author chenjianfan
	 * @return
	 */
	public static String getCurrentDate() {
		Calendar calendar = Calendar.getInstance();
		 SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日");
		return dateFormat.format(calendar.getTime());
	}

	/**
	 * 获取星期几
	 * @return
	 */
	public static String getCurrentWeek() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getToday());
		int week =  cal.get(Calendar.DAY_OF_WEEK);
		String strWeek = "星期天";
		switch (week) {
		case 1:
			strWeek = "星期天";
			break;
		case 2:
			strWeek = "星期一";
			break;
		case 3:
			strWeek = "星期二";
			break;
		case 4:
			strWeek = "星期三";
			break;
		case 5:
			strWeek = "星期四";
			break;
		case 6:
			strWeek = "星期五";
			break;
		case 7:
			strWeek = "星期六";
			break;
		default:
			strWeek = "星期天";
			break;
		}
		return strWeek;
	}
	
	/****
	 * 获取系统当前的时间
	 * 
	 * @author chenjianfan
	 * @return
	 */
	public static String getCurrentTime() {
		Calendar calendar = Calendar.getInstance();
		 SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		return dateFormat.format(calendar.getTime());
	}

	
	/**
	 * 格式转换
	 * @param str
	 * @return
	 */
	public static Date strToDate(String str) { 
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		Date date = null; 
		try { 
			date = format.parse(str); 
		} catch (Exception e) { 
			e.printStackTrace(); 
		} 
		return date; 
	} 
	
	
	/**
	 * 获得当天时间
	 * @param date
	 * @param iDaydiff
	 * @return
	 */
	public static String getDay(Date date, int iDaydiff){
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, iDaydiff);//把日期往后增加一天.整数往后推,负数往前移动
		date=calendar.getTime(); //这个时间就是日期往后推一天的结果
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date);
	}
	
	
	/**
	 * 获取当天日期  
	 * @return
	 */
	public static Date getToday(){
		/** 定义日期对象 */
		Calendar c = Calendar.getInstance();
		Date today = c.getTime();
		return today;
	}
	
}
