/**
 * 
 * created on 2007-4-5
 */
package net.ninecube.core.domain;

import net.ninecube.core.AbstractEntityTestCase;
import net.ninecube.core.Bar;

/**
 * 
 * @author jxb
 * 
 */
public class AbstractEntityManagerTest extends AbstractEntityTestCase {
	protected EntityManager bm;
	
	@Override
	protected void onSetUpBeforeTransaction() throws Exception {
		AbstractEntityManager<Bar> bm = new AbstractEntityManager<Bar>(){};
		bm.setSessionFactory(sessionFactory);
		this.bm = bm;
	}

	@SuppressWarnings("unchecked")
	public void testCRUD() {
		Bar cg = new Bar();
		cg.setId(1L);
		cg.setName("test group");
		bm.save(cg);

		this.assertEquals(1, bm.getAll().size());
		this.assertEquals(1, super.countRowsInTable("Bar"));
		
		cg = (Bar) bm.getById(cg.getId());
		this.assertNotNull(cg);
		this.assertEquals("test group", cg.getName());
		
		bm.delete(cg);
		this.assertEquals(0, bm.getAll().size());
		this.assertEquals(0, super.countRowsInTable("Bar"));
	}
}
