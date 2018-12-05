/**
 * 
 * created on 2007-4-12
 */
package net.ninecube.saturn.database;

/**
 * 
 * @author jxb
 * 
 */
public interface MutableTable extends Table {

	public void setPrimaryKey(Column key);
	
	public MutableTable addColumn(Column c);
	
	public Column addColumn(String name, String[] alias);
	
	public Association addAssociation(Column self, Column other);
}
