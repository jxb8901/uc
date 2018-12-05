/**
 * 
 * created on 2007-2-2
 */
package net.ninecube.saturn.database;

import net.ninecube.lang.BaseException;

/**
 * 
 * @author jxb
 * 
 */
public class DatabaseException extends BaseException {

	public DatabaseException() {
	}

	public DatabaseException(String message) {
		super(message);
	}

	public DatabaseException(Throwable cause) {
		super(cause);
	}

	public DatabaseException(String message, Throwable cause) {
		super(message, cause);
	}

}
