/**
 * 2008-1-10
 */
package net.ninecube.core.db.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author JXB
 *
 */
public class Util {
	public static String join(String limit, List<String> args) {
		if (args == null || args.size() == 0) return "";
		if (args.size() == 1) return args.get(0);
		
		StringBuffer ret = new StringBuffer();
		ret.append(args.get(0));
		for (int i = 1; i < args.size(); i++)
			ret.append(limit).append(args.get(i));
		return ret.toString();
	}
	
	public static boolean isEmpty(Object o) {
		return o == null || o.toString().length() == 0;
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
}
