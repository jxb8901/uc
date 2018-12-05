/**
 * 
 * created on 2007-1-29
 */
package net.ninecube.core.trans;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * 
 * @author jxb
 * 
 */
public class EntityUpdateTransactionTest extends TestCase {
	
	public void testCopySimpleProperty() throws Exception {
		TestBean s = new TestBean();
		TestBean d = new TestBean();
		s.setIdno(1L);
		s.setName("test1");
		Date date = new Date();
		s.setCreationDate(date);
		
		d.setIdno(2L);
		EntityUpdateTransaction.copyProperty(d, "idno", s);
		this.assertEquals(new Long(1), d.getIdno());
		EntityUpdateTransaction.copyProperty(d, "name", s);
		this.assertEquals("test1", d.getName());
		EntityUpdateTransaction.copyProperty(d, "creationDate", s);
		this.assertEquals(date, d.getCreationDate());
		s.setCreationDate(null);
		EntityUpdateTransaction.copyProperty(d, "creationDate", s);
		this.assertNull(d.getCreationDate());
	}
	
	public void testCopyBeanProperty() throws Exception {
		TestBean s = new TestBean();
		TestBean d = new TestBean();
		TestBean target = new TestBean();
		s.setParent(target);
		EntityUpdateTransaction.copyProperty(d, "parent", s);
		this.assertEquals(target, d.getParent());
	}
	
	public void testCopyMapProperty() throws Exception {
		TestBean s = new TestBean();
		TestBean d = new TestBean();
		s.setParams(new HashMap<String, String>());
		s.getParams().put("test", "1234");
		EntityUpdateTransaction.copyProperty(d, "params", s);
		this.assertNotNull(d.getParams());
		this.assertEquals(d.getParams().get("test"), "1234");

		s.getParams().put("test", "4321");
		s.getParams().put("name", "abcd");
		EntityUpdateTransaction.copyProperty(d, "params", s);
		this.assertNotNull(d.getParams());
		this.assertEquals(d.getParams().get("test"), "4321");
		this.assertEquals(d.getParams().get("name"), "abcd");
	}

	public static class TestBean {
		private Long idno;
		private String name;
		private Date creationDate;
		private TestBean parent;
		private Map<String, String> params;
		
		//~ getter and setter
		
		public Date getCreationDate() {
			return creationDate;
		}
		public void setCreationDate(Date creationDate) {
			this.creationDate = creationDate;
		}
		public Long getIdno() {
			return idno;
		}
		public void setIdno(Long idno) {
			this.idno = idno;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Map<String, String> getParams() {
			return params;
		}
		public void setParams(Map<String, String> params) {
			this.params = params;
		}
		public TestBean getParent() {
			return parent;
		}
		public void setParent(TestBean parent) {
			this.parent = parent;
		}
	}
}
