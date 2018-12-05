/**
 * 
 * created on 2007-1-14
 */
package net.ninecube.core.query.hibernate;

import java.io.Serializable;

import net.ninecube.core.query.QueryException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.type.ComponentType;
import org.hibernate.type.Type;

/**
 * 
 * @author jxb
 * 
 */
public class HibernateUtil {
	
	public static void setPrimaryKeyValue(SessionFactory sessionFactory, Object o, Object value){
		try{
			BeanUtils.setProperty(o,getPrimaryKeyName(sessionFactory, o.getClass()),value);
		}catch(Exception exc){
			throw new IllegalArgumentException(exc.getMessage(), exc);
		}
	}
	
	public static String getPrimaryKeyValue(SessionFactory sessionFactory, Object o){
		try{
			return BeanUtils.getProperty(o,getPrimaryKeyName(sessionFactory, o.getClass()));
		}catch(Exception exc){
			throw new IllegalArgumentException(exc.getMessage(), exc);
		}
	}
	
	public static String getPrimaryKeyName(SessionFactory sessionFactory, Class c){
		ClassMetadata metadata = sessionFactory.getClassMetadata(c);
		if (metadata == null) {
			Class c1 = getUnproxyClass(c);
			if (c1 != null) metadata =  sessionFactory.getClassMetadata(c1);
		}
		if (metadata == null) throw new QueryException("can't get metadata for entity class: '" + c.getName() + "'");
		return metadata.getIdentifierPropertyName();
	}
	
	/**
	 * 如果c是一个被cglib代理后的类，则偿试找出其真实类，如果找不到则返回null
	 * TODO: 是否有其它更好的方法能简单地找出被cglib所代理的真实类？
	 */
	protected static Class getUnproxyClass(Class c) {
		String className = c.getName();
		int i = className.indexOf("$$");
		if (i >= 0) {
			className = className.substring(0, i);
			try {
				return Class.forName(className);
			} catch (ClassNotFoundException e) {
				return null;
			}
		}
		return null;
	}
	
	public static Serializable getSerializablePrimaryKeyValue(SessionFactory sessionFactory, Object o) {
		try{
			Object ret = PropertyUtils.getProperty(o,getPrimaryKeyName(sessionFactory, o.getClass()));
			return (Serializable)ret;
		}catch(Exception exc){
			throw new IllegalArgumentException(exc.getMessage(), exc);
		}
	}

	public static Type getType(SessionFactory sessionFactory, Class c, String paramName) throws HibernateException
	{
		ClassMetadata metadata = sessionFactory.getClassMetadata(c);
		if (metadata == null) throw new QueryException("can't get metadata for entity class: '" + c.getName() + "'");
		//id
		if (metadata.getIdentifierPropertyName().equals(paramName))
			return metadata.getIdentifierType();
		//collection property: map field, like "properties['email']", collection property
		if (paramName.matches("[a-zA-Z][a-zA-Z0-9]*\\[.+\\]"))
		{
			String propertyName = paramName.substring(0, paramName.indexOf("["));
			CollectionMetadata cmd = sessionFactory.getCollectionMetadata(c.getName()+"."+propertyName);
			return cmd.getElementType();
		}
		//nested property: like "foo.bar.name", "foo.tao.first"
		else if (paramName.matches("([a-zA-Z][a-zA-Z0-9]*\\.)+[a-zA-Z][a-zA-Z0-9]*"))
		{
			int index = paramName.indexOf(".");
			String property = paramName.substring(0, index);
			String nestedProperty = paramName.substring(index+1);
			Type type = metadata.getPropertyType(property);
			//entity property
			if (type.isEntityType()) {
				return getType(sessionFactory, type.getReturnedClass(), nestedProperty);
			}
			//component property
			else if (type.isComponentType()) {
				return getComponentPropertyType((ComponentType)type, nestedProperty);
			}
		}
		return metadata.getPropertyType(paramName);
	}
	
	private static Type getComponentPropertyType(ComponentType type, String property) 
	{
		for (int i = 0; i < type.getSubtypes().length; i++) {
			if (type.getPropertyNames()[i].equals(property)) {
				return type.getSubtypes()[i];
			}
		}
		throw new net.ninecube.core.query.QueryException("can't get type of property: "+type.getName()+"."+property);
	}
}
