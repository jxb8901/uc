/**
 * 
 */
package net.ninecube.formula.exception;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Administrator
 *
 */
public class FormulaValidateException extends FormulaException {
	private List<Exception> all = new ArrayList<Exception>();
	private List<FormulaSyntaxException> formulaSyntaxExceptions = new ArrayList<FormulaSyntaxException>();
	private List<FormulaNotFoundException> formulaNotFoundExceptions = new ArrayList<FormulaNotFoundException>();
	private List<FormulaCycleReferenceException> formulaCycleReferenceExceptions = new ArrayList<FormulaCycleReferenceException>();

	public FormulaValidateException() {
	}

	public List getAll() {
		return all;
	}

	public List getFormulaCycleReferenceExceptions() {
		return formulaCycleReferenceExceptions;
	}

	public List getFormulaNotFoundExceptions() {
		return formulaNotFoundExceptions;
	}

	public List getFormulaSyntaxExceptions() {
		return formulaSyntaxExceptions;
	}
	
	public boolean isEmpty() {
		return all.isEmpty();
	}
	
	public void add(FormulaSyntaxException e) {
		formulaSyntaxExceptions.add(e);
		all.add(e);
	}
	
	public void add(FormulaCycleReferenceException e) {
		formulaCycleReferenceExceptions.add(e);
		all.add(e);
	}
	
	public void add(FormulaNotFoundException e) {
		formulaNotFoundExceptions.add(e);
		all.add(e);
	}

	public String getMessage() {
		return all.toString();
	}
}
