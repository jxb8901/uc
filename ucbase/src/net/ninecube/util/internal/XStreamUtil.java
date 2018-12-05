/**
 * 
 * created on 2007-6-27
 */
package net.ninecube.util.internal;

import net.ninecube.lang.TypedMap;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.collections.MapConverter;

/**
 * 
 * @author jxb
 */
public class XStreamUtil {
	private static final XStream xstream = new XStream();
	
	static {
		xstream.registerConverter(new MapConverter(xstream.getMapper()) {
			@Override
			public boolean canConvert(Class type) {
				if (TypedMap.class.equals(type)) return true;
				return super.canConvert(type);
			}
		});
	}

	public static String obj2xml(Object o) {
		return xstream.toXML(o);
	}

	public static Object xml2obj(String s) {
		return xstream.fromXML(s);
	}

}
