package net.ninecube.core.domain;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.ninecube.core.exception.EntityException;
import net.ninecube.core.exception.EntityIsUsingException;
import net.ninecube.core.query.Page;
import net.ninecube.core.query.QueryCondition;
import net.ninecube.core.query.QueryManager;
import net.ninecube.core.query.hibernate.HibernateUtil;
import net.ninecube.util.GenericsUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public abstract class AbstractEntityManager<T extends Entity> extends HibernateDaoSupport implements EntityManager<T> {
	private static final Log LOG = LogFactory.getLog(AbstractEntityManager.class);
	protected Class<T> entityClass;
	
	protected QueryManager queryManager;
	
	@SuppressWarnings("unchecked")
	public AbstractEntityManager() {
        entityClass = GenericsUtils.getGenericClass(getClass());
    }
	
	protected Class<T> getEntityClass() {
		return entityClass;
//		String className = this.getClass().getName();
//		String subClassName = className.substring(0, className.length() - className.lastIndexOf("Manager"));
//		try {
//			return Class.forName(subClassName);
//		} catch (ClassNotFoundException e) {
//			LOG.debug("can't find entity class:" + subClassName, e);
//			return null;
//		}
	}
	
	public Serializable getId(T entity) {
		return HibernateUtil.getSerializablePrimaryKeyValue(this.getSessionFactory(), entity);
	}
	
	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		return super.getHibernateTemplate().find("from "+getEntityClass().getName() + 
				" order by " + HibernateUtil.getPrimaryKeyName(this.getSessionFactory(), 
						getEntityClass()) + " desc");
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findByNamedQuery(String queryName, String[] paramNames, Object[] values) {
		return super.getHibernateTemplate().findByNamedQueryAndNamedParam(queryName, paramNames, values);
	}

	@SuppressWarnings("unchecked")
	public List<T> findByNamedQuery(String queryName, String paramName, Object value) {
		return super.getHibernateTemplate().findByNamedQueryAndNamedParam(queryName, paramName, value);
	}
	
	public T getByNamedQuery(String queryName, String paramName, Object value) {
		return getFromList(this.findByNamedQuery(queryName, paramName, value));
	}
	
	public T getByNamedQuery(String queryName, String[] paramNames, Object[] values) {
		return getFromList(this.findByNamedQuery(queryName, paramNames, values));
	}
	
	private T getFromList(List<T> list) {
		if (list.size() == 0) return null;
		return list.get(0);
	}
	
	public Page find(int pageNum, int pageSize) {
		return find(null, null, pageNum, pageSize);
	}
	
	public Page find(List<? extends QueryCondition> conditions, 
			Map<String, Object> parameters, 
			int pageNum, int pageSize) {
		return queryManager.getQueryObject(getEntityClass(), conditions).find(parameters, pageNum, pageSize);
	}
	
	public T create() {
		try {
			return getEntityClass().newInstance();
		} catch (Exception e) {
			throw new EntityException("can't get instance of Entity", e);
		}
	}
	
	public final void delete(Serializable id) {
		delete(getById(id));
	}

	public final void delete(T entity) {
		try {
			super.getHibernateTemplate().delete(onDelete(entity));
			super.getHibernateTemplate().flush();
		} catch (DataIntegrityViolationException e) {
			throw new EntityIsUsingException(entity, e);
		}
	}
	
	protected T onDelete(T entity) {
		return entity;
	}

	/**
	 * 这里设成final方法，以提醒本方法不能覆盖，否则使用AspectJ的pointcut
	 * 模式时将无法拦截本方法，如果需要在保存前执行某些操作可覆盖onSave方法
	 */
	public final void save(T entity) {
		super.getHibernateTemplate().save(onSave(entity));
	}
	
	protected T onSave(T entity) {
		return entity;
	}

	@SuppressWarnings("unchecked")
	public T getById(Serializable id) {
		return (T)super.getHibernateTemplate().get(getEntityClass(), id);
	}
	
	@SuppressWarnings("unchecked")
	public T load(Serializable id) {
		return (T) super.getHibernateTemplate().load(getEntityClass(), id);
	}
	
	public T load(T entity, Serializable id) {
		super.getHibernateTemplate().load(entity, id);
		return entity;
	}

	public void setQueryManager(QueryManager queryManager) {
		this.queryManager = queryManager;
	}

}
