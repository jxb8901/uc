
package net.ninecube.core.query.hibernate;

import java.util.List;

import net.ninecube.core.query.QueryCondition;
import net.ninecube.core.query.QueryManager;
import net.ninecube.core.query.QueryObject;

import org.hibernate.SessionFactory;

/**
 * 通用查询接口的hibernate实现,支持标准特性外,还支持:
 * 1.Map属性查询,如: properties['email']
 * @author jxb
 */
public class HibernateQueryManager implements QueryManager
{
	public QueryObject getQueryObject(Class entityClass, List<? extends QueryCondition> conditions) {
		return new HibernateQueryObject(sessionFactory, entityClass, conditions);
	}
	
	//~=====================================
	
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}
}
