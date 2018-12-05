/**
 * 
 * created on 2006-11-30
 */
package net.ninecube.saturn.database;

import java.util.List;

import org.codehaus.jrc.expression.Expression;

/**
 * 
 * @author jxb
 * 
 */
public interface Column {

	public Table getOwner();

	public String getName();

	public List<String> getAlias();

	public boolean hasAlias(String name);

	public Expression getSqlCode();

	/**
	 * @TODO:根据数据库字段类型返回不同的数据值 如果val的类型是Expression则返回val； 返回与字段SqlType相适应的数据值
	 *                         如果type是enumer，则返回转换后的数据库值
	 *                         如果SqlType是varchar，则返回Expression.literal(val)
	 *                         如果SqlType是date，则将val格式化成yyyy-MM-dd形式的字符串，返回相应Expression
	 */
	public Expression getSqlValue(Object val);

	public String getFullName();

}
