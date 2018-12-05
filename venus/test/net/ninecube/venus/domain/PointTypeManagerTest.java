package net.ninecube.venus.domain;

import java.util.Date;


/**
 * @author  JXB
 */
public class PointTypeManagerTest extends AbstractEntityManagerTestCase {
	private PointTypeManager um;

	public void testCRUD() {
		PointType cg = new PointType();
		cg.setIdno(99L);
		cg.setName("test group");
		um.save(cg);
		this.assertNotSame(new Integer(99), cg.getIdno());
		super.setComplete();
		super.endTransaction();
		super.startNewTransaction();

		this.assertEquals(1, um.getAll().size());
		this.assertEquals(1, super.countRowsInTable("TPointType"));
		
		cg = um.getById(cg.getIdno());
		this.assertNotNull(cg);
		this.assertEquals("test group", cg.getName());

		super.endTransaction();
		super.startNewTransaction();
		um.delete(cg);
		super.setComplete();
		super.endTransaction();
		super.startNewTransaction();
		this.assertEquals(0, um.getAll().size());
		this.assertEquals(0, super.countRowsInTable("TPointType"));
	}
	
	public void testDelete() {
		try {
			um.delete(null);
			this.fail();
		} catch (IllegalArgumentException ignore) { }
	}
	
	public void setPointtypeManager(PointTypeManager cgm) {
		this.um = cgm;
	}

}
