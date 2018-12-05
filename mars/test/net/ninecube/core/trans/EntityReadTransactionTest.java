/**
 * 
 * created on 2007-1-22
 */
package net.ninecube.core.trans;

import java.io.Serializable;

import junit.framework.TestCase;
import net.ninecube.core.domain.Entity;
import net.ninecube.test.domain.TestUser;

/**
 * 
 * @author jxb
 * 
 */
public class EntityReadTransactionTest extends TestCase {

	public void testGetSerializableId() throws Exception {
		EntityReadTransaction t = new EntityReadTransaction() {
			public Entity createEntity() {
				TestUser entity = new TestUser();
				entity.setIdno(1L);
				return entity;
			}
			public String getEntityKeyName() {
				return "idno";
			}
		};
		t.prepare();
		Serializable id = t.getSerializableId();
		this.assertTrue(id instanceof Long);
		this.assertEquals(1L, id);
	}

}
