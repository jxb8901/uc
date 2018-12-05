package net.ninecube.venus.domain;

import java.util.Date;

/**
 * @author  jxb
 */
public class CustomerManagerTest extends AbstractEntityManagerTestCase {
	private CustomerManager cm;

	public void testGet() {
		this.assertEquals(0, cm.getAll().size());
		super.jdbcTemplate.update("insert into knc_cust(custno, custna) values(?,?)", new Object[]{"0000000001", "张三"});
		super.jdbcTemplate.update("insert into knc_prsn(custno, mobitl) values(?,?)", new Object[]{"0000000001", "13088888888"});
		Customer c = cm.getById("0000000001");
		this.assertNotNull(c);
		this.assertEquals("张三", c.getName());
		this.assertEquals("13088888888", c.getAddress());
		
		// 修改只读对象时，hibernate实际不会同步对象状态到数据库中
		c.setAddress("read only");
		cm.save(c);
		super.setComplete();
		super.endTransaction();
		super.startNewTransaction();
		super.getSession().evict(c);
		c = cm.getById("0000000001");
		this.assertNotSame("read only", c.getAddress());

		// 删除只读对象时，hibernate会报错
		try {
			cm.delete(c);
			super.setComplete();
			this.fail("shouldn't delete read-only object!");
		} catch (UnsupportedOperationException ignore) { }
		super.endTransaction();

		super.jdbcTemplate.update("delete from knc_prsn", new Object[]{});
		super.jdbcTemplate.update("delete from knc_cust", new Object[]{});
		super.startNewTransaction();
	}
	
	public void setCustomerManager(CustomerManager cm) {
		this.cm = cm;
	}
}
