/**
 * 
 * created on 2007-2-5
 */
package net.ninecube.util.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.ninecube.lang.BaseException;
import net.ninecube.util.EnumerManager;
import net.ninecube.util.XmlUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

/**
 * 
 * @author jxb
 * 
 */
public class DefaultEnumerManager extends EnumerManager {
	private static Log log = LogFactory.getLog(DefaultEnumerManager.class);
	private Set<String> includedFileNames = new TreeSet<String>();
	protected Map<String, EnumerMap> enumers = new HashMap<String, EnumerMap>();
	private String configFileName = "enumer.xml";
	
	public DefaultEnumerManager() { }
	
	public DefaultEnumerManager(String configFileName) {
		this.configFileName = configFileName;
	}
	
	public DefaultEnumerManager init() {
		loadEnumers(getConfigElement(configFileName));
		return this;
	}

	@Override
	public Object getValue(String schema, String alias) {
		String defaultValue = schema + "." + alias + ":not found";
		EnumerMap enumer = getEnumer(schema);
		if (enumer == null) return defaultValue;
		if (enumer.enumer.containsKey(alias)) return alias;
		Object ret = enumer.reverse.get(alias);
		if (ret == null) return defaultValue;
		return ret;
	}
	
	@Override
	public Map<Object, String> getValues(String schema) {
		return getEnumer(schema).enumer;
	}

	protected EnumerMap getEnumer(String schema) {
		do {
			if (enumers.containsKey(schema)) return enumers.get(schema);
			int index = schema.indexOf(".");
			if (index == -1) return null;
			schema = schema.substring(index+1);
		} while (true);
	}
	
	@SuppressWarnings("unchecked")
	private void loadEnumers(Element e) {
		List<Element> ch = e.elements();
		for (Element e1 : ch) {
			if ("enum".equals(e1.getName())) {
				loadEnumer(e1);
			}
			else if ("include".equals(e1.getName())) {
				Element include = getConfigElement(e1.attributeValue("file"));
				if (include != null) {
					loadEnumers(include);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void loadEnumer(Element e) {
		if (enumers.containsKey(e.attributeValue("name")))
			throw new BaseException("duplication enum name:" + e.attributeValue("name"));
		EnumerMap enumer = new EnumerMap();
		enumers.put(e.attributeValue("name"), enumer);
		Class toClass = null;
		if(e.attributeValue("javaclass") != null)
			try {
				toClass = Class.forName(e.attributeValue("javaclass"));
			} catch (ClassNotFoundException exc) {log.error(exc.getMessage(), exc);}
		List<Element> ch = e.elements();
		for (Element e1 : ch) {
			Object val = e1.attributeValue("value");
//			if(toClass != null)
//				val = ConvertUtils.convert((String)val, toClass);
			enumer.enumer.put(val, e1.attributeValue("desc"));
			enumer.reverse.put(e1.attributeValue("desc"), val);
		}
	}
	
	protected static class EnumerMap {
		Map<Object, String> enumer = new HashMap<Object, String>();
		Map<String, Object> reverse = new HashMap<String, Object>();
	}

	protected Element getConfigElement(String fileName) {
		if (includedFileNames.contains(fileName)) return null;
		includedFileNames.add(fileName);
		return XmlUtil.getConfigElement(fileName, "-//ninecube//enumer1.0//EN", "enumer.dtd");
	}
}
