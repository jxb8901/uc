/**
 * 
 * created on 2007-4-17
 */
package net.ninecube.saturn.database;

import org.codehaus.jrc.expression.Expressions;

import net.ninecube.saturn.database.impl.WhereDataSet;
import junit.framework.TestCase;

/**
 * 
 * @author jxb
 * 
 */
public class DataSetTest extends AbstractDatabaseTestCase {

	public void testReturnNewDataSet() {
		DataSet d1 = fixture.dbm.newDataSet();
		DataSet d2 = d1.where(getPredicate("cust_base.sex=1"));
		this.assertFalse(d1 == d2);
		d2 = d1.where(getPredicate("cust_base.sex=1"));
		this.assertFalse(d1 == d2);
		
		d1 = d2.project(Expressions.literal("a"));
		this.assertFalse(d1 == d2);
		d2 = d1.project(Expressions.literal("a"));
		this.assertFalse(d1 == d2);
		
		d1 = d2.groupby(Expressions.literal("a"));
		this.assertFalse(d1 == d2);
		d2 = d1.groupby(Expressions.literal("a"));
		this.assertFalse(d1 == d2);
		
		d1 = d2.orderby(Expressions.literal("a"), true, 1);
		this.assertFalse(d1 == d2);
		d2 = d1.orderby(Expressions.literal("a"), true, 1);
		this.assertFalse(d1 == d2);
		
		d1 = d2.alias("a");
		this.assertFalse(d1 == d2);
		
		d1 = d2.merge(d1);
		this.assertFalse(d1 == d2);
		d2 = d1.merge(d1);
		this.assertFalse(d1 == d2);

		d1 = d2.insert(fixture.dbm.getColumnByAlias("cust_base.sex"));
		this.assertFalse(d1 == d2);
		d2 = d1.insert(fixture.dbm.getColumnByAlias("cust_base.sex"));
		this.assertFalse(d1 == d2);
		d1 = d2.project(Expressions.literal("a"));
		this.assertFalse(d1 == d2);
		
		d1 = fixture.dbm.newDataSet().where(getPredicate("cust_base.sex=1"));
		d2 = d1.update(fixture.dbm.getColumnByAlias("cust_base.sex"), Expressions.literal("a"));
		this.assertFalse(d1 == d2);
		d1 = d2.update(fixture.dbm.getColumnByAlias("cust_base.sex"), Expressions.literal("a"));
		this.assertFalse(d1 == d2);
	}

}
