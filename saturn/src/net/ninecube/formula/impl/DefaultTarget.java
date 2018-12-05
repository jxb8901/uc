/**
 * 
 * created on 2007-3-19
 */
package net.ninecube.formula.impl;

import net.ninecube.formula.Target;
import net.ninecube.saturn.database.DataSet;

/**
 * 
 * @author jxb
 * 
 */
public class DefaultTarget implements Target {
	private Long id;
	private String name;
	private String type;
	private String rule;
	private DataSet filter;

	public DefaultTarget(Long id, String name, String type, String rule, DataSet filter) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.rule = rule;
		this.filter = filter;
	}

	public DataSet getFilter() {
		return filter;
	}

	public void setFilter(DataSet filter) {
		this.filter = filter;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getRule() {
		return rule;
	}

	public String getType() {
		return type;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public void setType(String type) {
		this.type = type;
	}

}
