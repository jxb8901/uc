/**
 * created on 2006-4-6
 */
package net.ninecube.formula.exception;

import net.ninecube.lang.BaseException;

/**
 * 表示公式语法错误的异常类
 * @author JXB
 */
public class FormulaException extends BaseException {

	public FormulaException() {
		super();
	}

	public FormulaException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public FormulaException(String arg0) {
		super(arg0);
	}

	public FormulaException(Throwable arg0) {
		super(arg0);
	}

}
