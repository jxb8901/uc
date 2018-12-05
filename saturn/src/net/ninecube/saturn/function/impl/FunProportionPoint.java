package net.ninecube.saturn.function.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import net.ninecube.saturn.Context;
import net.ninecube.saturn.database.Column;
import net.ninecube.saturn.database.DataSet;
import net.ninecube.saturn.database.DataSetUtil;
import net.ninecube.saturn.function.Function;
import net.ninecube.saturn.function.operation.PointOperation;

import org.apache.log4j.Logger;
import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.expression.Expressions;
import org.codehaus.jrc.expression.Operator;

public class FunProportionPoint extends AbstractFunction implements Function {
	private static final Logger log = Logger.getLogger(FunProportionPoint.class);

	/**
	 * index parameter: 1.积分类别, 2.金额类型 3.金额 4.奖励积分
	 */
	public Object execute(Context context, List indexArgs, Map namedArgs) {
		DataSet ds = context.getDataSet();
		String pttype = indexArgs.get(0).toString();
		if(pttype==null){
			log.error("can't locate column  '"+indexArgs.get(0)+"'");
		}
		log.debug("indexArgs '"+indexArgs+"'");
		Expression amtty = ((Column) indexArgs.get(1)).getSqlCode();
		BigDecimal amt = (BigDecimal) indexArgs.get(2);
		BigDecimal pt = (BigDecimal) indexArgs.get(3);
		Expression val = Expressions.binary(amtty, Operator.Div, Expressions
				.literal(amt));
		val = Expressions.binary(val, Operator.Mul, Expressions.literal(pt));
		PointOperation ptop = new PointOperation(pttype,
				PointOperation.ADD_ASSIGN_OP, val);
		
		ptop.execute(context);
		return null;
	}

}
