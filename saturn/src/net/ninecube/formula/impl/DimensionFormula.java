/**
 * 
 * created on 2007-6-18
 */
package net.ninecube.formula.impl;

import net.ninecube.formula.Formula;

/**
 * 
 * @author jxb
 */
public interface DimensionFormula extends Formula {

	public boolean hasDimension();
	
	public String getDimension(String name);
	
	public String getDimensionValue(String name, String value);
}
