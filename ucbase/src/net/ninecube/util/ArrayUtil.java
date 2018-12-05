/**
 * 2005-8-18
 */
package net.ninecube.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * @author JXB
 *
 */
public class ArrayUtil {
	
	public static <T> T[] merge(T[] t1, T[] t2) {
		List<T> ret = new ArrayList<T>();
		ret.addAll(Arrays.asList(t1));
		ret.addAll(Arrays.asList(t2));
		return ret.toArray(t1);
	}
	
	public static boolean isEmpty(Collection c) {
		return c == null || c.isEmpty();
	}
	
	public static boolean isEmpty(Object[] o) {
		return o == null || o.length == 0;
	}
	
	public static <T> boolean in(T o, T[] array) {
		for (int i = 0; i < array.length; i++) {
			if (o.equals(array[i])) return true;
		}
		return false;
	}
	public static <T> boolean in(T o, Collection<T> array) {
		for (T a : array) {
			if (o.equals(a)) return true;
		}
		return false;
	}
	public static boolean in(String array,String array2){
		if (array == null || array2 == null)
			return false;
		Object[] os = asArray(array);
		Object[] os2 = asArray(array2);
		for (int i = 0; i < os.length; i++) {
			for(int j=0;j<os2.length;j++){
				if (((String)os[i]).trim().equals(((String)os2[j]).trim()))
					return true;
			}
		}
		return false;
	}
	
	public static String arr2Str(int[] os) {
		return join(os);
	}
	
	public static String arr2Str(String[] os) {
		return join(os);
	}
	
	public static String join(Object o) {
		return join(o, ",");
	}
	
	public static String join(Object o, String split) {
		if (o == null) return null;
		Object[] arr = asArray(o);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			sb.append(arr[i]).append(split);
		}
		if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	public static Object[] asArray(Object o) {
		if (o == null) return new Object[0];
		
		if (o instanceof Collection) {
			Collection c = (Collection)o;
			return c.toArray(new Object[c.size()]);
		}
		else if (o instanceof String[] && ((String[])o).length == 1) {
			return ((String[])o)[0].split(",");
		}
		else if (o.getClass().isArray()) {
			Object[] ret = new Object[Array.getLength(o)];
			for (int i = 0; i < ret.length; i++)
				ret[i] = Array.get(o, i);
			return ret;
		}
		else if (o instanceof String) {
			String s = (String)o;
			return s.split(",");
		}
		else {
			return new Object[]{o};
		}
	}
}
