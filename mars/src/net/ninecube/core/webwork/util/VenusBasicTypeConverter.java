/**
 * 
 * created on 2007-3-29
 */
package net.ninecube.core.webwork.util;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.ninecube.core.domain.Entity;
import net.ninecube.core.domain.EntityManagerFactory;
import net.ninecube.core.query.hibernate.HibernateUtil;
import net.ninecube.lang.Frequence;
import net.ninecube.util.DateUtil;
import net.ninecube.util.FileUtil;
import net.ninecube.util.StringUtil;
import ognl.Ognl;
import ognl.TypeConverter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;

import com.opensymphony.xwork.XworkException;
import com.opensymphony.xwork.util.XWorkBasicConverter;
import com.opensymphony.xwork.util.XWorkConverter;

/**
 * 默认类型转换器，增加以下转换：
 * 增加File对象到byte[],String的转换
 * 增加String对象到JDK5的枚举类型的转换
 * 增加Entity对象与String之间的互相转换
 * 增加Collection到String的转换
 * 增加String到Frequence的转换
 * 
 * 修改了xwork的如下转换形为：
 * 集合转换方式：xwork执行Collection到Collection的转换时，
 * 		实际并未转换集合内的元素。
 * 到Date的转换：修改为使用固定的格式字符串格式化日期
 *  Date到字符串的转换：修改为使用固定的格式字符串格式化日期
 * 
 * @author jxb
 * @TODO: 文件上传大小及文件类型控制
 * @TODO: 日期时间格式化字符串采用外部配置的方式
 */
public class VenusBasicTypeConverter extends XWorkBasicConverter {
	private static final Log log = LogFactory.getLog(VenusBasicTypeConverter.class);
	private static final String DATE_FORMAT = "yyyyMMdd";
	private static final String TIME_FORMAT = "HHmmss";
	private static final String DATETIME_FORMAT = DATE_FORMAT; // + " " +TIME_FORMAT;
	private SessionFactory sf;

	public Object convertValue(Map context, Object o, Member member, String s, Object value, Class toType) {
		try {
			Object ret = convertValue1(context, o, member, s, value, toType);
			if (ret != null) return ret;
			return super.convertValue(context, o, member, s, value, toType);
		} catch (RuntimeException e) {
			log.error(e.getMessage(), e);
			throw e;
		}
	}
	
