/**
 * created on 2006-4-6
 */
package net.ninecube.formula.exception;


/**
 * 表示公式语法错误的异常类
 * @author JXB
 */
public class FormulaSyntaxException extends FormulaException {

	public FormulaSyntaxException() {
		super();
	}

	public FormulaSyntaxException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public FormulaSyntaxException(String arg0) {
		super(arg0);
	}

	public FormulaSyntaxException(Throwable arg0) {
		super(arg0);
	}

}
