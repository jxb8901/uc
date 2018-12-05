/**
 * 
 * created on 2007-1-12
 */
package net.ninecube.core.domain;

import java.util.List;
import java.util.Map;

import net.ninecube.core.query.Page;
import net.ninecube.core.query.QueryCondition;

/**
 * 
 * @see net.ninecube.core.query.QueryManager
 * @author jxb
 * 
 */
public interface EntityQueryManager<T extends Entity> {

	public List<T> getAll();
	
	public Page find(int pageNum, int pageSize);
	
	public Page find(List<? extends QueryCondition> conditions, 
			Map<String, Object> parameters, 
			int pageNum, int pageSize);
}
