/**
 * 
 * created on 2007-6-22
 */
package net.ninecube.formula;

import net.ninecube.formula.impl.AbstractFormula;

/**
 * 
 * @author jxb
 */
public interface FormulaFactory {

	public Formula createFormula(Long id, String name, String formula);
	
}
