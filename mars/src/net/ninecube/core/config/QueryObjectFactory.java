/**
 * 
 * created on 2007-1-13
 */
package net.ninecube.core.config;

import net.ninecube.core.query.QueryObject;

/**
 * 
 * @author jxb
 * 
 */
public interface QueryObjectFactory {

	public QueryObject getQueryObject(ActionConf transaction);
	
}
