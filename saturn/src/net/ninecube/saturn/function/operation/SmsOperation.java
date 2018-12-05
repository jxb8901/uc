/**
 * 
 * created on 2006-11-30
 */
package net.ninecube.saturn.function.operation;

import java.util.List;

import net.ninecube.saturn.Context;
import net.ninecube.saturn.database.Column;
import net.ninecube.saturn.database.DataSet;

import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.expression.Expressions;

/**
 *  发短信操作
 *  生成待发送短信记录到特定表中；
 *  记录内容：客户ID，短信内容
 * @author jxb
 * 
 */
public class SmsOperation extends AbstractOperation{
	public static final String[] finishSqls = new String[] {
		"insert into SMSBONUS (CUSTOMERID,CONTENT) select CUSTOMERID,CONTENT from SMSBONUSCAL group by CUSTOMERID,CONTENT"
	};
	public static final String[] initSqls = new String[] {
		"delete from SMSBONUSCAL"
	};
	public static final String ResultProcessorName = "SMS";
	
	public static final String OPERATE_TABLE_NAME = "SMSBONUSCAL";
	
	private String content;
	
	public SmsOperation(String content){
		this.content = content;
	}
	
	protected String getProcessorName() {
		return ResultProcessorName;
	}
	
	protected DataSet populate(Context context) {
		DataSet ds = context.getDataSet();
		
		ds = ds.insert(ds.getColumnByAlias("SMSBONUSCAL.CUSTOMERID"));
		ds = ds.insert(ds.getColumnByAlias("SMSBONUSCAL.CONTENT"));
		
		//load CUSTOMER.ID field
		ds = ds.project(ds.getColumnByAlias("CUSTOMER.ID").getSqlCode());
		ds = ds.project(Expressions.literal(content));
		
		ds = ds.insert(ds.getColumnByAlias(getOperateTableName() + ".PROMOTIONPLANID"));
		ds = ds.project(getPromotionPlanID(context));
		
		return ds;
	}

	@Override
	protected String getOperateTableName() {
		return OPERATE_TABLE_NAME;
	}
	
	
}
