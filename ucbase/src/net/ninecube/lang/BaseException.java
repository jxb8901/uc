/**
 * 
 * created on 2007-1-12
 */
package net.ninecube.lang;

/**
 * 
 * @author jxb
 * 
 */
public class BaseException extends RuntimeException {

	public BaseException() {
	}

	public BaseException(String message) {
		super(message);
	}

	public BaseException(Throwable cause) {
		super(cause);
	}

	public BaseException(String message, Throwable cause) {
		super(message, cause);
	}

}