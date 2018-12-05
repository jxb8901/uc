/**
 * 
 * created on 2007-2-2
 */
package net.ninecube.saturn.database.impl;

import net.ninecube.saturn.database.AbstractDatabaseTestCase;

/**
 * 
 * @author jxb
 * 
 */
public class TableImplTest extends AbstractDatabaseTestCase {

	public void testPostProcess() {
		this.assertEquals(2, fixture.trans.getAssociations().size());
		this.assertEquals("id", fixture.cust_base.getPrimaryKey().getName());
	}
	
	public void testHasAlias() {
		this.assertTrue(fixture.trans.hasAlias("trans"));
		this.assertTrue(fixture.trans.hasAlias("交易"));
		this.assertTrue(fixture.trans.hasAlias("交易记录"));
		this.assertFalse(fixture.trans.hasAlias("POS交易 "));
	}

	public void testGetColumnByAlias() {
		this.assertEquals("id", fixture.cust_base.getTableColumnByAlias("客户号").getName());
	}

	public void testGetColumnByAliasWithDynamicName() {
		this.assertEquals("transdate.YM", fixture.trans.getTableColumnByAlias("交易日期.年月").getName());
		try {
			fixture.trans.getTableColumnByAlias("交易日期.XXX");
			this.fail();
		} catch (RuntimeException ignore) { }
	}
	
	public void testIsBase() {
		this.assertTrue(fixture.trans.isBase());
		this.assertFalse(fixture.cust_base.isBase());
		//@ TODO: 复合表的“日期字段”测试
	}
}
