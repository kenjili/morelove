package wjy.morelove.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期时间工具
 * @author wjy
 */
public class DateTimeUtils {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


	public static String date2String(Date date){
		return sdf.format(date);
	}

	public static String date2StringYMD(Date date){
		return sdf.format(date).split(" ")[0];
	}

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
