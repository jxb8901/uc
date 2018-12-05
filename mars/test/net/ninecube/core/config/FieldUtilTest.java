/**
 * 
 * created on 2007-1-23
 */
package net.ninecube.core.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import net.ninecube.core.config.impl.FieldConfImpl;
import net.ninecube.util.DynamicBean;

/**
 * 
 * @author jxb
 * 
 */
public class FieldUtilTest extends TestCase {
	private List<FieldConf> fields = new ArrayList<FieldConf>();
	
	protected void setUp() {
		FieldConfImpl f = new FieldConfImpl();
		f.setName("id");
		f.setType(FieldType.integer);
		fields.add(f);
		f = new FieldConfImpl();
		f.setName("name");
		f.setType(FieldType.string);
		fields.add(f);
		f = new FieldConfImpl();
		f.setName("date");
		f.setType(FieldType.date);
		fields.add(f);
		f = new FieldConfImpl();
		f.setName("enumt");
		f.setType(FieldType.enumt);
		fields.add(f);
	}

	public void testNewDynamicBean() {
		DynamicBean bean = FieldUtil.newDynamicBean(FieldUtilTest.class.getName()+"$$Example", fields);
		bean.set("id", 2);
		bean.set("name", "test");
		bean.set("date", new Date());
		bean.set("enumt", "enum");
		bean.set("xxx", "yyy");
		this.assertEquals(2, bean.get("id"));
		this.assertEquals("test", bean.get("name"));
		this.assertEquals(Date.class, bean.get("date").getClass());
		this.assertEquals(String.class, bean.get("enumt").getClass());
		this.assertEquals("enum", bean.get("enumt"));
		this.assertNull(bean.get("xxx"));
	}

	public void testGetFieldType() {
		FieldConf f = fields.get(1);
		this.assertEquals("name", f.getName());
		this.assertEquals(TestBean.class, FieldUtil.getFieldType(f, TestBean.class));
		f = fields.get(2);
		this.assertEquals("date", f.getName());
		this.assertEquals(Date.class, FieldUtil.getFieldType(f, TestBean.class));
	}
	
	private static class TestBean {
		private TestBean name;

		public TestBean getName() {
			return name;
		}

		public void setName(TestBean name) {
			this.name = name;
		}
	}
}
