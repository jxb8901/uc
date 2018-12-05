/**
 * 
 * created on 2007-2-7
 */
package org.codehaus.jrc.contribute.util;

import org.codehaus.jrc.contribute.Jrc;
import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.parser.factories.SqlFactory;
import org.codehaus.jrc.predicate.Predicate;
import org.codehaus.jrc.relation.Relation;

/**
 * 
 * @author jxb
 * 
 */
public class CloneVisitorUtil {
	protected SqlFactory factory = Jrc.getSqlFactory();
	private CloneRelationVisitor relationVisitorPrototype = new CloneRelationVisitor();
	private ClonePredicateVisitor predicateVisitorPrototype = new ClonePredicateVisitor();
	private CloneExpressionVisitor expressionVisitorPrototype = new CloneExpressionVisitor();
	
	private CloneVisitorUtil() {}
	
	private CloneVisitorUtil init() {
		this.relationVisitorPrototype.cvu = this;
		this.predicateVisitorPrototype.cvu = this;
		this.expressionVisitorPrototype.cvu = this;
		return this;
	}
	
	public static CloneVisitorUtil get() {
		return new CloneVisitorUtil().init();
	}

	//~ visitor
	
	public Relation vr(Relation r) {
		return r.accept(relationVisitorPrototype.clone(r));
	}
	
	public Predicate vp(Predicate p) {
		return p.accept(predicateVisitorPrototype.clone(p));
	}

	public Expression ve(Expression e) {
		return e.accept(expressionVisitorPrototype.clone(e));
	}

	public Expression[] ve(Expression[] e) {
		Expression[] ret = new Expression[e.length];
		for (int i = 0; i < e.length; i++) ret[i] = ve(e[i]);
		return ret;
	}
	
	//~ public setter
	
	public SqlFactory getSqlFactory() {
		return this.factory;
	}
	
	public void setFactory(SqlFactory factory) {
		this.factory = factory;
	}

	public void setExpressionVisitorPrototype(CloneExpressionVisitor expressionVisitorPrototype) {
		this.expressionVisitorPrototype = expressionVisitorPrototype;
		this.expressionVisitorPrototype.cvu = this;
	}

	public void setPredicateVisitorPrototype(ClonePredicateVisitor predicateVisitorPrototype) {
		this.predicateVisitorPrototype = predicateVisitorPrototype;
		this.predicateVisitorPrototype.cvu = this;
	}

	public void setRelationVisitorPrototype(CloneRelationVisitor relationVisitorPrototype) {
		this.relationVisitorPrototype = relationVisitorPrototype;
		this.relationVisitorPrototype.cvu = this;
	}

}
