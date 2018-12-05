package net.ninecube.core.domain;

import org.hibernate.SessionFactory;


public class ReadonlyEntityManager<T extends Entity> extends GenericEntityManager<T> {
	private static final String msg = "can't update entity object with ReadonlyEntityManager!" + 
			"please use concrete entity manager!";
	
	public ReadonlyEntityManager(SessionFactory sessionFactory, Class<T> entityClass) {
        	super(sessionFactory, entityClass);
    }

	@Override
	protected T onDelete(T entity) {
		throw new IllegalStateException(msg);
	}

	@Override
	protected T onSave(T entity) {
		throw new IllegalStateException(msg);
	}
}
