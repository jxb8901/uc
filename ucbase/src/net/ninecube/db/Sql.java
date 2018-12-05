/**
 * 
 * created on 2007-6-28
 */
package net.ninecube.db;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author jxb
 */
public class Sql {
	private String sql;
	private List<Object> parameters = new ArrayList<Object>();
	private int startIndex = 1;
	private int count = Integer.MAX_VALUE;
	
	//~ constructor

	public Sql() {}
	
	public Sql(String sql) {
		this.sql = sql;
	}

	public Sql(String sql, List<Object> parameters) {
		this.sql = sql;
		this.parameters = parameters;
	}
	
	public Sql(String sql, List<Object> parameters, int startIndex, int count) {
		this.sql = sql;
		this.parameters = parameters;
		this.startIndex = startIndex;
		this.count = count;
	}
	
	//~ getter and setter
	
	public List<Object> getParameters() {
		return parameters;
	}
	public void setParameters(List<Object> parameters) {
		this.parameters = parameters;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
}
