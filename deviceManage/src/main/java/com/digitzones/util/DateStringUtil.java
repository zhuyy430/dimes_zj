package com.digitzones.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
/**
 * 日期字符串转换工具类
 * @author Administrator
 */
public final class DateStringUtil {
	private String pattern ;
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
		sdf.applyPattern(pattern);
	}
	private SimpleDateFormat sdf ;
	public DateStringUtil(){
		this("yyyy-MM-dd");
	}
	public DateStringUtil(String pattern){
		this.pattern = pattern;
		sdf = new SimpleDateFormat(this.pattern);
	}

	/**
	 * 将给定的时间Hms设置为23:59:59
	 * @param date
	 * @return
	 */
	public static  Date toDaysEnd(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY,23);
		c.set(Calendar.MINUTE,59);
		c.set(Calendar.SECOND,59);
		return c.getTime();
	}
	/**
	 * 产生给定时间段的年份
	 * @param minYear
	 * @param maxYear
	 * @return
	 */
	public List<String> generateYearsBetween(String minYear,String maxYear){
		List<String> yearList = new ArrayList<>();
		this.setPattern("yyyy");
		try {
			Date minYearDate = sdf.parse(minYear);
			Date maxYearDate = sdf.parse(maxYear);
			Calendar minYearCal = Calendar.getInstance();
			minYearCal.setTime(minYearDate);
			Calendar maxYearCal = Calendar.getInstance();
			maxYearCal.setTime(maxYearDate);
			int year = minYearCal.get(Calendar.YEAR);
			int mxYear = maxYearCal.get(Calendar.YEAR);
			yearList.add(year+"");
			while(true) {
				if(year>mxYear) {
					break;
				}
				 year ++;
				 yearList.add(year+"");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.setPattern("yyyy-MM-dd");
		return yearList;
	}
	/**
	 * 产生给定时间段的月份(yyyy-MM)
	 * @param minDate
	 * @param maxDate
	 * @return
	 */
	public List<String> generateMonthsBetween(String minDate,String maxDate){
		List<String> dateList = new ArrayList<>();
		this.setPattern("yyyy-MM");
		try {
			Date minMonthDate = sdf.parse(minDate);
			Date maxMonthDate = sdf.parse(maxDate);
			String maxMonthString = sdf.format(maxMonthDate);
			Calendar minMonthCal = Calendar.getInstance();
			minMonthCal.setTime(minMonthDate);
			Calendar maxMonthCal = Calendar.getInstance();
			maxMonthCal.setTime(maxMonthDate);
			while(true) {
				String minMonthStirng  = sdf.format(minMonthCal.getTime());
				if(maxMonthString.compareTo(minMonthStirng)<0) {
					break;
				}
				dateList.add(minMonthStirng);
				minMonthCal.add(Calendar.MONTH, 1);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.setPattern("yyyy-MM-dd");
		return dateList;
	}
	/**
	 * 产生给定时间段的日期(yyyy-MM-dd)
	 * @param minDate
	 * @param maxDate
	 * @return
	 */
	public List<String> generateDaysBetween(String minDate,String maxDate){
		List<String> dateList = new ArrayList<>();
		if(minDate.compareTo(maxDate)>0) {
			return dateList;
		}
		try {
			Date minMonthDate = sdf.parse(minDate);
			Date maxMonthDate = sdf.parse(maxDate);
			Calendar minMonthCal = Calendar.getInstance();
			minMonthCal.setTime(minMonthDate);
			Calendar maxMonthCal = Calendar.getInstance();
			maxMonthCal.setTime(maxMonthDate);
			while(true) {
				dateList.add(sdf.format(minMonthCal.getTime()));
				if(DateUtils.isSameDay(minMonthCal, maxMonthCal)) {
					break;
				}
				minMonthCal.add(Calendar.DATE, 1);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateList;
	}
	/**
	 * 产生给定时间段的周(yyyy-周)
	 * @param minDate
	 * @param maxDate
	 * @return
	 */
	public List<String> generateWeeksBetween(String minDate,String maxDate){
		List<String> dateList = new ArrayList<>();
		if(minDate.compareTo(maxDate)>0) {
			return dateList;
		}
		try {
			Calendar minCal = Calendar.getInstance();
			Calendar maxCal = Calendar.getInstance();
			minCal.setTime(sdf.parse(minDate));
			maxCal.setTime(sdf.parse(maxDate));
			int minWeek = minCal.get(Calendar.WEEK_OF_YEAR);
			while(true) {
				if(minCal.after(maxCal)) {
					break;
				}
				dateList.add(minCal.get(Calendar.YEAR)+"-" +minWeek);
				minCal.add(Calendar.WEEK_OF_YEAR, 1);
				minWeek = minCal.get(Calendar.WEEK_OF_YEAR);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateList;
	}
	
	/**
	 * 日期转换到月份(MM)
	 * @param date
	 * @return
	 */
	public String date2MonthOnly(Date date) {
		String pat = this.pattern;
		setPattern("MM");
		String month = sdf.format(date);
		setPattern(pat);
		return month;
	}
	/**
	 * 根据参数给定时间，向前推12个小时(720分钟)
	 * @param date
	 * @return
	 */
	public synchronized List<Date> generate720Minutes(Date date){
		List<Date> list = new ArrayList<>();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR, -12);
		for(int i = 0;i<720;i++) {
			 c.add(Calendar.MINUTE, 1);
			 Date d = c.getTime();
			 list.add(d);
		}
		return list;
	}
	/**
	 * 字符串转日期
	 * @param dateString
	 * @return
	 * @throws ParseException
	 */
	public Date string2Date(String dateString){
		try {
			return sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
			System.err.println("日期格式不合法,可通过调用getPattern方法查看日期格式!");
		}
		return null;
	}
	/**
	 * 日期转字符串
	 * @param date
	 * @return
	 */
	public String date2String(Date date){
		return sdf.format(date);
	}
	/**
	 * 日期转换到日
	 * @param date
	 * @return
	 */
	public String date2DayOfMonth(Date date){
		String pat = this.pattern;
		setPattern("dd");
		String day = sdf.format(date);
		setPattern(pat);
		return day;
	}
	/**
	 * 日期转换到年月
	 * @param date
	 * @return
	 */
	public String date2Month(Date date){
		String pat = this.pattern;
		setPattern("yyyy-MM");
		String month = sdf.format(date);
		setPattern(pat);
		return month;
	}
	/**
	 * 产生当前年的十二个月
	 * @param date 
	 * @return
	 */
	public synchronized List<Date> generateOneYearMonth(Date date){
		String pat = this.pattern;
		setPattern("yyyy");
		String year = sdf.format(date);
		List<Date> months = new ArrayList<>();
		setPattern("yyyy-MM");
		for(int i = 1;i<=12;i++) {
			String d = year + "-"+(i<10?("0"+i):i);
			try {
				months.add(sdf.parse(d));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		setPattern(pat);
		return months;
	}
	
	/**
	 * 根据当前年月产生一个月的日期对象
	 * @param currentMonth yyyy-MM格式的字符串
	 * @return List<Date>
	 */
	public synchronized List<Date> generateOneMonthDay(String currentMonth){
		List<Date> dates = new ArrayList<Date>();
		String pat = this.pattern;
		setPattern("yyyy-MM");
		try {
			
			Calendar c = Calendar.getInstance();
			Date date = sdf.parse(currentMonth);
			c.setTime(date);
			int month = c.get(Calendar.MONTH);
			while(true){
				int newMonth = c.get(Calendar.MONTH);
				if(newMonth!=month){
					break;
				}
				dates.add(c.getTime());
				c.add(Calendar.DAY_OF_MONTH, 1);
			}
			setPattern(pat);
		} catch (ParseException e) {
			System.err.println("日期格式不正确，需要yyyy-MM格式字符串,但得到的字符串为：" + currentMonth);
		}
		return dates;
	}
	/**
	 * 比较两个时间是否相同
	 * @param one
	 * @param two
	 * @return
	 */
	public boolean equals(Date one,Date two) {
		if(one==null || two==null) {
			return false;
		}
		
		if(one==two) {
			return true;
		}
		Calendar oneCal = Calendar.getInstance();
		Calendar twoCal = Calendar.getInstance();
		oneCal.setTime(one);
		twoCal.setTime(two);
		return oneCal.get(Calendar.YEAR)==twoCal.get(Calendar.YEAR)&&
			   oneCal.get(Calendar.MONTH)==twoCal.get(Calendar.MONTH)&&
			   oneCal.get(Calendar.DATE)==twoCal.get(Calendar.DATE);
				
	}
	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		System.out.println(cal.get(Calendar.WEEK_OF_YEAR));
	}
}
