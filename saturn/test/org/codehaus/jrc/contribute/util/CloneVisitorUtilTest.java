/**
 * 
 * created on 2007-2-8
 */
package org.codehaus.jrc.contribute.util;

import junit.framework.TestCase;

import org.codehaus.jrc.contribute.Jrc;
import org.codehaus.jrc.contribute.util.CloneVisitorUtil;
import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.predicate.Predicate;

/**
 * 
 * @author jxb
 * 
 */
public class CloneVisitorUtilTest extends TestCase {

	public void testClonePredicate() {
		CloneVisitorUtil cv = CloneVisitorUtil.get();
		Predicate p1 = Jrc.getParser().parsePredicate("a = b");
		Predicate p2 = cv.vp(p1);
		this.assertNotSame(p2, p1);
		this.assertEquals(p1.toString(), p2.toString());

		p1 = Jrc.getParser().parsePredicate("substring(a, 1, 2) = '2'");
		p2 = cv.vp(p1);
		this.assertNotSame(p2, p1);
		this.assertEquals(p1.toString(), p2.toString());
	}
		
	public void testCloneExpression() {
		CloneVisitorUtil cv = CloneVisitorUtil.get();
		Expression e1 = Jrc.getParser().parseExpression("function(a, 1, 2)");
		Expression e2 = cv.ve(e1);
		this.assertNotSame(e2, e1);
		this.assertEquals(e1.toString(), e2.toString());
	}
	
	public void testCloneCustomExpression() {
		CloneVisitorUtil cv = CloneVisitorUtil.get();
		Expression e1 = Jrc.getParser().parseExpression("substring(a, 1, 2)");
		Expression e2 = cv.ve(e1);
		this.assertNotSame(e2, e1);
		this.assertEquals(e1.toString(), e2.toString());

		e1 = Jrc.getParser().parseExpression("now()");
		e2 = cv.ve(e1);
		this.assertSame(e2, e1);
		this.assertEquals(e1.toString(), e2.toString());
	}
}
