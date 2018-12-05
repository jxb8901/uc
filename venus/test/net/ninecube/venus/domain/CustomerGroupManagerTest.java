package net.ninecube.venus.domain;

import java.util.Date;

import org.springframework.dao.InvalidDataAccessApiUsageException;

/**
 * @author  Fox
 */
public class CustomerGroupManagerTest extends AbstractEntityManagerTestCase {
	private CustomerGroupManager cgm;

	public void testCRUD() {
		this.assertEquals(0, cgm.getAll().size());
		
		CustomerGroup cg = new CustomerGroup();
		cg.setIdno(99L);
		cg.setName("test group");
		cg.setCreationDate(new Date());
		cgm.save(cg);
		this.assertNotSame(new Integer(99), cg.getIdno());

		this.assertEquals(1, cgm.getAll().size());
		this.assertEquals(1, super.countRowsInTable("TCustomerGroup"));
		
		cg = cgm.getById(cg.getIdno());
		this.assertNotNull(cg);
		this.assertEquals("test group", cg.getName());
		
		cgm.delete(cg);
		this.assertEquals(0, cgm.getAll().size());
		this.assertEquals(0, super.countRowsInTable("TCustomerGroup"));
	}
	
	public void testManytoOne() {
		CustomerGroup parent = new CustomerGroup();
		parent.setName("parent");
		
		CustomerGroup child1 = new CustomerGroup();
		child1.setName("child1");
		child1.setParent(parent);
		cgm.save(child1);
		super.setComplete();
		try {
			super.endTransaction();
			this.fail("外键对象未保存时应该报错！");
		} catch (InvalidDataAccessApiUsageException ignore) { }
		super.startNewTransaction();
	}
	
	public void testParentChildren() {
		CustomerGroup parent = new CustomerGroup();
		parent.setName("parent");
		cgm.save(parent);
		
		CustomerGroup child1 = new CustomerGroup();
		child1.setName("child1");
		child1.setParent(parent);
		cgm.save(child1);
		super.setComplete();
		super.endTransaction();
		super.startNewTransaction();
		
		child1 = cgm.getById(child1.getIdno());
		this.assertNotNull(child1.getParent());
		this.assertEquals(parent.getIdno(), child1.getParent().getIdno());
		super.getSession().evict(parent);
		parent = cgm.getById(parent.getIdno());
		this.assertNotNull(parent.getChildren());
		this.assertEquals(1, parent.getChildren().size());
		
		CustomerGroup child2 = new CustomerGroup();
		child2.setName("child2");
		child2.setParent(parent);
		cgm.save(child2);
		super.setComplete();
		super.endTransaction();
		super.startNewTransaction();
		if (this.getSession().isOpen()) this.getSession().evict(parent);
		parent = cgm.getById(parent.getIdno());
		this.assertEquals(2, parent.getChildren().size());
	}
	
	public void setCustomergroupManager(CustomerGroupManager cgm) {
		this.cgm = cgm;
	}
}
