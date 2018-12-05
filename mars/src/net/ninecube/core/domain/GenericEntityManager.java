package net.ninecube.core.domain;

import org.hibernate.SessionFactory;

public class GenericEntityManager<T extends Entity> extends AbstractEntityManager<T> {

	public GenericEntityManager(SessionFactory sessionFactory, Class<T> entityClass) {
		this.entityClass = entityClass;
		this.setSessionFactory(sessionFactory);
		super.afterPropertiesSet();
	}
}
