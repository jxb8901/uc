package net.ninecube.venus.domain;

import java.util.Date;

/**
 * @author  jxb
 */
public class GiftManagerTest extends AbstractEntityManagerTestCase {
	private GiftManager gm;
	private static final byte[] IMAGE = "\1\2\3\4".getBytes();

	public void testCRUD() {
		this.assertEquals(0, gm.getAll().size());
		
		Gift cg = new Gift();
		cg.setName("test");
		cg.setImage(IMAGE);
		gm.save(cg);
		this.assertNotSame(new Integer(99), cg.getIdno());

		this.assertEquals(1, gm.getAll().size());
		this.assertEquals(1, super.countRowsInTable("TGift"));
		
		cg = gm.getById(cg.getIdno());
		this.assertNotNull(cg);
		this.assertEquals("test", cg.getName());
		this.assertEquals(IMAGE, cg.getImage());
		
		gm.delete(cg);
		this.assertEquals(0, gm.getAll().size());
		this.assertEquals(0, super.countRowsInTable("TGift"));
	}
	
	public void setGiftManager(GiftManager gm) {
		this.gm = gm;
	}
}
