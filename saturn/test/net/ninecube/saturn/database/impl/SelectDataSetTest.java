/**
 * 
 * created on 2007-3-15
 */
package net.ninecube.saturn.database.impl;

import net.ninecube.saturn.database.AbstractDatabaseTestCase;

public class SelectDataSetTest extends AbstractDatabaseTestCase {

	public void testProject() {
		WhereDataSet d1 = new WhereDataSet(fixture.dbm);
		d1 = d1.project(d1.getColumnByAlias("客户.性别").getSqlCode());
		System.out.println(d1);
		this.assertEquals("select cust_base.sex from cust_base", d1.toString());
		d1 = d1.project(d1.getColumnByAlias("客户.客户号").getSqlCode());
		System.out.println(d1);
		this.assertEquals("select cust_base.sex,cust_base.id from cust_base", d1.toString());
	}
	
	public void testGroupBy() {
		WhereDataSet d1 = new WhereDataSet(fixture.dbm);
		d1 = d1.project(d1.getColumnByAlias("客户.性别").getSqlCode());
		d1 = d1.groupby(d1.getColumnByAlias("客户.性别").getSqlCode());
		System.out.println(d1);
		this.assertEquals("select cust_base.sex from cust_base group by cust_base.sex", d1.toString());
		d1 = d1.groupby(d1.getColumnByAlias("客户.客户号").getSqlCode());
		System.out.println(d1);
		this.assertEquals("select cust_base.sex from cust_base group by cust_base.sex,cust_base.id", d1.toString());
	}
}
