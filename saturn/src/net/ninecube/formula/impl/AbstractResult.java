/**
 * created on 2006-4-6
 */
package net.ninecube.formula.impl;

import java.math.BigDecimal;

import net.ninecube.formula.Formula;
import net.ninecube.formula.Result;
import net.ninecube.formula.Target;
import net.ninecube.lang.FrequenceDate;

/**
 * @author JXB
 */
public abstract class AbstractResult implements Result {
	
	private Formula formula;
	private Target target;
	private FrequenceDate targetDate;
	private BigDecimal value;

	public BigDecimal getValue() {
		return value;
	}
	
	public void setValue(BigDecimal b) {
		this.value = b;
	}

	public Formula getFormula() {
		return formula;
	}

	public void setFormula(Formula formula) {
		this.formula = formula;
	}

	public Target getTarget() {
		return target;
	}

	public void setTarget(Target target) {
		this.target = target;
	}

	public FrequenceDate getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(FrequenceDate targetDate) {
		this.targetDate = targetDate;
	}
}
