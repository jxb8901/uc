package net.ninecube.test;

import net.ninecube.core.domain.Entity;
import net.ninecube.core.domain.GenericEntityManager;
import net.ninecube.core.util.OpenSessionInViewFilterSimulator;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

/**
 * @author  jxb
 */
public abstract class AbstractEntityManagerTestCase extends
AbstractTransactionalDataSourceSpringContextTests {
	private OpenSessionInViewFilterSimulator openSessionInView;
	protected SessionFactory sessionFactory;
	private Session session;

	@Override
	protected void prepareTestInstance() throws Exception {
		super.setAutowireMode(AUTOWIRE_BY_NAME);
		super.prepareTestInstance();
	}
	
	@Override
	protected void onSetUpBeforeTransaction() throws Exception {
		openSessionInView = new OpenSessionInViewFilterSimulator(this.sessionFactory);
		openSessionInView.before();
		session = openSessionInView.getSession();
	}

	@Override
	protected void onTearDownAfterTransaction() throws Exception {
		openSessionInView.after();
	}

	
	public <T extends Entity> GenericEntityManager<T> createEntityManager(Class<T> clazz) {
		GenericEntityManager<T> em = new GenericEntityManager<T>(sessionFactory, clazz);
		return em;
	}
	
	public Session getSession() {
		if (this.session == null)
			this.session = this.sessionFactory.getCurrentSession();
		return session;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
