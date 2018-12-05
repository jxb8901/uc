/**
 * 
 * created on 2007-4-25
 */
package net.ninecube.venus.query;

import java.math.BigDecimal;

import junit.framework.TestCase;

/**
 * 
 * @author jxb
 */
public class EvaluationTypeTest extends TestCase {

	public void testReference() {
		this.assertReferenceEquals(EvaluationType.Abs, "10", "30", "20");
		this.assertReferenceEquals(EvaluationType.Percent, "0.3334", "30", "20");
	}

	private static void assertReferenceEquals(EvaluationType type, String except, String one, String other) {
		assertEquals(except, type.reference(new BigDecimal(one), new BigDecimal(other)).toString());
	}
}
