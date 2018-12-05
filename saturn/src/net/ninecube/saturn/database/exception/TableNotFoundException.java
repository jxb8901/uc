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
public class TableNotFoundException extends DatabaseException {
	private String tableName;

	public TableNotFoundException(String tableName) {
		super();
		this.tableName = tableName;
	}
	
	public String getMessage() {
		return "can't find table for alias '" + tableName + "'";
	}
}
