/**
 * 
 * created on 2007-4-11
 */
package net.ninecube.saturn.database.exception;

import net.ninecube.saturn.database.DatabaseException;
import net.ninecube.saturn.database.Table;
import net.ninecube.saturn.database.impl.TableImpl;

/**
 * 
 * @author jxb
 * 
 */
public class ColumnNotFoundException extends DatabaseException {
	private String name;
	
	public ColumnNotFoundException(String name) {
		this.name = name;
	}
	
	public ColumnNotFoundException(Table table, String name) {
		this.name = table.getName() + "." + name;
	}

	public String getMessage() {
		return "can't find column '" + this.name + "'";
	}
}
