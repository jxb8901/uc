/**
 * created on 2006-4-6
 */
package net.ninecube.formula;

import java.util.List;

import net.ninecube.lang.DateRange;
import net.ninecube.lang.FrequenceDate;


/**
 * 公式引擎
 * @author JXB
 */
public interface FormulaEngine {
	
	public void eval(String targetType, DateRange daterange);
	
	public void eval(List<String> formulaNames, String targetType, DateRange daterange);

	public void eval(List<Formula> formulas, List<Target> targets, DateRange daterange);
	
	public void eval(String targetType, Formula formula, FrequenceDate date);

	public void eval(Formula formula, FrequenceDate date);

	public void validateFormulas();
	
	//~ config
	
	public boolean setSaveToDB(boolean saveToDB);
	
	public boolean setRecalculate(boolean recalculate);
}
