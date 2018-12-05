/**
 * 
 * created on 2006-11-30
 */
package net.ninecube.saturn;

import net.ninecube.saturn.database.DataSet;

/**
 * 
 * @author jxb
 * 
 */
public interface Parser {

	public DataSet parse(String s);
}
