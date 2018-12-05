/**
 * 2005-8-20
 */
package net.ninecube.util;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author JXB
 *
 */
public class DataTypeUtil {

	
	public static String intArray2Str(int[] a) {
		StringBuffer ret = new StringBuffer();
		for (int i = 0; i < a.length; i++) {
			ret.append(a[i]).append(",");
		}
		if (ret.length() > 0) ret.deleteCharAt(ret.length() - 1);
		return ret.toString();
	}
	
	public static int[] str2IntArray(String s) {
		String[] sp = s.split(",");
		int[] ret = new int[sp.length];
		for (int i = 0; i < ret.length; i++) {
			try {
				ret[i] = Integer.parseInt(sp[i]);
			} catch (NumberFormatException e) {
				ret[i] = 0;
			}
		}
		return ret;
	}

    public static Date obj2Date(Object o) {
        if (o == null || o.toString().length() == 0)
            return null;
        if (o instanceof java.util.Date) 
        	return new Date(((java.util.Date)o).getTime());
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return new Date(f.parse(o.toString()).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Time obj2Time(Object o) {
        if (o == null || o.toString().length() == 0)
            return null;
        if (o instanceof java.util.Date) 
        	return new Time(((java.util.Date)o).getTime());
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return new Time(f.parse(o.toString()).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Timestamp obj2Timestamp(Object o) {
        if (o == null || o.toString().length() == 0)
            return null;
        if (o instanceof java.util.Date) 
        	return new Timestamp(((java.util.Date)o).getTime());
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return new Timestamp(f.parse(o.toString()).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static boolean obj2boolean(Object o) {
        if (o != null && o.toString().length() != 0) {
            return "1".equals(o.toString());
        }
        return false;
    }
    
    private static String bigDecimal2Str(BigDecimal b) {
    	if (b == null) return null;
    	return b.toString();
    }
    
    public static String boolean2Str(boolean b) {
        return b ? "1" : "0";
    }
    
    private static String date2Str(java.util.Date date) {
    	if (date == null) return null;
    	return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
    
    private static String timestamp2Str(java.util.Date date) {
    	if (date == null) return null;
    	return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }
    public static List<String> array2List(String[] array) {
    	if (array == null) return null;
    	List<String> list=new ArrayList<String>();
    	for(int i=0;i<array.length;i++)
    		list.add(array[i].trim());
    	return list;
    }

	public static BigDecimal str2BigDecimal(String s) {
		if (s != null && s.length() > 0) {
			 s = s.trim().replaceAll(",","");
			try {
				return new BigDecimal(s);
			} catch (NumberFormatException ignore) {
			}
		}
		return null;
	}

	public static Long str2Long(String s) {
		if (s != null && s.length() > 0) {
			try {
				return Long.valueOf(s.trim());
			} catch (NumberFormatException ignore) {
			}
		}
		return null;
	}

	public static Integer str2Integer(String s) {
		if (s != null && s.length() > 0) {
			try {
				return Integer.valueOf(s.trim());
			} catch (NumberFormatException ignore) {
			}
		}
		return null;
	}

	public static int str2Int(String s, int defaultValue) {
		Integer i = str2Integer(s);
		if (i == null)
			return defaultValue;
		return i.intValue();
	}

	public static Date str2Date(String s, String pattern) {
		if (s == null || s.length() == 0) return null;
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(pattern);
		format.setLenient(false);
		Date date = null;
		try {
			date = format.parse(s.trim());
		} catch (ParseException e) { }
		if (date != null) {
			if (!s.equals(format.format(date))) date = null;
		}
		return date;
	}

	public static boolean str2Boolean(String s) {
		return str2Boolean(s, false);
	}

	public static boolean str2Boolean(String s, boolean defaultValue) {
		if (s != null && s.length() > 0) {
			try {
				return Boolean.valueOf(s.trim()).booleanValue();
			} catch (NumberFormatException ignore) {
			}
		}
		return defaultValue;
	}
	
	//~ obj2xxx

	public static String obj2String(Object o) {
		if (o == null) return null;
		if (o instanceof String) return (String)o;
		if (o instanceof String[]) return ((String[])o)[0];
		return o.toString();
	}
	
	public static String[] obj2Strings(Object o) {
		if (o == null) return null;
		if (o instanceof String) return new String[]{(String)o};
		if (o instanceof String[]) return (String[])o;
		throw new IllegalArgumentException();
	}

	public static BigDecimal obj2BigDecimal(Object o) {
		if (o == null) return null;
		if (o instanceof BigDecimal) return (BigDecimal)o;
		return str2BigDecimal(obj2String(o));
	}

	public static Long obj2Long(Object o) {
		if (o == null) return null;
		if (o instanceof Long) return (Long)o;
		return str2Long(obj2String(o));
	}

	public static Integer obj2Integer(Object o) {
		if (o == null) return null;
		if (o instanceof Integer) return (Integer)o;
		return str2Integer(obj2String(o));
	}

	public static int obj2Int(Object o, int defaultValue) {
		if (o == null) return defaultValue;
		if (o instanceof Integer) return ((Integer)o).intValue();
		return str2Int(obj2String(o), defaultValue);
	}
	
	public static Date obj2Date(Object o, String pattern) {
		if (o == null) return null;
		if (o instanceof Date) return (Date)o;
		return str2Date(obj2String(o), pattern);
	}

	public static Boolean obj2Boolean(Object o) {
		if (o == null) return null;
		if (o instanceof Boolean) return (Boolean)o;
		return obj2Boolean(o, false);
	}

	public static boolean obj2Boolean(Object o, boolean defaultValue) {
		if (o == null) return defaultValue;
		if (o instanceof Boolean) return ((Boolean)o).booleanValue();
		return str2Boolean(obj2String(o), defaultValue);
	}
}
