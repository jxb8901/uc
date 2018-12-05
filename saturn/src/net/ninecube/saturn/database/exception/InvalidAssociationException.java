/**
 * 
 * created on 2007-4-11
 */
package net.ninecube.saturn.database.exception;

import net.ninecube.saturn.database.DatabaseException;

/**
 * 
 * @author jxb
 * 
 */
public class InvalidAssociationException extends DatabaseException {

	public InvalidAssociationException() {
		super();
	}

	public InvalidAssociationException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidAssociationException(String message) {
		super(message);
	}

	public InvalidAssociationException(Throwable cause) {
		super(cause);
	}
}
