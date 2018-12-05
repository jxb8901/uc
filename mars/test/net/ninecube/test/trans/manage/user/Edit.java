/**
 * 
 * created on 2006-12-17
 */
package net.ninecube.test.trans.manage.user;

import net.ninecube.core.trans.Transaction;

/**
 * 
 * @author jxb
 * 
 */
public class Edit extends Transaction {
	private String name;
	private String userid;
	private String password;
	private String password2;
	private int staticParam;
	
	public String executionMethod;

	public String execute() {
		this.executionMethod = "execute";
		return "success";
	}
	
	public String submit() {
		this.executionMethod = "submit";
		return "success";
	}
	
	//~ getter and setter

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password) {
		this.password2 = password;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public int getStaticParam() {
		return staticParam;
	}

	public void setStaticParam(int staticParam) {
		this.staticParam = staticParam;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
