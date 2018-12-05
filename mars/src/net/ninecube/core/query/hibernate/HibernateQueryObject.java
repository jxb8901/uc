/*
 * Created on 2004-7-17
 *
 */
package net.ninecube.core.query.hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ninecube.core.query.Operator;
import net.ninecube.core.query.Page;
import net.ninecube.core.query.QueryCondition;
import net.ninecube.core.query.QueryException;
import net.ninecube.core.query.QueryObject;
import net.ninecube.core.webwork.util.VenusBasicTypeConverter;
import net.ninecube.util.StringUtil;
import ognl.TypeConverter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.Type;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.opensymphony.xwork.util.XWorkBasicConverter;

/**
 * @TODO: 去掉对ognl和xwork的依赖
 * @author jxb
 */
public class HibernateQueryObject implements QueryObject {
	private static final Log log = LogFactory.getLog(HibernateQueryObject.class);
	private static final String ENTITY_ALIAS = "a";
	// 默认类型转换器
	private TypeConverter typeConverter = new VenusBasicTypeConverter();
	private SessionFactory sessionFactory;
	private HibernateTemplate hibernateTemplate;
	private Class entityClass;
	private List<? extends QueryCondition> conditions;
	private Map<String, Type> fieldTypes = new HashMap<String, Type>();
	
	public HibernateQueryObject(SessionFactory sessionFactory, Class entityClass, 
			List<? extends QueryCondition> conditions)
	{		
		this.sessionFactory = sessionFactory;
		this.hibernateTemplate = new HibernateTemplate(sessionFactory);
		this.conditions = conditions == null ? new ArrayList<QueryCondition>() : conditions;
		this.entityClass = entityClass;

		log.debug("query object - entity class: '" + this.entityClass.getName() + "'");
		log.debug("query object - conditions: " + this.conditions);
		
		this.init();
	}
	
	//~ implements QueryObject

	public List<? extends QueryCondition> getConditions() {
		return this.conditions;
	}

	public Class getEntityClass() {
		return this.entityClass;
	}

	public Page find(Map<String, ?> parameters, int pageNum, int pageSize)
	{
		if (parameters == null) parameters = new HashMap<String, Object>();
		Page ret = new Page(pageNum, pageSize);
		StringBuffer where = new StringBuffer();
		StringBuffer orderBy = new StringBuffer();
		getWhere(parameters, where, orderBy);
		ret.setRecords(list(getQueryHql(where, orderBy), parameters, 
				ret.getStartIndexFrom0(), pageSize), 
				count(getCountHql(where), parameters));
		return ret;
	}
	
	public void setTypeConverter(TypeConverter typeConverter)
	{
		this.typeConverter = typeConverter;
	}
	
	private boolean isIgnoreCondition(QueryCondition condition, Map<String, ?> parameters) {
		if (condition.getOperator().isOrderOperator()) return true;
		Object o = parameters.get(condition.getName());
		return o == null || StringUtil.isEmpty(o);
	}
	
	//~
	
	private List list(final String hql,
			final Map<String, ?> parameters, 
			final int startIndex, final int count)
	{
		return hibernateTemplate.executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query queryObject =  session.createQuery(hql);
				setQueryParameter(queryObject, parameters);
				queryObject.setFirstResult(startIndex);
				queryObject.setMaxResults(count);
				return queryObject.list();
			}
		});
	}
	
	private int count(final String hql, final Map<String, ?> parameters)
	{
		List ret = hibernateTemplate.executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query queryObject = session.createQuery(hql);
				setQueryParameter(queryObject, parameters);
				return queryObject.list();
			}
		});
		return ((Integer)ret.get(0)).intValue();
	}
	
	//~=======================
		
	private String getQueryHql(StringBuffer where, StringBuffer orderBy)
	{
		StringBuffer ret = new StringBuffer();
		ret.append("from ").append(this.entityClass.getName());
		ret.append(" ").append(ENTITY_ALIAS);
		ret.append(" ").append(where.toString());
		ret.append(" ").append(orderBy.toString());
		return ret.toString();
	}
	
	private String getCountHql(StringBuffer where)
	{
		StringBuffer ret = new StringBuffer();
		ret.append("select count(*) from ").append(this.entityClass.getName());
		ret.append(" ").append(ENTITY_ALIAS);
		ret.append(" ").append(where.toString());
		return ret.toString();
	}
	
	private void getWhere(Map<String, ?> parameters, StringBuffer where, StringBuffer orderBy) 
	{
		boolean hasOrderBy = false;
		
		where.append("where 1=1");
		orderBy.append("order by ");
		for (QueryCondition c : this.conditions)
		{
			if (c.getOperator().isOrderOperator()) {
				orderBy.append(ENTITY_ALIAS).append(".").append(c.getEntityName()).append(" ");
				orderBy.append(c.getOperator().toSql()).append(",");
				hasOrderBy = true;
			}
			else {
				if (isIgnoreCondition(c, parameters)) continue;
				where.append(" and ").append(ENTITY_ALIAS).append(".").append(c.getEntityName());
				where.append(" ").append(c.getOperator().toSql()).append(" ?");
			}
		}
		if (hasOrderBy) {
			orderBy.setLength(orderBy.length()-1);
		}
		else {
			orderBy.setLength(0);
		}
	}
	
	private void setQueryParameter(Query query, Map<String, ?> parameters) 
	{
		int j = 0;
		for (QueryCondition f : this.conditions) {
			if (isIgnoreCondition(f, parameters)) continue;
			Object val = typeConverter.convertValue(new HashMap(), null, null, null, 
					parameters.get(f.getName()), fieldTypes.get(f.getEntityName()).getReturnedClass());
			if (Operator.like.equals(f.getOperator())) {
				val = "%"+val+"%";
			}
			query.setParameter(j++, val, fieldTypes.get(f.getEntityName()));
		}
	}
	
	private void init() {
		for (QueryCondition f : this.conditions) {
			if (fieldTypes.containsKey(f.getEntityName())) continue;
			Type type = HibernateUtil.getType(this.sessionFactory, this.entityClass, f.getEntityName());
			if (type == null) throw new QueryException("field type is null: '"+this.entityClass+"-" + f.getEntityName() + "'");
			fieldTypes.put(f.getEntityName(), type);
		}
	}
}
