/**
 * created on 2006-4-6
 */
package net.ninecube.lang;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.ninecube.util.DataTypeUtil;

/**
 * @author JXB
 */
public class TypedMap<K, V> extends HashMap<K, V> {
	
	//~ constructor

	public TypedMap() {
		super();
	}

	public TypedMap(int arg0) {
		super(arg0);
	}

	public TypedMap(int arg0, float arg1) {
		super(arg0, arg1);
	}

	public TypedMap(Map<? extends K, ? extends V> arg0) {
		super(arg0);
	}
	
	public String getString(String paramName) {
		return obj2String(this.get(paramName));
	}
	
	public static String obj2String(Object o) {
		return DataTypeUtil.obj2String(o);
	}

	public String getStringTrim(String paramName) {
		String s = this.getString(paramName);
		if (s == null) {
			return "";
		}
		return s;
	}
	
	public String[] getStringValues(String paramName) {
		Object o = this.get(paramName);
		if (o == null) return null;
		if (o instanceof String) return new String[]{(String)o};
		if (o instanceof String[]) return (String[])o;
		throw new IllegalArgumentException();
	}

	public BigDecimal getBigDecimal(String paramName) {
		if (get(paramName) instanceof BigDecimal) return (BigDecimal)get(paramName);
		return DataTypeUtil.str2BigDecimal(getString(paramName));
	}

	public Integer getInteger(String paramName) {
		return DataTypeUtil.str2Integer(getString(paramName));
	}

	public int getInt(String paramName, int defaultValue) {
		return DataTypeUtil.str2Int(getString(paramName), defaultValue);
	}
	
	public Date getDate(String paramName, String pattern) {
		Object obj = get(paramName);
		if (obj == null) return null;
		if (obj instanceof Date) return (Date)obj;
		return DataTypeUtil.str2Date(getString(paramName), pattern);
	}

	public boolean getBoolean(String paramName) {
		return DataTypeUtil.str2Boolean(getString(paramName), false);
	}

	public boolean getBoolean(String paramName, boolean defaultValue) {
		return DataTypeUtil.str2Boolean(getString(paramName), defaultValue);
	}

}
