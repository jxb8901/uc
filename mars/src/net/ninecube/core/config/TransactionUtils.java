/**
 * 
 * created on 2006-12-26
 */
package net.ninecube.core.config;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author jxb
 * 
 */
public class TransactionUtils {

	public static boolean isDefaultAction(String action) {
		return StringUtils.isEmpty(action) || "execute".equals(action) || "default".equals(action);
	}
}
