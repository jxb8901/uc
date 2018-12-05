/**
 * 
 * created on 2006-12-14
 */
package net.ninecube.core.config;

import net.ninecube.core.config.impl.WebConfigManagerImpl;


/**
 * 
 * @author jxb
 * 
 */
public abstract class WebConfigManager {
	private static WebConfigManager instance = new WebConfigManagerImpl();
	
	public static WebConfigManager get() {
		return instance;
	}
	
	public static void setInstance(WebConfigManager instance) {
		WebConfigManager.instance = instance;
	}
	
	public abstract void init();
	
	public abstract WebConfig getConfiguration();

}
