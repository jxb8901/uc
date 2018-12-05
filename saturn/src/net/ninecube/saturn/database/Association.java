/**
 * 
 * created on 2006-11-30
 */
package net.ninecube.saturn.database;

import org.codehaus.jrc.predicate.Predicate;

/**
 * 
 * @author jxb
 * 
 */
public interface Association {
	
	public Table getOppositeTable(Table table);
	public Column getColumn(Table table);
	public Column getOppositeColumn(Table table);
	public Column getOppositeColumn(Column one);
	
	
	public Predicate getJoinOnPredicate();
}
