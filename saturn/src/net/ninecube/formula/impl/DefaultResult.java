/**
 * created on 2006-4-6
 */
package net.ninecube.formula.impl;

import java.math.BigDecimal;

import net.ninecube.formula.Context;

/**
 * @author JXB
 */
public class DefaultResult extends AbstractResult {

	public DefaultResult(Context context, BigDecimal result) {
		this.setFormula(context.getFormula());
		this.setTarget(context.getTarget());
		this.setTargetDate(context.getTargetDate());
		this.setValue(result);
	}
}
