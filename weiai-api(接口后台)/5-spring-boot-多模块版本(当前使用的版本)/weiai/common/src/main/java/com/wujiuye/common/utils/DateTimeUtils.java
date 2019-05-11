package com.wujiuye.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 时间戳转日期字符串
	 * 
	 * @param datetime
	 * @return
	 */
	public static String longtime2Stringtime(long datetime) {
		Date date = new Date(datetime * 1000);
		return sdf.format(date);
	}

	/**
	 * 日期字符串转时间戳
	 * 
	 * @param datetime
	 * @return
	 */
	public static long stringtime2Longdatetime(String datetime) {
		try {
			Date date = sdf.parse(datetime);
			return date.getTime() / 1000;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 获取当前年月日
	 * 
	 * @return
	 */
	public static String getCurrentYMDString() {
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH) + 1;
		int day = now.get(Calendar.DAY_OF_MONTH);
		String strYMD = year + "-" + (month > 9 ? month : "0" + month) + "-" + (day > 9 ? day : "0" + day);
		return strYMD;
	}
	
	/**
	 * 年月日转日期字符串
	 * @param y
	 * @param m
	 * @param d
	 * @return
	 */
	public static String ymd2StringDate(int y,int m,int d) {
		String date = y+"-";
		date+=m>9?m:"0"+m;
		date+="-";
		date+=d>9?d:"0"+d;
		return date;
	}

	/**
	 * 获取给定时间戳的年月日字符串
	 * 
	 * @param datetime
	 *            时间戳 单位为毫秒
	 * @return
	 */
	public static String getLongDateTimeYMDString(long datetime) {
		Calendar now = Calendar.getInstance();
		now.clear();
		now.setTimeInMillis(datetime * 1000);
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH) + 1;
		int day = now.get(Calendar.DAY_OF_MONTH);
		String strYMD = year + "-" + (month > 9 ? month : "0" + month) + "-" + (day > 9 ? day : "0" + day);
		return strYMD;
	}

	/**
	 * 获取给定时间戳的年月日字符串
	 * 
	 * @param datetime
	 *            时间戳 单位为毫秒
	 * @return
	 */
	public static String getLongDateTimeYMDString2(long datetime) {
		Calendar now = Calendar.getInstance();
		now.clear();
		now.setTimeInMillis(datetime * 1000);
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH) + 1;
		int day = now.get(Calendar.DAY_OF_MONTH);
		String strYMD = year + "年" + (month > 9 ? month : "0" + month) + "月" + (day > 9 ? day : "0" + day) + "日";
		return strYMD;
	}

	/**
	 * 获取系统当前时间字符串，格式化yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new Date());
		return time;
	}

	/**
	 * 获取多少天前的年月日字符串表示
	 * 
	 * @param beforDays
	 *            多少天前
	 * @return
	 */
	public static String getBeforDayYMDStringWithDays(int beforDays) {
		Calendar now = Calendar.getInstance();
		now.clear();
		now.setTimeInMillis((System.currentTimeMillis() / 1000 - beforDays * 24 * 60 * 60) * 1000);
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH) + 1;
		int day = now.get(Calendar.DAY_OF_MONTH);
		String strYMD = year + "-" + (month > 9 ? month : "0" + month) + "-" + (day > 9 ? day : "0" + day);
		return strYMD;
	}

	/**
	 * 获取多少天后的年月日字符串表示
	 * 
	 * @param afterDays
	 *            多少天后
	 * @return
	 */
	public static String getAfterDaysDayYMDStringWithDays(int afterDays) {
		Calendar now = Calendar.getInstance();
		now.clear();
		now.setTimeInMillis((System.currentTimeMillis() / 1000 + afterDays * 24 * 60 * 60) * 1000);
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH) + 1;
		int day = now.get(Calendar.DAY_OF_MONTH);
		String strYMD = year + "-" + (month > 9 ? month : "0" + month) + "-" + (day > 9 ? day : "0" + day);
		return strYMD;
	}

	/**
	 * 获取当前年号
	 * 
	 * @return
	 */
	public static int getCurrentYear() {
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		return year;
	}
	
	/**
	 * 获取当前月
	 * 
	 * @return
	 */
	public static int getCurrentMonth() {
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.MONTH)+1;
		return year;
	}
	
	/**
	 * 获取当前日
	 * 
	 * @return
	 */
	public static int getCurrentDay() {
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.DAY_OF_MONTH);
		return year;
	}

	/**
	 * 获取年月日转时间戳-当日的开始时间
	 * 
	 * @param y
	 * @param m
	 * @param d
	 * @return
	 */
	public static long getLongTimeStartWith(int y, int m, int d) {
		String datetime = y + "-" + (m <= 9 ? "0" + m : m) + "-" + (d <= 9 ? "0" + d : d) + " 00:00:00";
		return stringtime2Longdatetime(datetime);
	}

	/**
	 * 获取年月日转时间戳-当日的结束时间
	 * 
	 * @param y
	 * @param m
	 * @param d
	 * @return
	 */
	public static long getLongTimeEndWith(int y, int m, int d) {
		String datetime = y + "-" + (m <= 9 ? "0" + m : m) + "-" + (d <= 9 ? "0" + d : d) + " 23:59:59";
		return stringtime2Longdatetime(datetime);
	}

	/**
	 * 计算两个时间戳的相隔天数
	 * 
	 * @param currentDatetime
	 *            当前时间戳
	 * @param lastDatetime
	 *            比较的时间戳，过去
	 * @return
	 */
	public static int datetimeOrDatetimeDays(long currentDatetime, long lastDatetime) {
		// 先算出两时间的毫秒数之差大于一天的天数
		int betweenDays = (int) ((currentDatetime - lastDatetime) / (24 * 60 * 60)); 
		Calendar beginCalendar = Calendar.getInstance();
		beginCalendar.setTimeInMillis(lastDatetime * 1000);
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTimeInMillis(currentDatetime * 1000);
		// 使endCalendar减去这些天数，将问题转换为两时间的毫秒数之差不足一天的情况
		endCalendar.add(Calendar.DAY_OF_MONTH, -betweenDays);
		// 再使endCalendar减去1天
		endCalendar.add(Calendar.DAY_OF_MONTH, -1);
		// 比较两日期的DAY_OF_MONTH是否相等
		if (beginCalendar.get(Calendar.DAY_OF_MONTH) == endCalendar.get(Calendar.DAY_OF_MONTH))
			return betweenDays + 1; // 相等说明确实跨天了
		else
			return betweenDays + 0; // 不相等说明确实未跨天
	}

	/**
	 * 日期大小字符串比较
	 * 
	 * @param datetime1
	 * @param datetime2
	 * @return
	 */
	public static int datetimeStringComp(String datetime1, String datetime2) {
		if (datetime1.equals(datetime2)) {
			return 0;
		} else {
			for (int i = 0; i < datetime1.length(); i++) {
				if (datetime1.charAt(i) > datetime2.charAt(i)) {
					return 1;
				} else if (datetime1.charAt(i) < datetime2.charAt(i)) {
					return -1;
				}
			}
		}
		return 0;
	}
}
