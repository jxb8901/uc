package net.ninecube.venus.domain;

import java.util.Date;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

/**
 * @author  Fox
 */
public abstract class CreateVenusTables extends AbstractTransactionalDataSourceSpringContextTests {
	protected LocalSessionFactoryBean sessionFactory;

	public void setSessionFactory(LocalSessionFactoryBean sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	protected String[] getConfigLocations() {
		return new String[]{
				"classpath*:applicationContext-mysql.xml",
				"classpath*:applicationContext-hibernate.xml",
				"classpath*:applicationContext-transaction.xml"
		};
	}
	
	public static void create() throws Exception {
		CreateVenusTables c = new CreateVenusTables(){};
		c.setUp();
//		c.sessionFactory.dropDatabaseSchema();
//		c.sessionFactory.createDatabaseSchema();
		c.sessionFactory.updateDatabaseSchema();
		c.setComplete();
		c.tearDown();
	}

	public static void main(String[] a) throws Exception {
		create();
	}
}
