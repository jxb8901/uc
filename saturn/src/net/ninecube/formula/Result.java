/**
 * created on 2006-4-6
 */
package net.ninecube.formula;

import java.math.BigDecimal;

import net.ninecube.lang.FrequenceDate;

/**
 * 公式求值结果，结果可能为空值、单值及多值
 * 计算结果保存在数据库的表中时，结果可能为空值
 * @author JXB
 * @TODO: 多值结果、空值结果
 */
public interface Result {
	
	public Formula getFormula();
	
	public Target getTarget();
	
	public FrequenceDate getTargetDate();

	/**
	 * 取计算结果，只在单值时有效
	 */
	public BigDecimal getValue();
}
