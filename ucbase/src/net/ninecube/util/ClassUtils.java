/**
 * 
 * created on 2006-12-30
 */
package net.ninecube.util;

/**
 * 
 * @author jxb
 * 
 */
public class ClassUtils {

	public static String getShortClassName(Class clazz) {
		String name = clazz.getName();
		int index = name.lastIndexOf(".");
		if (index >= 0) {
			name = name.substring(index + 1);
		}
		return name;
	}
}
