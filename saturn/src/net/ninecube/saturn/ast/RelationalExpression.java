package net.ninecube.saturn.ast;

import java.math.BigDecimal;
import java.util.List;

import net.ninecube.saturn.Context;
import net.ninecube.saturn.DateData;
import net.ninecube.saturn.database.Column;
import net.ninecube.saturn.database.DatabaseException;

import org.apache.log4j.Logger;
import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.expression.Expressions;
import org.codehaus.jrc.predicate.BoolOperator;
import org.codehaus.jrc.predicate.Predicate;
import org.codehaus.jrc.predicate.Predicates;

public class RelationalExpression extends SimpleNode {
	Logger log = Logger.getLogger(RelationalExpression.class);

	private String operator = null;

	public RelationalExpression(int id) {
		super(id);
	}

	public RelationalExpression(RuleParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(RuleParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

//	public void validate(Context context, List<Exception> exceptions){
//		try {
//			execute(context);
//		} catch (DatabaseException e) {
//			exceptions.add(e);
//		}
//	}
	
	public Object execute(Context context) {
		Object loperand =((SimpleNode) jjtGetChild(0)).execute(context);
		Object roperand = ((SimpleNode) jjtGetChild(1)).execute(context);
		if(loperand instanceof Column ){
			if(roperand instanceof String || roperand instanceof DateData) roperand = ((Column)loperand).getSqlValue(roperand);
			else if(roperand instanceof Column)roperand = ((Column)roperand).getSqlCode();
			loperand = ((Column)loperand).getSqlCode();
		}else if (roperand instanceof Column ){
			if(loperand instanceof String) loperand = ((Column)roperand).getSqlValue(loperand);
			roperand = ((Column)roperand).getSqlCode();
		}
		
		if (loperand instanceof BigDecimal && roperand instanceof BigDecimal) {
			return cal((BigDecimal) loperand, (BigDecimal) roperand);
		}

		Expression lexpr;
		Expression rexpr;
		if (loperand instanceof Expression) {
			lexpr = (Expression) loperand;
		} else {
			lexpr = Expressions.literal(loperand);
		}
		if (roperand instanceof Expression) {
			rexpr = (Expression) roperand;
		} else {
			rexpr = Expressions.literal(roperand);
		}
		Predicate result = null;
		log.debug("operator '" + getOperator() + "' ");
		String op = getOperator();
		if ("==".equals(op)) {
			op = "=";
		}
		result = Predicates.comparison(lexpr, BoolOperator
				.asOperator(op), rexpr, false);
		log.debug("result '" + result + "'");
		return result;

	}

	public Boolean cal(BigDecimal operand1, BigDecimal operand2) {
		boolean result = false;
		int cmprst = operand1.compareTo(operand2);
		Operator op = Operator.asOperator(getOperator());
		switch (op) {
		case GT:
			result = cmprst > 0;
			break;
		case GE:
			result = cmprst >= 0;
			break;
		case EQ:
			result = cmprst == 0;
			break;
		case NE:
			result = cmprst != 0;
			break;
		case LT:
			result = cmprst < 0;
			break;
		case LE:
			result = cmprst <= 0;
			break;
		}

		return Boolean.valueOf(result);
	}

	public Class getResultType() {
		return Predicate.class;
	}

}
