package net.ninecube.saturn.database.impl;

import java.util.ArrayList;
import java.util.List;

import net.ninecube.saturn.database.Database;

public class DatabaseImpl implements Database {

	private String type;
	private String version;
	private String name;
	private String url;
	private String port;
	private String user;
	private String password;
	private List<TableImpl> tables = new ArrayList<TableImpl>();
	
	//~

	public void postProcess(DatabaseManagerImpl dbm) {
		for (TableImpl t : this.tables) {
			t.postProcess(dbm);
		}
	}
	
	public TableImpl getCentricTable(){
		for(TableImpl tb : this.tables)
			if(tb.isCentric()) return tb;
		return null;
	}
	//~ getter and setter
	
	public String getType() {		
		return type;
	}

	public String getVersion() {		
		return version;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public List<TableImpl> getTables() {
		return tables;
	}

	public DatabaseImpl addTable(TableImpl table) {
		this.tables.add(table);
		table.setOwner(this);
		return this;
	}
	
}
