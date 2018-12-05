/**
 * 
 * created on 2007-1-12
 */
package net.ninecube.core.exception;

import net.ninecube.lang.BaseException;

/**
 * 
 * @author jxb
 * 
 */
public class ObjectNotFoundException extends BaseException {

	public ObjectNotFoundException() {
	}

	public ObjectNotFoundException(String message) {
		super(message);
	}

	public ObjectNotFoundException(Throwable cause) {
		super(cause);
	}

	public ObjectNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
