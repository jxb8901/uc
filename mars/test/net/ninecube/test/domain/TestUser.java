/**
 * 
 * created on 2007-1-14
 */
package net.ninecube.test.domain;

import java.math.BigDecimal;
import java.util.Date;

import net.ninecube.core.domain.Entity;

/**
 * 
 * @author jxb
 * @hibernate.class  table="TestUser" 
 */
public class TestUser implements Entity {

	private Long idno;
	private Long userid;
	private String name;
	private Date creationDate;
	private String description;
	private BigDecimal amount;
	private String password;
	private int staticParam;
	
	//~ getter and setter

	/**
	 * @hibernate.id  generator-class="native"  length="80"
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
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	/**
	 * @hibernate.property
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	/**
	 * @hibernate.property
	 */
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	/**
	 * @hibernate.property
	 */
	public String getDescription() {
		return description;
	}
	public void setDescription(String desc) {
		this.description = desc;
	}
	/**
	 * @hibernate.property
	 */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @hibernate.property
	 */
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getStaticParam() {
		return staticParam;
	}
	public void setStaticParam(int staticParam) {
		this.staticParam = staticParam;
	}
	
}
