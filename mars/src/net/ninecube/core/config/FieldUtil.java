/**
 * 
 * created on 2007-1-23
 */
package net.ninecube.core.config;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ninecube.util.DynamicBean;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author jxb
 * 
 */
public class FieldUtil {
	private static final Log log = LogFactory.getLog(FieldUtil.class);
	private static final Map<String, Class> cache = new HashMap<String, Class>();

	public static DynamicBean newDynamicBean(String className, List<? extends FieldConf> fields) {
		return newDynamicBean(className, fields, null);
	}

	public static DynamicBean newDynamicBean(String className, List<? extends FieldConf> fields, Class prototype) {
		Class c = getDynamicBean(className, fields, prototype);
		try {
			return (DynamicBean) c.newInstance();
		} catch (Exception e) {
			throw new ConfigException("can't create dynamic bean:" + className, e);
		} 
	}

	public static Class getDynamicBean(String className, List<? extends FieldConf> fields) {
		return getDynamicBean(className, fields, null);
	}

	public static synchronized Class getDynamicBean(String className, List<? extends FieldConf> fields, Class prototype) {
		if (cache.containsKey(className)) return cache.get(className);
		Class ret = createDynamicBean(className, fields, prototype);
		cache.put(className, ret);
		return ret;
	}
	
	protected static Class createDynamicBean(String className, List<? extends FieldConf> fields, Class prototype) {
		if (log.isDebugEnabled()) log.debug("create dynamic bean: " + className);
		List<String> fieldNames = new ArrayList<String>();
		List<Class> fieldTypes = new ArrayList<Class>();
		for (FieldConf f : fields) {
			fieldNames.add(f.getName());
			fieldTypes.add(getFieldType(f, prototype));
			if (log.isDebugEnabled()) log.debug("bean field: " + f.getName() + " - " + f.getType().toClass());
		}
		// return DynamicBean.createDynamicBeanWithPublicField(className, fieldNames, fieldTypes);
		return DynamicBean.createDynamicBean(className, fieldNames, fieldTypes);
	}
	
	protected static Class getFieldType(FieldConf field, Class prototype) {
		if (prototype != null) {
			for (PropertyDescriptor pd : PropertyUtils.getPropertyDescriptors(prototype)) {
				if (pd.getName().equals(field.getName())) 
					return pd.getPropertyType();
			}
		}
		return field.getType().toClass();
	}
}
