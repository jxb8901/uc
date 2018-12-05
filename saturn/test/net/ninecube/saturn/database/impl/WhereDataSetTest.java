/**
 * 
 * created on 2007-3-15
 */
package net.ninecube.saturn.database.impl;

import net.ninecube.saturn.database.AbstractDatabaseTestCase;

import org.codehaus.jrc.expression.Expressions;
import org.codehaus.jrc.relation.Relation;

public class WhereDataSetTest extends AbstractDatabaseTestCase {
	
	public void testUnsupportedOperation(){
		WhereDataSet d = getDataSet("cust_base.sex=1");
		//alias operation
		try{
			d.alias("M2");
			this.fail("unsupport alias operation on WhereDataSet ! ");
		}catch(UnsupportedOperationException e){};
		UpdateDataSet d1 = new UpdateDataSet(d, 
				fixture.dbm.getColumnByAlias("cust_base.sex"), 
				Expressions.literal("a"));
		// alias operation
		try{
			d1.alias("");
			this.fail("unsupport alias operation on Update ! ");
		}catch(UnsupportedOperationException e){};
		//insert operation
		try{
			d1.insert(null);
			this.fail("unsupport insert operation on Update ! ");
		}catch(UnsupportedOperationException e){};
	}

	public void testGetRelation() {
		WhereDataSet d = getDataSet("cust_base.sex=1");
		d = d.where(getPredicate("trans.amount=100"));
		Relation r = d.getRelation();
		System.out.println(r);
		this.assertEquals("cust_base " + 
				"inner join trans on trans.custid=cust_base.id " + 
				"where (cust_base.sex=1 and trans.amount=100)", r.toString());
	}
	
	public void testMerge() {
		WhereDataSet d1 = getDataSet("cust_base.sex=1").merge(getDataSet("trans.amount=100"));
		WhereDataSet d2 = getDataSet("trans.amount=100").merge(getDataSet("cust_base.sex=1"));
		Relation r1 = d1.getRelation();
		Relation r2 = d2.getRelation();
		System.out.println(r1);
		System.out.println(r2);
		this.assertEquals("cust_base " +
				"inner join trans on trans.custid=cust_base.id where (cust_base.sex=1 and trans.amount=100)", r1.toString());
		this.assertEquals("trans inner join " + 
				"cust_base on trans.custid=cust_base.id where (trans.amount=100 and cust_base.sex=1)", r2.toString());
	}
	
	public void testWhere() {
		WhereDataSet d1 = new WhereDataSet(fixture.dbm);
		this.assertEquals(0, d1.getTables().size());
		
		d1 = d1.where(getPredicate("cust_info.id=1"));
		this.assertEquals(1, d1.getTables().size());
		
		d1 = d1.where(getPredicate("cust_base.id=1"));
		this.assertEquals(2, d1.getTables().size());
		
		d1 = d1.where(getPredicate("cust_base.name='test'"));
		this.assertEquals(2, d1.getTables().size());
	}
	
	public WhereDataSet getDataSet(String predicate) {
		WhereDataSet d1 = new WhereDataSet(fixture.dbm);
		d1 = d1.where(getPredicate(predicate));
		return d1;
	}
}
