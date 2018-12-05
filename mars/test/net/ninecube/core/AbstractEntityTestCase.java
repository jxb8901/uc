/*
 * Created on 2004-7-17
 *
 */
package net.ninecube.core;

import org.hibernate.SessionFactory;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

/**
 * @author jxb
 */
public abstract class AbstractEntityTestCase extends AbstractTransactionalDataSourceSpringContextTests {
	
	protected SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	protected String[] getConfigLocations() {
		return new String[]{
				"classpath*:applicationContext-test.xml",
				"classpath*:applicationContext-hibernate.xml",
		};
	}
}
