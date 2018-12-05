package net.ninecube.core.exception;

import net.ninecube.lang.BaseException;

/**
 * @author mike
 *
 */
public class TaskException extends BaseException {

	public TaskException() {
	}

	public TaskException(String message) {
		super(message);
	}

	public TaskException(Throwable cause) {
		super(cause);
	}

	public TaskException(String message, Throwable cause) {
		super(message, cause);
	}


}
