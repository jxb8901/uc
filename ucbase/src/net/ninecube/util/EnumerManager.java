package net.ninecube.util;

import java.util.Map;

import net.ninecube.util.internal.DefaultEnumerManager;


/**
 * 
 * @author jxb
 * 
 */
public abstract class EnumerManager {
	private static EnumerManager instance;
	
	public static EnumerManager get() {
		if (instance == null) setDefault();
		return instance;
	}

	public static void setDefault() {
		instance = new DefaultEnumerManager().init();
	}

	public static void set(EnumerManager instance) {
		EnumerManager.instance = instance;
	}
	
	public EnumerManager init() {
		return this;
	}

	public abstract Object getValue(String schema, String alias);
	
	public abstract Map<Object, String> getValues(String schema);
}
