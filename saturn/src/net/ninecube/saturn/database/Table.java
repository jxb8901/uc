/**
 * 
 * created on 2006-11-30
 */
package net.ninecube.saturn.database;

import java.util.List;
import java.util.Set;

import org.codehaus.jrc.predicate.Predicate;
import org.codehaus.jrc.relation.Relation;

/**
 * 
 * @author jxb
 * 
 */
public interface Table {

	public Database getOwner();
	
	/**
	 * 按字段名或别名取字段对象，参数中的名称可以为字段别名也可以为字段名
	 * @param name 别名或字段名，通常为"性别"这样的形式，但对于动态字段
	 * 		则使用"出生日期.年月"的形式，即宿主字段名和动态字段名
	 * 		间用逗号隔开
	 * @reutrn 返回相应别的Comumn对象，未找到别名定义返回null
	 */
	public Column getTableColumnByAlias(String name);

	public String getName();
	
	public List<String> getAlias();
	
	public boolean hasAlias(String name);

	public Set<? extends Association> getAssociations();
	
	public Column getPrimaryKey();
	
	public boolean isCentric();
	
	public boolean isBase();
	/**
	 * 
	 * @return 如果ifBase()为true，则返回相关的日期字段，否则返回空。
	 */
	public Column getDateColumn();
	
	public Relation getTableRelation();
	
	public Predicate getWhere();

}
