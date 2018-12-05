package net.ninecube.saturn.ast;

import java.math.BigDecimal;

import net.ninecube.saturn.Context;
import net.ninecube.saturn.database.Column;
import net.ninecube.saturn.exception.RuleSyntaxException;

import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.expression.Expressions;

public class AddExpression extends SimpleNode {
	private String operator = null;

	public AddExpression(int id) {
		super(id);
	}

	public AddExpression(RuleParser p, int id) {
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

	public Object execute(Context context){
		Object result = null;
		SimpleNode ln = (SimpleNode) jjtGetChild(0);
		SimpleNode rn = (SimpleNode) jjtGetChild(1);
		Object loperand = ln.execute(context);
		Object roperand = rn.execute(context);
		if (loperand instanceof BigDecimal && roperand instanceof BigDecimal) {
			result = cal((BigDecimal) loperand, (BigDecimal) roperand);
			return result;
		}

		if(loperand instanceof Column ){
			if(roperand instanceof String) roperand = ((Column)loperand).getSqlValue(roperand);
			else if(roperand instanceof Column)roperand = ((Column)roperand).getSqlCode();
			loperand = ((Column)loperand).getSqlCode();
		}else if (roperand instanceof Column ){
			if(loperand instanceof String) loperand = ((Column)roperand).getSqlValue(loperand);
			roperand = ((Column)roperand).getSqlCode();
		}		
		
		Expression lexpr = null;
		Expression rexpr = null;
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

		result = Expressions.binary(lexpr, org.codehaus.jrc.expression.Operator
				.asOperator(getOperator()), rexpr);

		return result;
	}

	protected BigDecimal cal(BigDecimal loperand, BigDecimal roperand){
		BigDecimal result = null;
		Operator op = Operator.asOperator(operator);
		switch (op) {
		case PLUS:
			result = loperand.add(roperand);
			break;
		case MINUS:
			result = loperand.subtract(roperand);
			break;
		case MUL:
			result = loperand.multiply(roperand);
			break;
		case DIV:
			result = loperand.divide(roperand);
			break;
		case MOD:
			result = loperand.remainder(roperand);
			break;
		default:
			throw new RuleSyntaxException("unsupported operator '" + operator
					+ "' .");
		}
		return result;
	}
}
