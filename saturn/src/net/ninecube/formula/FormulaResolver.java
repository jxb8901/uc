/**
 * 
 * created on 2007-3-18
 */
package net.ninecube.formula;

import java.util.List;

import net.ninecube.formula.exception.FormulaNotFoundException;
import net.ninecube.lang.Frequence;

/**
 * 
 * @author jxb
 * 
 */
public interface FormulaResolver {

	/**
	 * 根据名称查找公式，如果找不到抛出异常
	 * @exception FormulaNotFoundException
	 */
	public Formula resolve(String formulaRef) throws FormulaNotFoundException;

	/**
	 * 取指定频度和计算对象类型的所有公式
	 */
	public List<Formula> getFormulaByTargetTypeAndFrequence(String targetType, Frequence freq);

}
