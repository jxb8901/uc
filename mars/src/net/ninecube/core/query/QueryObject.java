/**
 * 
 * created on 2007-1-12
 */
package net.ninecube.core.query;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author jxb
 * 
 */
public interface QueryObject {
	
	/**
	 * 返回本查询对象要查询的实体类
	 */
	public Class getEntityClass();
	
	/**
	 * 取查询条件，所有条件间是“and”的关系
	 */
	public List<? extends QueryCondition> getConditions();
	
	/**
	 * @param parameters 查询参数
	 * @param pageNum 页号,从1开始计数
	 * @param pageSize 页大小
	 * @return 结果页
	 */
	public Page find(Map<String, ?> parameters, int pageNum, int pageSize);
	
}
