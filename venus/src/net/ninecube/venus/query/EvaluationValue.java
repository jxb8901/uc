/**
 * 
 * created on 2007-5-29
 */
package net.ninecube.venus.query;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * 
 * @author jxb
 */
public class EvaluationValue {
	private Object main;
	private Object refer;
	private EvaluationType type;
	
	public EvaluationValue(EvaluationType type, Object main, Object refer) {
		this.type = type;
		this.main = main;
		this.refer = refer;
	}

	public Object getMain() {
		return main;
	}

	public Object getRefer() {
		return refer;
	}

	public EvaluationType getType() {
		return type;
	}
	
	public Object toValue() {
		if (this.main instanceof BigDecimal && this.refer instanceof BigDecimal) {
			BigDecimal main = (BigDecimal) this.main;
			BigDecimal refer = (BigDecimal) this.refer;
			if (type == EvaluationType.Abs) {
				return main.subtract(refer);
			}
			else if (type == EvaluationType.Percent) {
				return main.subtract(refer).divide(main, 4, BigDecimal.ROUND_UP);
			}
		}
		return list(this.main, this.refer);
	}
	
	private static Object list(Object one, Object other) {
		return one + " - " + other;
	}
	
	public String toString() {
		return this.toValue().toString();
	}
}
