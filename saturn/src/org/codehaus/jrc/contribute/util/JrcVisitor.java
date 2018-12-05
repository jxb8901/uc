/**
 * 
 * created on 2007-6-23
 */
package org.codehaus.jrc.contribute.util;

import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.predicate.Predicate;
import org.codehaus.jrc.relation.Relation;

/**
 * 
 * @author jxb
 */
public interface JrcVisitor<R> {
	
	/**
	 * @param object 类型可能是Relation, Predicate, Expression 或其它类型对象
	 * @return 返回true表示迭代继续进入object内部，否则不进入内部迭代
	 */
	public boolean visit(Relation relation);
	public boolean visit(Predicate relation);
	public boolean visit(Expression relation);

	public void visit(Object object);
}
