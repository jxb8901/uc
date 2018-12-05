/**
 * 
 * created on 2007-2-7
 */
package org.codehaus.jrc.contribute.util;

import org.codehaus.jrc.expression.Aggregate;
import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.expression.ExpressionVisitor;
import org.codehaus.jrc.expression.Operator;
import org.codehaus.jrc.expression.stringfunctions.SubstringExpression;

/**
 * 
 * @author jxb
 * 
 */
public class CloneExpressionVisitor extends CloneVisitor implements ExpressionVisitor<Expression> {
	protected Expression expression;
	
	public CloneExpressionVisitor clone(Expression expression) {
		CloneExpressionVisitor ret = (CloneExpressionVisitor) super.clone();
		ret.expression = expression;
		return ret;
	}

	public Expression visitAggregate(Aggregate aggr, Expression expr) {
		return cvu.factory.aggregate(aggr, cvu.ve(expr));
	}

	public Expression visitBinary(Expression e1, Operator op, Expression e2) {
		return cvu.factory.binary(cvu.ve(e1), op, cvu.ve(e2));
	}

	public Expression visitBoundParameter(String name, Object val) {
		return expression.bindArgument(name, val);
	}

	public Expression visitColumn(String table, String col) {
		return cvu.factory.column(table, col);
	}

	public Expression visitFunction(String f, Expression[] args) {
		return cvu.factory.function(null, f, cvu.ve(args));
	}

	public Expression visitLiteral(Object val) {
		return cvu.factory.literal(val);
	}

	public Expression visitNamed(String name, Expression expr) {
		return cvu.factory.alias(name, cvu.ve(expr));
	}

	public Expression visitNegate(Expression expr) {
		return cvu.factory.negate(cvu.ve(expr));
	}

	public Expression visitParameter(String name) {
		return cvu.factory.var(name);
	}

	public Expression visitWildcard(String table) {
		return cvu.factory.wildcard(table);
	}

	public Expression visitCustom(Object custom) {
		// TODO: 能否使用其它可配置的方式实现CustomExpression的克隆
		if (custom instanceof SubstringExpression) {
			SubstringExpression s = (SubstringExpression) custom;
			return new SubstringExpression(cvu.ve(s.getString()), cvu.ve(s.getStart()), cvu.ve(s.getLength()));
		}
		return expression;
	}

}
