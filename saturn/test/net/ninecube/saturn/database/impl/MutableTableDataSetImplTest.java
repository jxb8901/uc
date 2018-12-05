/**
 * 
 * created on 2007-4-13
 */
package net.ninecube.saturn.database.impl;

import org.codehaus.jrc.expression.Expressions;

import net.ninecube.saturn.database.AbstractDatabaseTestCase;
import net.ninecube.saturn.database.Column;
import net.ninecube.saturn.database.DataSet;
import net.ninecube.saturn.database.AliasDataSet;
import net.ninecube.saturn.database.Table;
import junit.framework.TestCase;

/**
 * 
 * @author jxb
 * 
 */
public class MutableTableDataSetImplTest extends AbstractDatabaseTestCase {

	public void testUnsupportedOperation(){
		AliasDataSet m1 = getMutableTableDataSet();
		this.assertEquals("M1",m1.getTable().getName());
		try{
			m1.alias("M2");
			this.fail("unsupport alias operation on MutableTableDataSet ! ");
		}catch(UnsupportedOperationException e){};
	}
	
	public void testGetSql(){
		AliasDataSet m1 = getMutableTableDataSet();
		this.assertEquals("select id=cust_info.id from cust_info where cust_info.id=1 AS M1", m1.toString());
		DataSet s1 = m1.where(getPredicate("trans.amount>300"));
		this.assertEquals("select id=cust_info.id from cust_info where cust_info.id=1 AS M1 inner join" +
				" trans on M1.id=trans.custid where trans.amount>300", s1.toString());
	}
	
	public void testGetColumnByAlias() {
		AliasDataSet m1 = getMutableTableDataSet();
		Column col = m1.getColumnByAlias("id");
		this.assertNotNull(col);
		Column col2 = m1.getColumnByAlias("标识符");
		this.assertEquals(col, col2);
		
		this.assertNotNull(m1.getColumnByAlias("M1.标识符"));
	}
	
	public AliasDataSet getMutableTableDataSet(){
		WhereDataSet d1 = new WhereDataSet(fixture.dbm);
		d1 = d1.where(getPredicate("cust_info.id=1"));
		AliasDataSetImpl m1 =  new AliasDataSetImpl(d1.project(Expressions.alias("id",Expressions.column("cust_info", "id"))),"M1");
		Column col = m1.getTable().addColumn("id", new String[]{"id","标识符"});
		m1.getTable().addAssociation(col, m1.getColumnByAlias("cust_info.id"));
		return m1;
	}
}
