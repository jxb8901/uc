/**
 * 
 * created on 2007-3-15
 */
package net.ninecube.saturn.database.impl;

import org.codehaus.jrc.expression.Expressions;

import net.ninecube.saturn.database.AbstractDatabaseTestCase;

public class InsertDataSetTest extends AbstractDatabaseTestCase {
		
	public void testInsertSelect() {
		WhereDataSet d1 = new WhereDataSet(fixture.dbm);
		d1 = d1.project(d1.getColumnByAlias("客户.性别").getSqlCode());
		d1 = d1.insert(d1.getColumnByAlias("交易.金额"));
		System.out.println(d1);
		System.out.println(d1.getSql());
		this.assertEquals("insert into trans(trans.amount) select cust_base.sex from cust_base", d1.toString());
		d1 = d1.where(getPredicate("cust_info.id=1"));
		d1 = d1.project(d1.getColumnByAlias("客户.出生日期").getSqlCode());
		d1 = d1.insert(d1.getColumnByAlias("交易.交易日期"));
		System.out.println(d1);
		System.out.println(d1.getSql());
		this.assertEquals("insert into trans(trans.amount,trans.transdate) select cust_base.sex,cust_info.date " + 
				"from cust_base inner join cust_info on cust_info.id=cust_base.id where cust_info.id=1", d1.toString());
	}
	
	public void testInsertValues() {
		WhereDataSet d1 = new WhereDataSet(fixture.dbm);
		d1 = d1.insert(d1.getColumnByAlias("交易.金额"));
		d1 = d1.project(Expressions.literal("100"));
		System.out.println(d1);
		System.out.println(d1.getSql());
		this.assertEquals("insert into trans(trans.amount) values(100)", d1.toString());
		this.assertEquals("insert into trans(amount) values('100')", d1.getSql("test"));

		d1 = d1.insert(d1.getColumnByAlias("交易.交易类型"));
		d1 = d1.project(Expressions.literal("NET"));
		System.out.println(d1);
		System.out.println(d1.getSql());
		this.assertEquals("insert into trans(trans.amount,trans.transtype) values(100,NET)", d1.toString());
		this.assertEquals("insert into trans(amount,transtype) values('100','NET')", d1.getSql("test"));
	}
}
