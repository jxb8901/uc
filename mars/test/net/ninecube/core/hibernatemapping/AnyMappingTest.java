/**
 * 
 * created on 2007-4-18
 */
package net.ninecube.core.hibernatemapping;

import net.ninecube.core.AbstractEntityTestCase;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Any;
import org.hibernate.mapping.PersistentClass;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

/**
 * 
 * @author jxb
 */
public class AnyMappingTest extends AbstractEntityTestCase {
	private LocalSessionFactoryBean sessionFactoryBean;

	public void test() {
		updateConfig();
		Session s = super.sessionFactory.getCurrentSession();
		ASource a = new ASource();
		a.setName("a");
		a.setAtype("a");
		s.save(a);
		BSource b = new BSource();
		b.setName("a");
		b.setBtype("a");
		s.save(b);
		CSource c = new CSource();
		c.setName("a");
		c.setCtype("a");
		s.save(c);
		
		Log log = new Log();
		log.setLog("aaa");
		log.setSource(a);
		s.save(log);
		super.setComplete();
		super.endTransaction();
		updateConfig();
	}
	
	@SuppressWarnings("unchecked")
	private void updateConfig() {
		Configuration config = this.sessionFactoryBean.getConfiguration();
		PersistentClass mapping = config.getClassMapping("net.ninecube.core.hibernatemapping.Log");
		System.out.println(mapping.getProperty("source").getValue());
		Any any = (Any) mapping.getProperty("source").getValue();
		System.out.println(any.getMetaValues());
		any.getMetaValues().remove("A");
		any.getMetaValues().put("A1", "net.ninecube.core.hibernatemapping.ASource");
	}

	public void setLocalSessionFactoryBean(LocalSessionFactoryBean sessionFactoryBean) {
		this.sessionFactoryBean = sessionFactoryBean;
	}
}
