/**
 * 
 * created on 2007-4-25
 */
package net.ninecube.core.webwork.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.ninecube.lang.DateRange;
import net.ninecube.lang.Frequence;
import net.ninecube.util.DateUtil;

/**
 * TODO: 能否写一个interceptor按参数名称进行数据类型转换？
 * @author jxb
 */
public class DateRangeByNameConverter {
	/** Name_Frequence_Start|End */
	private static final Pattern p1 = Pattern.compile("([^_]*)_([^_]*)_(Start|End)");

	public static void convert(Map<String, Object> parameters) {
		Map<String, String[]> found = new HashMap<String, String[]>();
		for (Map.Entry<String, ?> en : parameters.entrySet()) {
			Matcher m = p1.matcher(en.getKey());
			if (m.matches()) found.put(m.group(1) + m.group(2), new String[]{m.group(1), m.group(2)});
		}
		for (String[] ss : found.values()) {
			convert(ss[0], ss[1], parameters);
		}
	}
	
	private static void convert(String name, String freq, Map<String, Object> parameters) {
		Date start = obj2Date(parameters.remove(name + "_" + freq + "_" + "Start"));
		Date end = obj2Date(parameters.remove(name + "_" + freq + "_" + "End"));
		if (start == null && end == null) return;
		if (start == null) start = new Date();
		if (end == null) end = new Date();
		put(parameters, name, new DateRange(Frequence.get(freq), start, end));
	}
	
	@SuppressWarnings("unchecked")
	private static void put(Map<String, Object> parameters, String name, DateRange d) {
		Object o = parameters.get(name);
		if (o == null) {
			parameters.put(name, d);
		}
		else if (o instanceof List) {
			((List<DateRange>)o).add(d);
		}
		else {
			List list = new ArrayList<DateRange>();
			list.add(o);
			list.add(d);
			parameters.put(name, list);
		}
	}
	
	private static Date obj2Date(Object date) {
		if (date == null) return null;
		String s = null;
		if (date instanceof String[]) {
			s = ((String[])date)[0];
		}
		else if (date instanceof String[]) {
			s = (String) date;
		}
		else if (date instanceof Date) {
			return (Date) date;
		}
		else {
			s = date.toString();
		}
		
		return DateUtil.parseDate(s, "yyyy-MM-dd");
	}
}
