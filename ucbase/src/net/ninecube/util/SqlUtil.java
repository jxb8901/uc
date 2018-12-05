/**
 * created on 2006-1-20
 */
package net.ninecube.util;

/**
 * @author JXB
 */
public class SqlUtil {

	public static String arr2in(String[] s) {
		StringBuffer ret = new StringBuffer();
		if (s.length > 0) ret.append("'").append(s[0]).append("'");
		for (int i = 1; i < s.length; i++) {
			ret.append(",'").append(s[i]).append("'");
		}
		return ret.toString();
	}
}
