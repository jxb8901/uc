/**
 * 2005-8-4
 */
package net.ninecube.db;

/**
 * @author JXB
 *
 */
public class DBException extends RuntimeException {

	public DBException() {
		super();
	}
	
	public DBException(String msg) {
		super(msg);
	}

	public DBException(Throwable cause) {
		super(cause);
	}

	public DBException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public String getMessage() {
		return "操作失败:" + super.getMessage();
	}
}
