/**
 * 
 * created on 2007-4-25
 */
package net.ninecube.venus.query;

import java.math.BigDecimal;

/**
 * 
 * @author jxb
 */
public enum EvaluationType {
	Abs, Percent, List;
	
	public EvaluationValue reference(Object one, Object other) {
		return new EvaluationValue(this, one, other);
	}
}
