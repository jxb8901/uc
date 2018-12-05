/**
 * 
 * created on 2007-6-23
 */
package org.codehaus.jrc.contribute.util;

import java.util.Set;

import org.codehaus.jrc.ast.BaseAstVisitor;
import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.predicate.Predicate;
import org.codehaus.jrc.relation.Relation;

/**
 * 
 * @author jxb
 */
public abstract class JrcVisitorSupport<R> extends BaseAstVisitor<R> implements JrcVisitor {

	public boolean visit(Relation relation) {
		relation.accept(this);
		return true;
	}

	public boolean visit(Predicate pred) {
		pred.accept(this);
		return true;
	}

	public boolean visit(Expression expr) {
		expr.accept(this);
		return true;
	}

	public void visit(Object object) {
	}
	
	public abstract R getResult();
}
