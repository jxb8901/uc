/**
 * 
 * created on 2007-1-13
 */
package net.ninecube.core.config.impl;

import java.util.HashMap;
import java.util.Map;

import net.ninecube.core.config.ActionConf;
import net.ninecube.core.config.QueryObjectFactory;
import net.ninecube.core.query.QueryManager;
import net.ninecube.core.query.QueryObject;

/**
 * 
 * @author jxb
 * 
 */
public class QueryObjectFactoryImpl implements QueryObjectFactory {
	private Map<String, QueryObject> cache = new HashMap<String, QueryObject>();
	private QueryManager queryManager;

	public QueryObject getQueryObject(ActionConf config) {
		String key = config.getFullName();
		QueryObject ret = cache.get(key);
		if (ret == null) {
			ret = queryManager.getQueryObject(config.getEntityClass(), config.getQueryFields());
			cache.put(key, ret);
		}
		return ret;
	}

	public void setQueryManager(QueryManager queryManager) {
		this.queryManager = queryManager;
	}	
}
