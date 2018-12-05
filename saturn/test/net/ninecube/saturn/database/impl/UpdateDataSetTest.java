/**
 * 
 * created on 2007-3-15
 */
package net.ninecube.saturn.database.impl;

import net.ninecube.saturn.database.AbstractDatabaseTestCase;

import org.codehaus.jrc.expression.Expressions;

public class UpdateDataSetTest extends AbstractDatabaseTestCase {
		
	public void testUpdateValues() {
		WhereDataSet d1 = new WhereDataSet(fixture.dbm);
		d1 = d1.update(d1.getColumnByAlias("客户.性别"), Expressions.literal("1"));
		System.out.println(d1);
		this.assertEquals("update cust_base set sex='1'", d1.getSql());
		this.assertEquals("update cust_base set (cust_base.sex) = (1)", d1.toString());
		d1 = d1.update(d1.getColumnByAlias("客户.客户号"), Expressions.literal("0"));
		System.out.println(d1);
		this.assertEquals("update cust_base set sex='1',id='0'", d1.getSql());
		this.assertEquals("update cust_base set (cust_base.sex,cust_base.id) = (1,0)", d1.toString());
	}
	
	public void testUpdateWhere() {
		WhereDataSet d1 = new WhereDataSet(fixture.dbm);
		d1 = d1.update(d1.getColumnByAlias("客户.性别"), Expressions.literal("1"));
		d1 = d1.where(getPredicate("cust_info.id=1"));
		System.out.println("**" + d1);
		System.out.println("**" + d1.getSql());
		this.assertEquals("update cust_base set (cust_base.sex) = (1) " +
				"where cust_info where cust_info.id=1", d1.toString());
	}
}
