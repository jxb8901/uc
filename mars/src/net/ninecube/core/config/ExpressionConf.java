/**
 * 
 * created on 2006-12-14
 */
package net.ninecube.core.config;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author jxb
 * 
 */
public class ExpressionConf implements Serializable, ActionsAware {

	private String name;
	private String cname;
	private List<String> action;
	private String fieldName;
	private String java;
	private String javascript;
	private String errormsg;
	private List<String> depends;
	
	//~ getter and setter
	
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public List<String> getDepends() {
		return depends;
	}
	public void setDepends(List<String> depends) {
		this.depends = depends;
	}
	public String getErrormsg() {
		return errormsg;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
	public String getJava() {
		return java;
	}
	public void setJava(String java) {
		this.java = java;
	}
	public String getJavascript() {
		return javascript;
	}
	public void setJavascript(String javascript) {
		this.javascript = javascript;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public List<String> getAction() {
		return action;
	}
	public void setAction(List<String> action) {
		this.action = action;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{Expression: ");
		sb.append("name=").append(this.name).append(", ");
		sb.append("cname=").append(this.cname).append(", ");
		if (null != this.java) sb.append("java=").append(this.java).append(", ");
		if (null != this.javascript) sb.append("javascript=").append(this.javascript).append(", ");
		if (null != this.fieldName) sb.append("fieldName=").append(this.fieldName).append(", ");
		sb.append("}");
		return sb.toString();
	}
	
}
