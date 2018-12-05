package net.ninecube.saturn.database.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import net.ninecube.saturn.database.Association;
import net.ninecube.saturn.database.Column;
import net.ninecube.saturn.database.DataSet;
import net.ninecube.saturn.database.DataSetUtil;
import net.ninecube.saturn.database.DatabaseManager;
import net.ninecube.saturn.database.Table;
import net.ninecube.saturn.database.exception.ColumnNotFoundException;
import net.ninecube.saturn.database.exception.TableNotFoundException;
import net.ninecube.util.StringUtil;

import org.codehaus.jrc.contribute.Jrc;

public class DatabaseManagerImpl extends DatabaseManager {

	private String configFileName = "database.xml";
	private String propertiesFileName = "database";
	private List<DatabaseImpl> databases = new ArrayList<DatabaseImpl>();
	private String type;
	
	public DatabaseManagerImpl() {	}
	
	public DatabaseManagerImpl(String configFileName) {			
		this.configFileName = configFileName;
	}
	
	public DatabaseManager reload() {
		XmlConfigParser.parse(this, this.configFileName);
		for (DatabaseImpl db : this.databases) {
			db.postProcess(this);
		}
		if (!StringUtil.isEmpty(this.getType()))
			Jrc.setDefaultSqlGeneratorName(this.getType());
		
		return this;
	}
	
	public String getNamedSql(String name) {
		String ret = this.getProperty(this.propertiesFileName+"_" +this.getType(), name);
		if (ret == null) ret = this.getProperty(this.propertiesFileName, name);
		return ret;
	}
	
	protected String getProperty(String file, String name) {
		try {
			return ResourceBundle.getBundle(file).getString(name);
		} catch (MissingResourceException e) {
			return null;
		}
	}
	
	public Table getCentricTable(){
		for(DatabaseImpl db : this.databases){
			Table tb = db.getCentricTable();
			if(tb != null) return tb;
		}
		throw new TableNotFoundException("Can't find centric Table ! ");
	}
	
	public DataSet newDataSet() {
		WhereDataSet ret = new WhereDataSet(this);
		return ret;
	}
	
	public List<TableImpl> getTableByAlias(String alias) {
		List<TableImpl> list = new ArrayList<TableImpl>();
		for (DatabaseImpl d : this.databases) {
			for (TableImpl t : d.getTables()) {
				if (t.hasAlias(alias)) list.add(t);
			}
		}
		return list;
	}
	
	public Column getColumnByAlias(String alias) {
		if (StringUtil.isEmpty(StringUtil.getFirstByDot(alias))) throw new ColumnNotFoundException("字段名必须是'表名.字段名'的形式:" + alias);
		List<TableImpl> tables = getTableByAlias(StringUtil.getFirstByDot(alias));
		if (tables.isEmpty()) throw new TableNotFoundException(alias);
		for (TableImpl t : tables) {
			try {
				return t.getTableColumnByAlias(StringUtil.getSecondByDot(alias));
			} catch (ColumnNotFoundException e) {
			}
		}
		throw new ColumnNotFoundException(alias);
	}

	public DatabaseImpl getDatabaseByName(String name) {
		for (DatabaseImpl d : this.databases) {
			if (d.getName().equals(name)) return d;
		}
		return null;
	}
	
	public List<Association> getAssociations(Table t) {
		return DataSetUtil.getAssociations(t, this.getTables());
	}
	
	public List<? extends Table> getTables() {
		List<TableImpl> ret = new ArrayList<TableImpl>();
		for (DatabaseImpl d : this.databases) {
			ret.addAll(d.getTables());
		}
		return ret;
	}

	public List<DatabaseImpl> getDatabases() {
		return databases;
	}
	
	public void setDatabases(List<DatabaseImpl> databases) {
		this.databases = databases;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
