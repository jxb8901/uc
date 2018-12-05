package net.ninecube.venus.domain;

import net.ninecube.core.domain.Entity;
import net.ninecube.core.domain.GenericEntityManager;
import net.ninecube.core.util.OpenSessionInViewFilterSimulator;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @author  jxb
 */
public abstract class AbstractEntityManagerTestCase extends
net.ninecube.test.AbstractEntityManagerTestCase {

	@Override
	protected String[] getConfigLocations() {
		return new String[]{
				"classpath*:applicationContext-test.xml",
				"classpath*:applicationContext-hibernate.xml",
				"classpath*:applicationContext-transaction.xml",
				"classpath*:applicationContext-rule-test.xml",
				"classpath*:applicationContext-beans.xml",
		};
	}

}
