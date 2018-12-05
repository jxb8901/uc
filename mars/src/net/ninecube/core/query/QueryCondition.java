/**
 * 
 * created on 2007-1-12
 */
package net.ninecube.core.query;

/**
 * 表示一个SQL的查询条件
 * 
 * @TODO: 不能直接支持between比较操作符
 * 
 * @author jxb
 */
public interface QueryCondition {

	/**
	 * 参数名称，用于从参数中取参数值
	 */
	public String getName();
	
	/**
	 * 比较操作符
	 */
	public Operator getOperator();
	
	/**
	 * 实体类的属性，通常为数据库字段名
	 */
	public String getEntityName();
	
}
