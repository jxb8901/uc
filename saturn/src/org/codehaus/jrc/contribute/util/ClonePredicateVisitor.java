/**
 * 
 * created on 2007-2-7
 */
package org.codehaus.jrc.contribute.util;

import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.predicate.BoolOperator;
import org.codehaus.jrc.predicate.LogicalOperator;
import org.codehaus.jrc.predicate.Predicate;
import org.codehaus.jrc.predicate.PredicateVisitor;
import org.codehaus.jrc.relation.Relation;

/**
 * 
 * @author jxb
 * 
 */
public class ClonePredicateVisitor extends CloneVisitor implements PredicateVisitor<Predicate> {
	protected Predicate predicate;

	public ClonePredicateVisitor clone(Predicate predicate) {
		ClonePredicateVisitor ret = (ClonePredicateVisitor) super.clone();
		ret.predicate = predicate;
		return ret;
	}

	public Predicate visitBetween(Expression e, Expression from, Expression to) {
		return cvu.factory.between(cvu.ve(e), cvu.ve(from), cvu.ve(to));
	}

	public Predicate visitBinary(Predicate p1, LogicalOperator op, Predicate p2) {
		return cvu.factory.binary(cvu.vp(p1), op, cvu.vp(p2));
	}

	public Predicate visitBool(boolean value) {
		return cvu.factory.always(value);
	}

	public Predicate visitComparison(Expression e1, BoolOperator op, Expression e2, boolean nullAware) {
		// nullAware 使用factory中的设置
		return cvu.factory.comparison(cvu.ve(e1), op, cvu.ve(e2));
	}

	public Predicate visitExists(Relation rel) {
		return cvu.factory.exists(cvu.vr(rel));
	}

	public Predicate visitGroupComparison(Expression[] e1, BoolOperator op, Expression[] e2, boolean nullAware) {
		return cvu.factory.comparison(cvu.ve(e1), op, cvu.ve(e2));
	}

	public Predicate visitIn(Expression expr, Expression[] vals) {
		return cvu.factory.in(cvu.ve(expr), cvu.ve(vals));
	}

	public Predicate visitInRelation(Expression expr, Relation rel) {
		return cvu.factory.in(cvu.ve(expr), cvu.vr(rel));
	}

	public Predicate visitIsNull(Expression expr) {
		return cvu.factory.isNull(cvu.ve(expr), true);
	}

	public Predicate visitLike(Expression expr, Expression pattern, Expression escaper) {
		return cvu.factory.like(cvu.ve(expr), cvu.ve(pattern), cvu.ve(escaper));
	}

	public Predicate visitNot(Predicate pred) {
		return cvu.factory.not(cvu.vp(pred));
	}

	public Predicate visitNotBetween(Expression e, Expression from, Expression to) {
		return cvu.factory.notBetween(cvu.ve(e), cvu.ve(from), cvu.ve(to));
	}

	public Predicate visitNotExists(Relation rel) {
		return cvu.factory.notExists(cvu.vr(rel));
	}

	public Predicate visitNotIn(Expression expr, Expression[] vals) {
		return cvu.factory.notIn(cvu.ve(expr), cvu.ve(vals));
	}

	public Predicate visitNotInRelation(Expression expr, Relation rel) {
		return cvu.factory.notIn(cvu.ve(expr), cvu.vr(rel));
	}

	public Predicate visitNotLike(Expression expr, Expression pattern, Expression escaper) {
		return cvu.factory.notLike(cvu.ve(expr), cvu.ve(pattern), cvu.ve(escaper));
	}

	public Predicate visitNotNull(Expression expr) {
		return cvu.factory.isNull(cvu.ve(expr), false);
	}

	public Predicate visitCustom(Object custom) {
		return predicate;
	}

}
