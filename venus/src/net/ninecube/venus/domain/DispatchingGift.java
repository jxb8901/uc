/**
 * 
 * created on 2007-3-28
 */
package net.ninecube.venus.domain;

import java.util.Date;

import net.ninecube.core.domain.Entity;

/**
 * @hibernate.meta attribute="auto-generated"
 * @hibernate.class  table="TDispatchingGift" 
 * @author jxb
 * 
 */
public class DispatchingGift implements Entity {

	private Long idno;
	private Gift gift;
	private Customer customer;
	private int quantity;
	private Date applyDate;
	private int status;
	
	//~ getter and setter

	/**
	 * @hibernate.property
	 */
	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	/**
	 * @hibernate.many-to-one
	 */
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	/**
	 * @hibernate.many-to-one
	 */
	public Gift getGift() {
		return gift;
	}
	public void setGift(Gift gift) {
		this.gift = gift;
	}
	/**
	 * @hibernate.id  generator-class="native"
	 */
	public Long getIdno() {
		return idno;
	}
	public void setIdno(Long idno) {
		this.idno = idno;
	}
	/**
	 * @hibernate.property
	 */
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	/**
	 * @hibernate.property
	 */
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
