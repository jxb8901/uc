/**
 * 
 * created on Jul 9, 2007
 */
package net.ninecube.core.task.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.PropertyBatchUpdateException;

import net.ninecube.util.DateUtil;

import junit.framework.TestCase;

/**
 * 
 * @author jxb
 */
public class DelegatingJobTest extends TestCase {

	public void testBindProperties() {
		Map<Object, Object> params = new HashMap<Object, Object>();
		params.put("id", "3");
		params.put("date", "20070912");
		params.put("booleanValue", "true");
		TestTask t = DelegatingJob.bindProperties(TestTask.class, params);
		this.assertNotNull(t);
		this.assertEquals(3, t.getId());
		this.assertEquals("20070912", DateUtil.getYYYYMMDD(t.getDate()));
		this.assertEquals(true, t.getBooleanValue());
		
		params.put("booleanValue", null);
		try {
			DelegatingJob.bindProperties(TestTask.class, params);
			this.fail("can't support null for boolean type!");
		} catch (PropertyBatchUpdateException e) {
			this.assertEquals(1, e.getExceptionCount());
			this.assertEquals(IllegalArgumentException.class, e.getPropertyAccessExceptions()[0].getCause().getClass());
		}
	}

}
