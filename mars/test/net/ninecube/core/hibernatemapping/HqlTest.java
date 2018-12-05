/**
 * 
 * created on 2007-4-20
 */
package net.ninecube.core.hibernatemapping;

import java.util.List;

import net.ninecube.core.AbstractEntityTestCase;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

/**
 * 
 * @author jxb
 */
public class HqlTest extends AbstractEntityTestCase {
	private LocalSessionFactoryBean sessionFactoryBean;

	public void test() {
		Session s = super.sessionFactory.getCurrentSession();
		Query q = s.createQuery("select blog from Blog blog, Book book " + 
				"where blog.author in elements(book.authors) " +
				"and book.id=?");
		q.setParameter(0, 1L);
		List list = q.list();
		System.out.println(list.size());
		System.out.println(list);
		q = s.createQuery("select blog from Blog blog,Book book where blog.author.id=book.authors.id and book.id=?");
		q.setParameter(0, 1L);
		list = q.list();
		System.out.println(list.size());
		System.out.println(list);
	}

	public void setLocalSessionFactoryBean(LocalSessionFactoryBean sessionFactoryBean) {
		this.sessionFactoryBean = sessionFactoryBean;
	}
}
