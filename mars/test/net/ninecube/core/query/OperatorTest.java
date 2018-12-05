/**
 * 
 * created on 2007-1-13
 */
package net.ninecube.core.query;

import junit.framework.TestCase;

/**
 * 
 * @author jxb
 * 
 */
public class OperatorTest extends TestCase {

	public void testValueOf() {
		Operator.valueOf("like");
		try {
			Operator.valueOf("eQ");
		} catch (RuntimeException e) { }
		this.assertEquals(Operator.eq.toString(), "eq");
		this.assertEquals(Operator.eq.toSql(), "=");
	}
}