	public Object convertValue1(Map context, Object o, Member member, String s, Object value, Class toType) {
		// TODO: 如果转换抛出异常，webwork如何处理？
		log.debug("**convert '"+s+"' from '" + (value != null ? value.getClass() : null) + "' to '" + toType + "'");
		/* File -> byte[] */
		if (value instanceof File && toType == byte[].class) {
			return doConvertFileToByteArray((File) value);
		}else if (value instanceof File[] && toType == byte[].class) {
			return doConvertFileToByteArray(((File[]) value)[0]);
		} 
		/* File -> String */
		else if (value instanceof File && toType == String.class) {
			return new String(doConvertFileToByteArray((File) value));
		} 
		else if (value instanceof File[] && toType == String.class) {
			return new String(doConvertFileToByteArray(((File[]) value)[0]));
		} 
		/* String -> Frequence */
		else if (Frequence.class == toType && value instanceof String) {
			return Frequence.get((String)value);
		}
		/* String -> Enum */
		else if (toType.isEnum() && value instanceof String) {
			return doConvertStringToEnum(toType, (String)value);
		}
		/* String -> Date */
		else if (value instanceof String && Date.class.isAssignableFrom(toType)) {
			return doConvertStringToDate((String)value, toType);
		}
		else if (value instanceof Date && toType == String.class) {
			return doConvertDateToString((Date)value);
		}
		/* Serializable -> Entity */
//		else if (value instanceof Serializable && Entity.class.isAssignableFrom(toType)) {
//			return doConvertSerializableToEntity((Serializable)value, toType);
//		}
		/* Entity -> String */
//		else if (value instanceof Entity && String.class.equals(toType)) {
//			return HibernateUtil.getPrimaryKeyValue(sf, value);
//		}
		/* Collection -> String */
		else if(value instanceof Collection && toType == String.class){
			return doConvertCollectionToString(context, o, member, s, value, toType);
		} 
		/* -> Collection */
		else if (Collection.class.isAssignableFrom(toType)) {
			return doConvertToCollection(context, o, member, s, value, toType);
        }
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private Object doConvertSerializableToEntity(Serializable value, Class toType) {
		log.debug("convert to entity : " + toType);
		Entity r = EntityManagerFactory.get().getEntityManager(toType).load(value);
		// Entity r = EntityManagerFactory.get().getEntityManager(toType).create();
		HibernateUtil.setPrimaryKeyValue(sf, r, value);
		log.debug("convert result , type :" + r.getClass() + " ; value : " + r);
		return r;
	}
	
	private String doConvertCollectionToString(Map context, Object o, Member member, String s, Object value, Class toType) {
		log.debug("convert collection to String !!!");
		Collection c = (Collection)value;
		String r = "";
		int i = 0 ;
		for(Iterator it = c.iterator(); it.hasNext() ; i++ ){
			if(i > 0 ) r += ",";
			r += convertValue1(context, o, member, s , it.next(), String.class);
		}
		return r;
	}
	
	@SuppressWarnings("unchecked")
	private Object doConvertStringToEnum(Class clazz, String obj) {
		return Enum.valueOf(clazz, obj);
	}

	private byte[] doConvertFileToByteArray(File value) {
		try {
			return FileUtil.getFileBytes(value);
		} catch (IOException e) {
			throw new XworkException(e.getLocalizedMessage(), e);
		}
	}
	
    /**
	 * Copy from XworkBasicConverter,
	 */
	@SuppressWarnings("unchecked")
	private Collection doConvertToCollection(Map context, Object o,
			Member member, String prop, Object value, Class toType) {
		log.debug("convert to collection: from '" + (value==null?null:value.getClass()) + "' to '" + toType+"'");
		
		Collection result;
		Class memberType = String.class;

		if (o != null) {
			// memberType = (Class)
			// XWorkConverter.getInstance().getConverter(o.getClass(),
			// XWorkConverter.CONVERSION_COLLECTION_PREFIX + prop);
			memberType = XWorkConverter.getInstance().getObjectTypeDeterminer()
					.getElementClass(o.getClass(), prop, null);

			if (memberType == null) {
				memberType = String.class;
			}
		}

		// 修改点：尽量对value做类型转换
		/*if (toType.isAssignableFrom(value.getClass())) {
			// no need to do anything
			result = (Collection) value;
		} else */if (value.getClass().isArray()) {
			Object[] objArray = (Object[]) value;
			TypeConverter converter = Ognl.getTypeConverter(context);
			result = createCollection(o, prop, toType, memberType,
					objArray.length);

			for (int i = 0; i < objArray.length; i++) {
				result.add(converter.convertValue(context, o, member, prop,
						objArray[i], memberType));
			}
		} else if (Collection.class.isAssignableFrom(value.getClass())) {
			Collection col = (Collection) value;
			TypeConverter converter = Ognl.getTypeConverter(context);
			result = createCollection(o, prop, toType, memberType, col.size());

			for (Iterator it = col.iterator(); it.hasNext();) {
				result.add(converter.convertValue(context, o, member, prop, it
						.next(), memberType));
			}
		} else {
			result = createCollection(o, prop, toType, memberType, -1);
			//更改点，集合中的数据也要进行转换。
			Object v =Ognl.getTypeConverter(context).convertValue(context, o, member, prop, value, memberType);
			log.debug("convert result value : " + v + " ; class : " + v.getClass());
			result.add(v);
		}

		log.debug("converted. result=" + result);
		return result;
	}

    /**
	 * Copy from XworkBasicConverter,
	 */
	@SuppressWarnings("deprecation")
	private Collection createCollection(Object fromObject, String propertyName,
			Class toType, Class memberType, int size) {
		// try {
		// Object original =
		// Ognl.getValue(OgnlUtil.compile(propertyName),fromObject);
		// if (original instanceof Collection) {
		// Collection coll = (Collection) original;
		// coll.clear();
		// return coll;
		// }
		// } catch (Exception e) {
		// // fail back to creating a new one
		// }

		Collection result;

		if (toType == Set.class) {
			if (size > 0) {
				result = new HashSet(size);
			} else {
				result = new HashSet();
			}
		} else if (toType == SortedSet.class) {
			result = new TreeSet();
		} else {
			if (size > 0) {
				result = new com.opensymphony.xwork.util.XWorkList(memberType, size);
			} else {
				result = new com.opensymphony.xwork.util.XWorkList(memberType);
			}
		}

		return result;
	}
	
	private String doConvertDateToString(Date value) {
		if (value == null) return "";
		return DateUtil.format(value, getDatePattern(value.getClass()));
	}

    private Date doConvertStringToDate(String value, Class toType) {
		if (StringUtil.isEmpty(value)) return null;

		String pattern = getDatePattern(toType);
		Date result = DateUtil.parseDate(value, pattern);
		if (result == null) throw new XworkException("Could not parse date: '" + 
				value + "' with pattern: '" + pattern + "'");

		if (toType == java.sql.Date.class) return new java.sql.Date(result.getTime());
		if (toType == java.sql.Time.class) return new java.sql.Time(result.getTime());
		if (toType == java.sql.Timestamp.class) return new java.sql.Timestamp(result.getTime());
		return result;
	}
    
    private String getDatePattern(Class type) {
    	String pattern;
		if (java.sql.Time.class == type) {
			pattern = TIME_FORMAT;
		} else if (java.sql.Timestamp.class == type) {
			pattern = DATETIME_FORMAT;
		} else {
			pattern = DATE_FORMAT;
		}
		return pattern;
    }

	public void init() {
		XWorkConverter.getInstance().setDefaultConverter(this);
	}
	
	public void setSessionFactory(SessionFactory sf){
		this.sf = sf;
	}
}
