/**
 * 
 * created on 2007-4-13
 */
package net.ninecube.saturn.function.operation;

import org.codehaus.jrc.contribute.Jrc;
import org.codehaus.jrc.expression.Expressions;

import net.ninecube.lang.FrequenceDate;
import net.ninecube.saturn.Context;
import net.ninecube.saturn.database.AbstractDatabaseTestCase;
import net.ninecube.saturn.database.DataSet;
import junit.framework.TestCase;

/**
 * 
 * @author jxb
 * 
 */
public class PointOperationTest extends AbstractDatabaseTestCase {
	private Context context;
	private PointOperation pointOp;
	
	public void setUp(){
		super.setUp();
		context = new Context(fixture.dbm);
		context.setDataSet(context.getDataSet().where(super.getPredicate("cust_info.sex=1")));
		context.setCurrentFrequence(new FrequenceDate("D20060531"));
		pointOp = new PointOperation("POINTACCOUNT.POINT",
				PointOperation.ADD_ASSIGN_OP,Expressions.literal("100"));
		
	}

	public void testPopulate() {
		DataSet ds = pointOp.populate(context);
		this.assertEquals("insert into POINTCAL(POINTCAL.CUSTOMERID,POINTCAL.TRANSID," + 
				"POINTCAL.TRANSTYPE,POINTCAL.POINTTYPE,POINTCAL.POINT,POINTCAL.TRANSTIME," + 
				"POINTCAL.COMMENTS,POINTCAL.PROMOTIONPLANID) select cust_info.id,cust_info.id," + 
				"cust_info,POINTACCOUNT.POINT,100,2006-05-31,D20060531,-1 from cust_info " + 
				"where cust_info.sex=1", ds.toString());
		pointOp = new PointOperation("POINTACCOUNT.POINT",
				PointOperation.MUL_ASSIGN_OP,Expressions.literal("2"));
		ds = pointOp.populate(context);
		this.assertEquals("update POINTCAL set POINT=(POINTCAL.POINT*'2') where " + 
				"(POINTCAL.POINTTYPE='POINTACCOUNT.POINT' and POINTCAL.CUSTOMERID in " + 
				"(select cust_base.id from cust_info inner join cust_base on cust_info.id=cust_base.id " + 
				"where cust_info.sex=1))", ds.getSql("test"));
		// TODO: 自动引入表时不应该引入子查询中的表，修改后再测试
//		this.assertEquals("update POINTCAL set (POINTCAL.POINT) = ((POINTCAL.POINT*2)) " +
//				"where cust_info inner join POINTCAL on POINTCAL.CUSTOMERID=cust_info.id " +
//				"where (cust_info.sex=1 and POINTCAL.POINTTYPE=POINTACCOUNT.POINT)", ds.toString());
	}

}
