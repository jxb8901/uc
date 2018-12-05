package net.ninecube.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static String getYYYYMMDD_HHMMSS() {
		return getYYYYMMDD() + " " + getHHMMSS();
	}
    
    public static String getYYYYMMDD(){
    	return getYYYYMMDD(new Date());
    }
   
    public static String getYYYY(Date date){
    	SimpleDateFormat f = new SimpleDateFormat("yyyy");
    	return f.format(date);
    }
    
    public static String getYYYYMMDD(Date date) {
    	SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
    	return f.format(date);
    }
    
    public static String getHHMMSS(){
    	return getHHMMSS(new Date());
    }
    
    public static String getHHMMSS(Date date) {
    	SimpleDateFormat f = new SimpleDateFormat("HHmmss");
    	return f.format(date);
    }
    
    public static boolean isCurrentDate(Date date) {
    	return getYYYYMMDD().equals(getYYYYMMDD(date));
    }
    
    public static Date fromYYYYMMDD(String date) {
    	return parseDate(date, "yyyyMMdd");
    }
    
    public static String format(Date date, String pattern){
    	SimpleDateFormat f = new SimpleDateFormat(pattern);
    	return f.format(date);
    }
    
    public static Date parseDate(String date, String pattern) {
    	SimpleDateFormat f = new SimpleDateFormat(pattern);
    	f.setLenient(false);
    	try {
			return f.parse(date);
		} catch (ParseException e) {
			return null;
		}
    }
    
    public static Date getLastDateOfMonth(int year, int month) {
		if (month == 12) return DateUtil.parseDate(year + "1231", "yyyyMMdd");
    	month = month + 1;
		Date date = DateUtil.parseDate(year + (month > 9 ? "" + month : "0"+month), "yyyyMM");
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, -1);
		return c.getTime();
    }

    public static Date getFirstDateOfMonth(int year, int month) {
		return DateUtil.parseDate(year + (month > 9 ? "" + month : "0"+month), "yyyyMM");
    }
    
    public static Date digestParts(Date date, String pattern) {
		SimpleDateFormat fmt = new SimpleDateFormat(pattern);
		String str = fmt.format(date);
		try {
			return fmt.parse(str);
		} catch (ParseException exc) {
			throw new IllegalArgumentException("illegal pattern '" + pattern
					+ "' . ", exc);
		}
	}
    
    public static Date addFieldValue(Date date, int field, int val){
    		Calendar c = Calendar.getInstance();
    		c.setTime(date);
    		c.add(field, val);
    		return c.getTime();
   }
    
}
