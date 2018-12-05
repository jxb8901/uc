/**
 * 
 * created on 2007-1-14
 */
package net.ninecube.core.query;

import net.ninecube.core.AbstractEntityTestCase;
import net.ninecube.core.Foo;
import net.ninecube.core.query.hibernate.HibernateUtil;

import org.hibernate.Hibernate;

/**
 * 
 * @author jxb
 * 
 */
public class HibernateUtilTest extends AbstractEntityTestCase {

	public void testGetType() {
		this.assertEquals(Hibernate.STRING, HibernateUtil.getType(sessionFactory, Foo.class, "name"));
		this.assertEquals(Hibernate.STRING, HibernateUtil.getType(sessionFactory, Foo.class, "properties['email']"));
		this.assertEquals(Hibernate.STRING, HibernateUtil.getType(sessionFactory, Foo.class, "properties['not found']"));

		try {
			this.assertEquals(Hibernate.STRING, HibernateUtil.getType(sessionFactory, HibernateUtilTest.class, "xxx"));
			this.fail("non hibernate class");
		} catch (QueryException e) { }
	}

}
