/**
 * 
 * created on 2006-12-15
 */
package net.ninecube.venus;

import junit.framework.TestCase;
import net.ninecube.core.config.FieldConf;
import net.ninecube.core.config.FieldTagType;
import net.ninecube.core.config.TransactionConf;
import net.ninecube.core.config.WebConfig;
import net.ninecube.core.config.WebConfigManager;
import net.ninecube.core.config.impl.TransactionConfImpl;
import net.ninecube.core.config.impl.WebConfigManagerImpl;

/**
 * 
 * @author jxb
 * 
 */
public class ConfigFileTest extends TestCase {
	
	protected WebConfig loadConfig(String fileName) {
		WebConfigManager cm = new WebConfigManagerImpl(fileName);
		cm.init();
		WebConfig config = cm.getConfiguration();
		return config;
	}
	
	public void testGetConfiguration() {
		
	} 
	
	
}
