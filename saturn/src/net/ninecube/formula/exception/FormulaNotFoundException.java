/**
 * created on 2006-4-11
 */
package net.ninecube.formula.exception;


/**
 * @author JXB
 */
public class FormulaNotFoundException extends FormulaException {
	
	private String formulaName;

	public FormulaNotFoundException(String formulaName) {
		super(formulaName);
		this.formulaName = formulaName;
	}

	public FormulaNotFoundException(String formulaName, Throwable arg1) {
		super(formulaName, arg1);
		this.formulaName = formulaName;
	}
	
	public String getFormulaName() {
		return this.formulaName;
	}

	public String getMessage() {
		return "找不到公式定义["+super.getMessage()+"]";
	}
}
