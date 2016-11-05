package com.enation.framework.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期相关的操作
 * @author Dawei
 *  
 */

public class DateUtil {

	/**
	 * 将一个字符串转换成日期格式
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static Date toDate(String date, String pattern) {
		if((""+date).equals("")){
			return null;
		}
		if(pattern == null){
			pattern = "yyyy-MM-dd";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date newDate = new Date();
		try {
			newDate = sdf.parse(date);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return newDate;
	}
	
	/**
	 * 把日期转换成字符串型
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String toString(Date date, String pattern){
		if(date == null){
			return "";
		}
		if(pattern == null){
			pattern = "yyyy-MM-dd";
		}
		String dateString = "";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			dateString = sdf.format(date);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dateString;
	}
	
	public static String toString(Long time,String pattern){
		if(time>0){
			if(time.toString().length()==10){
				time = time*1000;
			}
			Date date = new Date(time);
			String str  = DateUtil.toString(date, pattern);
			return str;
		}
		return "";
	}

	/*
	*  获取数天后天的某天的时间
	* */
	public static Date getDateAfter(int days){
		Calendar now = Calendar.getInstance();
		// 获取当前时间
		now.setTime(new Date());
		now.set(Calendar.DATE, now.get(Calendar.DATE) + days);
		return now.getTime();
	}

	/*
	*  获取数天后天的某天的时间
	* */
	public static String getDateAfter(int days , String pattern){
		Calendar now = Calendar.getInstance();
		// 获取当前时间
		now.setTime(new Date());
		now.set(Calendar.DATE, now.get(Calendar.DATE) + days);
		if(pattern == null){
			pattern = "yyyy-MM-dd";
		}
		return toString(now.getTime() , pattern);
	}
	
	/**
	 * 获取上个月的开始结束时间
	 * @return
	 */
	public static String[] getLastMonth() {
		   // 取得系统当前时间
		   Calendar cal = Calendar.getInstance();
		   int year = cal.get(Calendar.YEAR);
		   int month = cal.get(Calendar.MONTH) + 1;
		   
		   // 取得系统当前时间所在月第一天时间对象
		   cal.set(Calendar.DAY_OF_MONTH, 1);
		   
		   // 日期减一,取得上月最后一天时间对象
		   cal.add(Calendar.DAY_OF_MONTH, -1);
		   
		   // 输出上月最后一天日期
		   int day = cal.get(Calendar.DAY_OF_MONTH);

		   String months = "";
		   String days = "";

		   if (month > 1) {
		    month--;
		   } else {
		    year--;
		    month = 12;
		   }
		   if (!(String.valueOf(month).length() > 1)) {
		    months = "0" + month;
		   } else {
		    months = String.valueOf(month);
		   }
		   if (!(String.valueOf(day).length() > 1)) {
		    days = "0" + day;
		   } else {
		    days = String.valueOf(day);
		   }
		   String firstDay = "" + year + "-" + months + "-01";
		   String lastDay = "" + year + "-" + months + "-" + days;

		   String[] lastMonth = new String[2];
		   lastMonth[0] = firstDay;
		   lastMonth[1] = lastDay;

		 //  ////System.out.println(lastMonth[0] + "||" + lastMonth[1]);
		   return lastMonth;
		}
	
	/*
	*  获取数天后天的某天的时间
	* */
	public static String getDateAfterForGoods(int days){
		Calendar now = Calendar.getInstance();
		// 获取当前时间
		now.setTime(new Date());
		//now.set(Calendar.DATE, now.get(Calendar.DATE) + days);
		now.add(Calendar.DATE,days);
		return toString(now.getTime() , "yyyy-MM-dd");
	}
	/**
	 * 获取当月的开始结束时间
	 * @return
	 */
	public static String[] getCurrentMonth() {
		   // 取得系统当前时间
		   Calendar cal = Calendar.getInstance();
		   int year = cal.get(Calendar.YEAR);
		   int month = cal.get(Calendar.MONTH)+1 ;
		   // 输出下月第一天日期
		   int notMonth = cal.get(Calendar.MONTH)+2 ;
		   // 取得系统当前时间所在月第一天时间对象
		   cal.set(Calendar.DAY_OF_MONTH, 1);
		   
		   // 日期减一,取得上月最后一天时间对象
		   cal.add(Calendar.DAY_OF_MONTH, -1);
		   
		  

		   String months = "";
		   String nextMonths = "";


		   if (!(String.valueOf(month).length() > 1)) {
		    months = "0" + month;
		   } else {
		    months = String.valueOf(month);
		   }
		   if (!(String.valueOf(notMonth).length() > 1)) {
			   nextMonths = "0" + notMonth;
		   } else {
			   nextMonths = String.valueOf(notMonth);
		   }
		   String firstDay = "" + year + "-" + months + "-01";
		   String lastDay=	""+year+"-"+nextMonths+"-01";
		   String[] currentMonth = new String[2];
		   currentMonth[0] = firstDay;
		   currentMonth[1] = lastDay;

		 //  ////System.out.println(lastMonth[0] + "||" + lastMonth[1]);
		   return currentMonth;
		}
	/**
	 * @param 获取的日期+时间
	 * @return 日期+时间对应的长整形
	 */
	public static long getTimeline(String date){
		return (long)(toDate(date, "yyyy-MM-dd HH:mm:ss").getTime()/1000);
	}	
 
	public static long getDateline(){
		return System.currentTimeMillis()/1000;
	}
	public static long getDateline(String date){
		return (long)(toDate(date, "yyyy-MM-dd").getTime()/1000);
	}
	public static long getDateline(String date,String pattern){
		return (long)(toDate(date, pattern).getTime()/1000);
	}
	public static void main(String[] args){
		
		/*	long d= 1319990400 ;
					d=d*1000;
			int line =getDateline("2011-10-31");
		
			////System.out.println( line +   "--"+toString(new Date(d), "yyyy-MM-dd"));
			////System.out.println(d);*/
		
//		int d1 =getDateline("2011-10-30");
//		int d2 =getDateline("2011-10-15");
//		
//		////System.out.println(d1);
//		////System.out.println(d2);
//		
//		int f = 15 *24*60*60;
//		
//		////System.out.println(d1-f);
		
		////System.out.println( new Date(1320205608000l));
		////System.out.println( DateUtil.toString( new Date(1320205608000l),"yyyy-MM-dd HH:mm:ss"));
	}
}
