package net.ninecube.saturn.ast;

import java.math.BigDecimal;

import net.ninecube.saturn.Context;

import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.expression.Expressions;
import org.codehaus.jrc.predicate.Predicate;
import org.codehaus.jrc.predicate.Predicates;

public class UnaryExpression extends SimpleNode {
	private String operator;

	public UnaryExpression(int id) {
		super(id);
	}

	public UnaryExpression(RuleParser p, int id) {
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
		SimpleNode child = (SimpleNode) jjtGetChild(0);
		result = child.execute(context);
		if (Operator.NOT.equals(Operator.asOperator(operator))) {
			if (result instanceof Boolean) {
				result = Boolean.TRUE;
				if (((Boolean) result).booleanValue()) {
					result = Boolean.FALSE;
				}
				return result;
			}
			result = Predicates.buildNot((Predicate)result );
		} else {
			
			if (Operator.MINUS.equals(Operator.asOperator(operator))) {
				if (result instanceof BigDecimal) {
					return ((BigDecimal) result).negate();
				}
				result = Expressions.negate((Expression) result);
			}
		}
		return result;
	}

}
