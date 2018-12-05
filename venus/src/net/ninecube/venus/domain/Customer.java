/**
 * 
 * created on 2007-3-28
 */
package net.ninecube.venus.domain;

import net.ninecube.core.domain.Entity;

/**
 * 
 * @author jxb
 * 
 */
public class Customer implements Entity {

	private String customerNo;
	private String name;
	private String address;
	
	//~ getter and setter

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
