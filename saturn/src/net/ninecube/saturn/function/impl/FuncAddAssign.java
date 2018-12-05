package net.ninecube.saturn.function.impl;

import java.util.List;
import java.util.Map;

import net.ninecube.saturn.Context;
import net.ninecube.saturn.exception.RuleSyntaxException;
import net.ninecube.saturn.function.operation.Operation;
import net.ninecube.saturn.function.operation.PointOperation;

import org.apache.log4j.Logger;
import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.expression.Expressions;

public class FuncAddAssign extends AbstractFunction {
	private static final Logger log = Logger.getLogger(FuncAddAssign.class);
	
	private String operator;

	public FuncAddAssign(String operator){
		this.operator = operator;
	}

	/**
	 *  index parameter:1.积分类别 2.积分增加表达式
	 */
	public Object execute(Context context, List indexArgs, Map namedArgs) {
		String pttype = indexArgs.get(0).toString();
		if(pttype==null){
			String errMsg ;
			errMsg = "can't locate column , indexArgs : "+indexArgs+"";
			log.error(errMsg);
			throw new RuleSyntaxException(errMsg);
		}
		Expression val = null;
		Object rawval = indexArgs.get(1);
		//log.debug("point val : " + rawval + " ; class : " + rawval.getClass());
		if(rawval instanceof Expression){
			val = (Expression)rawval;
		}else{
			val=Expressions.literal(rawval);
		}
		Operation op = new PointOperation(pttype,getOperator(),val);
		log.debug("op : " + op);
		op.execute(context);
		return null;
	}
	
	protected String getOperator(){
		return operator;
	}

}
