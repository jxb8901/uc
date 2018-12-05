/**
 * 
 * created on 2007-4-12
 */
package net.ninecube.saturn.database;

import junit.framework.TestCase;
import net.ninecube.saturn.database.impl.WhereDataSet;
import net.ninecube.saturn.database.impl.WhereDataSetTest;

import org.codehaus.jrc.expression.Expressions;

/**
 * 
 * @author jxb
 * 
 */
public class DataSetUtilTest extends TestCase {
	private TableImplTestFixture fixture = new TableImplTestFixture();
	
	protected void setUp() throws Exception {
		fixture.setUp();
		DataSetUtil.aliasRefCout = 0;
	}

	public void testAggregate() {
		WhereDataSet d1 = new WhereDataSet(fixture.dbm);
		d1 = d1.where(WhereDataSetTest.getPredicate("cust_info.id=1"));
		DataSet d2 = DataSetUtil.aggregate(d1, null, "总金额");
		System.err.println(d2);
		
		this.assertEquals("select id=cust_info.id,recordNum=count (cust_info.id) " +
				"from cust_info where cust_info.id=1 group by cust_info.id AS table1", d2.toString());
		
		d2 = d2.where(WhereDataSetTest.getPredicate("cust_info.sex=1"));
		System.err.println(d2);
		this.assertEquals("select id=cust_info.id,recordNum=count (cust_info.id) from cust_info where cust_" + 
				"info.id=1 group by cust_info.id AS table1 inner join cust_info on table1.id=cust_info.id " + 
				"where cust_info.sex=1", d2.toString());
		
		d2 = d2.where(WhereDataSetTest.getPredicate("trans.amount > 100"));
		System.err.println(d2);
		this.assertEquals("select id=cust_info.id,recordNum=count (cust_info.id) from cust_info where " + 
				"cust_info.id=1 group by cust_info.id AS table1 inner join cust_info on table1.id=cust_info.id " + 
				"inner join trans on (trans.custid=cust_info.id and table1.id=trans.custid) where " + 
				"(cust_info.sex=1 and trans.amount>100)", d2.toString());
	}

	// 测试虚拟表与交易表自动建关联
	public void testAggregate1() {
		WhereDataSet d1 = new WhereDataSet(fixture.dbm);
		d1 = d1.where(WhereDataSetTest.getPredicate("cust_info.id=1"));
		DataSet d2 = DataSetUtil.aggregate(d1, null, "总金额");		
		d2 = d2.where(WhereDataSetTest.getPredicate("trans.amount > 100"));
		System.err.println(d2);
		this.assertEquals("select id=cust_info.id,recordNum=count (cust_info.id) from cust_info where " + 
				"cust_info.id=1 group by cust_info.id AS table1 inner join trans on table1.id=trans.custid where " + 
				"trans.amount>100", d2.toString());
	}
	
	public void testOrderby() {
		WhereDataSet d1 = new WhereDataSet(fixture.dbm);
		d1 = d1.where(WhereDataSetTest.getPredicate("cust_info.id=1"));
		DataSet d2 = DataSetUtil.orderby(d1, Expressions.column("cust_info", "id"), true, 0);
		System.err.println(d2);
		
		this.assertEquals("select id=cust_info.id from cust_info where " +
				"cust_info.id=1 order by cust_info.id asc AS table1", d2.toString());
	}

}
