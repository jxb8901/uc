/**
 * 
 * created on 2007-2-5
 */
package net.ninecube.util.internal;

import junit.framework.TestCase;
import net.ninecube.util.internal.DefaultEnumerManager.EnumerMap;

/**
 * 
 * @author jxb
 * 
 */
public class DefaultEnumerManagerTest extends TestCase {

	public void testGetEnumer() {
		DefaultEnumerManager em = new DefaultEnumerManager();
		EnumerMap enumer1 = new EnumerMap();
		em.enumers.put("db.table.type", enumer1);
		this.assertEquals(enumer1, em.getEnumer("db.table.type"));
		this.assertNull(em.getEnumer("table.type"));
		this.assertNull(em.getEnumer("type"));
		this.assertNull(em.getEnumer("type.table"));

		EnumerMap enumer2 = new EnumerMap();
		em.enumers.put("table.type", enumer2);
		this.assertEquals(enumer1, em.getEnumer("db.table.type"));
		this.assertEquals(enumer2, em.getEnumer("table.type"));
		this.assertNull(em.getEnumer("type"));
		this.assertNull(em.getEnumer("type.table"));
	}
	
	public void testGetValue() {
		DefaultEnumerManager em = new DefaultEnumerManager("net/ninecube/util/internal/enumer.xml").init();
		this.assertEquals(4, em.enumers.size());
		this.assertEquals(2, em.getValues("sextp").size());
		System.out.println(em.getValues("sextp"));
		this.assertEquals("1", em.getValue("sextp", "女"));
	}
}
