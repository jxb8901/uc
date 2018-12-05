package net.ninecube.core.exception;

import net.ninecube.lang.BaseException;

/**
 * @author mike
 *
 */
public class NoPermissionException extends BaseException {

	public NoPermissionException() {
	}

	public NoPermissionException(String message) {
		super(message);
	}

	public NoPermissionException(Throwable cause) {
		super(cause);
	}

	public NoPermissionException(String message, Throwable cause) {
		super(message, cause);
	}


}
