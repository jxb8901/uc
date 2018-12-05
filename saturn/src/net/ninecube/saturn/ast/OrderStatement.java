package net.ninecube.saturn.ast;

import net.ninecube.saturn.Context;
import net.ninecube.saturn.database.Column;
import net.ninecube.saturn.database.DataSet;
import net.ninecube.saturn.database.DataSetUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.expression.Expressions;

public class OrderStatement extends SimpleNode {
	private Log log = LogFactory.getLog(OrderStatement.class);
	private boolean isAsc = true; 
	private int top = 0;
	private SimpleNode stmtNode;
	
	
	public OrderStatement(int id) {
		super(id);
	}

	public OrderStatement(RuleParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(RuleParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
	
	@Override
	public Object execute(Context context){
		DataSet orgds = context.getDataSet();
		try{
			DataSet ds = orgds.clone();
			context.setDataSet(ds);
			Object expv = ((SimpleNode)jjtGetChild(0)).execute(context);
			Expression expr = null;
			if(expv instanceof Column){
				expr = ((Column)expv).getSqlCode();;
			}else if(expv instanceof Expression){
				expr = (Expression)expv;
			}else{
				log.debug("unpexted value type : " + expv);
				expr = Expressions.literal(expv);
			}
			ds = DataSetUtil.orderby(ds, expr,isAsc,top);
			context.setDataSet(ds);
			stmtNode.execute(context);
			
		}finally{
			context.setDataSet(orgds);
		}
		
		return null;
	}

	public boolean isAsc() {
		return isAsc;
	}

	public void setAsc(boolean isAsc) {
		this.isAsc = isAsc;
	}

	public void setTop(int top) {
		this.top = top;
	}
	
	@Override
	public String toString(){
		String str = super.toString();
		str += "(";
		str += jjtGetChild(0).toString();
		if(top != 0){
			str += ", " + top; 
		}
		str += ", " + isAsc;
		str += ")";
		
		return str;
	}

	public void setStmtNode(SimpleNode stmtNode) {
		this.stmtNode = stmtNode;
	}
}
