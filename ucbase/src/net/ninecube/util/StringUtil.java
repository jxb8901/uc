/**
 * 2005-9-3
 */
package net.ninecube.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author JXB
 *
 */
public class StringUtil {

	/**
	 * 判断给定对象s是否为空
	 * 如果s为null，返回真
	 * 如果s是0长字符串，返回真
	 * 如果s是字符串数组，且数组个数为0，返回真
	 * 如果s是字符串数组，且数组中的每个元素都是空字符串，返回真
	 * 如果s是集合，返回Collection.isEmpty
	 * 其它情况返回假
	 */
	public static boolean isEmpty(Object s) {
		if (s == null) return true;
		if (s instanceof String) return ((String)s).length() == 0;
		if (s.getClass().isArray() && ((Object[])s).length == 0) return true; 
		if (s instanceof String[]) {
			String[] ss = (String[])s;
			for (String s1 : ss) if (s1.length() != 0) return false;
			return true;
		}
		if (s instanceof Collection) {
			return ((Collection)s).isEmpty();
		}
		return false;
	}

	public static String nullable(Object s) {
		return s == null ? "" : s.toString();
	}
	
	public static String rtrim(String s, String suffix) {
		if (s.endsWith(suffix)) return s.substring(0, s.length() -suffix.length());
		return s;
	}
	
	public static String capitalFirst(String s) {
		return String.valueOf(s.charAt(0)).toUpperCase() + s.substring(1);
	}
	
	public static String rspace(String s, int n) {
		StringBuffer sb = new StringBuffer();
		sb.append(s);
		for (int i = 0; i < n - s.length(); i++)
			sb.append(" ");
		return sb.toString();
	}
	
	public static String lpadding(String s, int n, String padding) {
		StringBuffer strbuf = new StringBuffer();
		for (int i = 0; i < n - s.length(); i++) {
			strbuf.append(padding);
		}
		strbuf.append(s);
		return strbuf.toString();
	}
	
	public static String multiPadding(String str, int multi, char ch) {
		if (str.length() % multi == 0) return str;
		return StringUtil.padding(str, (str.length() / multi + 1) * multi, ch);
	}
	
	public static String padding(String str, int len, char ch) {
		for (int i = str.length(); i < len; i++) {
			str += ch;
		}
		return str;
	}
	
	public static String unPadding(String str, char ch) {
		while (str.charAt(str.length() - 1) == ch) str = str.substring(0, str.length() - 1);
		return str;
	}

	public static String space(int n) {
		String ret = "";
		for (int i = 0; i < n; i++) ret += " ";
		return ret;
	}
	
	public static boolean isNumber(String str) {
		return Pattern.matches("^(-?\\d+)(\\.\\d+)?$", str);
	}
	
	/**
	 * 取以"."分隔字符串中的前部，若字符串没有包含字符"."，则返回""
	 */
	public static String getFirstByDot(String str) {
		int index = str.indexOf(".");
		if (index == -1) return "";
		return str.substring(0, index);
	}
	
	/**
	 * 取以"."分隔字符串中的后部，若字符串没有包含字符"."，则返回""
	 */
	public static String getSecondByDot(String str) {
		int index = str.indexOf(".");
		if (index == -1) return str;
		return str.substring(index + 1, str.length());
	}
	
	/**
	 * 将字符串s按空格和逗号拆分为列表
	 * 与String.split方法不同的地方是：本方法的返回结果中不包括零长字符串
	 */
	public static List<String> split(String s) {
		return split(s, "[\\s,]+");
	}
	
	/**
	 * 与String.split方法不同的地方是：本方法的返回结果中不包括零长字符串
	 */
	public static List<String> split(String input, String sep) {
		if (input == null) return null;
		int index = 0;
        List<String> matchList = new ArrayList<String>();
        Matcher m = Pattern.compile(sep).matcher(input);

        // Add segments before each match found
        while(m.find()) {
        	if (index < m.start()) {
                String match = input.subSequence(index, m.start()).toString();
                matchList.add(match);
        	}
            index = m.end();
        }
        
        if (index < input.length())
        matchList.add(input.subSequence(index, input.length()).toString());

       return matchList;
	}

}
