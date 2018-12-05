/**
 * 
 */
package net.ninecube.formula.exception;


/**
 * @author Administrator
 *
 */
public class FormulaCycleReferenceException extends FormulaSyntaxException {

	private String formulaName;
	
	public FormulaCycleReferenceException(String formulaName) {
		super();
		this.formulaName = formulaName;
	}

	public String getMessage() {
		return "公式出现循环引用["+formulaName+"]";
	}
}
