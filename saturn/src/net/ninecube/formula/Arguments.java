/**
 * created on 2006-4-6
 */
package net.ninecube.formula;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.ninecube.lang.TypedMap;


/**
 * @author JXB
 */
public class Arguments extends TypedMap<String, String> {
	
	private static final String PREFIX_ARGUMENT_INDEX = "ARG_";
	
	//~
	
	public Arguments clone() {
		return (Arguments) super.clone();
	}
	
	//~
	
	public String getArgument(int index) {
		return getString(PREFIX_ARGUMENT_INDEX + index);
	}
	
	public String getArgument(String name) {
		return getString(name);
	}
	
	public void setArgument(String name, String value) {
		put(name, value);
	}
	
	public void setArgument(int index, String value) {
		put(PREFIX_ARGUMENT_INDEX + index, value);
	}
	
	public static boolean isUnnamedArgument(String name) {
		return name.startsWith(PREFIX_ARGUMENT_INDEX);
	}

	/**
	 * 取所有未命名参数（索引参数）
	 */
	public List<String> unnamedArgumentValueIterate() {
		List<String> ret = new ArrayList<String>();
		for (Iterator<Map.Entry<String, String>> it = this.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<String, String> o = it.next();
			if (o.getKey().startsWith(PREFIX_ARGUMENT_INDEX))
				ret.add(o.getValue());
		}
		return ret;
	}
}
