package net.ninecube.saturn.database;

import java.util.List;

public interface Database {

	public String getType();
	
	public String getVersion();
	
	public String getName();
	
	public String getUrl();
	
	public String getPort();
	
	public String getUser();
	
	public String getPassword();
	
	public List<? extends Table> getTables();
	
}
