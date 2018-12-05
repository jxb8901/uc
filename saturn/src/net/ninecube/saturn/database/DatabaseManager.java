/**
 * 
 * created on 2006-11-30
 */
package net.ninecube.saturn.database;

import java.util.List;

import net.ninecube.saturn.database.impl.DatabaseManagerImpl;



/**
 * 
 * @author jxb
 * 
 */
public abstract class DatabaseManager {
	private static DatabaseManager instance;
	
	public static DatabaseManager get() {
		if (instance == null) {
			instance = new DatabaseManagerImpl().reload();
		}
		return instance;
	}
	
	public static DatabaseManager setDatabaseManager(DatabaseManager dbm) {
		return instance = dbm;
	}
	
	public DatabaseManager reload() {
		return this;
	}
	public abstract Table getCentricTable();
	
	public abstract List<Association> getAssociations(Table t);
	
	/**
	 * 因为可以有同名的表存在，故本方法返回多个表
	 */
	public abstract List<? extends Table> getTableByAlias(String alias);
	
	/**
	 * TODO：不允许表名和字段别名都相同的字段存在，系统应该作明确的检查
	 */
	public abstract Column getColumnByAlias(String alias);
	
	public abstract DataSet newDataSet();
	
	public abstract String getNamedSql(String name);
}
