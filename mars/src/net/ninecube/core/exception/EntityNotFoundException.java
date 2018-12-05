/**
 * 
 * created on 2007-2-1
 */
package net.ninecube.core.exception;

import net.ninecube.lang.BaseException;

/**
 * 
 * @author jxb
 * 
 */
public class EntityNotFoundException extends BaseException {

	public EntityNotFoundException() {
		super();
	}

	public EntityNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityNotFoundException(String message) {
		super(message);
	}

	public EntityNotFoundException(Throwable cause) {
		super(cause);
	}

}
