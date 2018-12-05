
package net.ninecube.core.query;

import java.util.List;

/**
 * 通用的查询接口,支持如下特性:
 * 1.仅支持单一实体的查询
 * 2.不支持实体关联查询
 * 3.可指定任意排序字段及排序顺序(字段名前加"_"表示降序,默认升序)
 * 4.支持的比较操作符有:
 *    =,>,>=,<,<= 在字段名前加上相应的符号即可
 *    like 在字段名前加上"%"符号
 *    没有上述前缀的字段名将被忽略
 * 5.自动类型转换.
 * 6.分页
 * @TODO: 7.或条件的支持(or)
 * @TODO: 8.in条件的支持(in)
 * @TODO:使用范型
 * @TODO:考虑改成根据查询配置数据执行数据库查询
 * @author jxb
 */
public interface QueryManager
{
	
	public QueryObject getQueryObject(Class entityClass, List<? extends QueryCondition> conditions);
	
}
