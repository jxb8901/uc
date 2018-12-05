/**
 * 
 * created on 2007-4-27
 */
package net.ninecube.core.util;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 
 * @author jxb
 */
public class OpenSessionInViewFilterSimulator {
	private SessionFactory sessionFactory;
	private Session session;
	private boolean singleSession = true;
	
	public OpenSessionInViewFilterSimulator(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void before() {
		boolean participate = false;

		if (isSingleSession()) {
			// single session mode
			if (TransactionSynchronizationManager.hasResource(sessionFactory)) {
				// Do not modify the Session: just set the participate flag.
				participate = true;
			}
			else {
				session = getSession(sessionFactory);
				TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
			}
		}
		else {
			// deferred close mode
			if (SessionFactoryUtils.isDeferredCloseActive(sessionFactory)) {
				// Do not modify deferred close: just set the participate flag.
				participate = true;
			}
			else {
				SessionFactoryUtils.initDeferredClose(sessionFactory);
			}
		}
	}
	
	public void after() {
		if (isSingleSession()) {
			// single session mode
			TransactionSynchronizationManager.unbindResource(sessionFactory);
			closeSession(session, sessionFactory);
		}
		else {
			// deferred close mode
			SessionFactoryUtils.processDeferredClose(sessionFactory);
		}
	}
	
	protected Session getSession(SessionFactory sessionFactory) throws DataAccessResourceFailureException {
		Session session = SessionFactoryUtils.getSession(sessionFactory, true);
		session.setFlushMode(FlushMode.NEVER);
		return session;
	}
	
	protected void closeSession(Session session, SessionFactory sessionFactory) {
		SessionFactoryUtils.closeSession(session);
	}
	
	//~ getter and setter

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public boolean isSingleSession() {
		return singleSession;
	}

	public void setSingleSession(boolean singleSession) {
		this.singleSession = singleSession;
	}
}
