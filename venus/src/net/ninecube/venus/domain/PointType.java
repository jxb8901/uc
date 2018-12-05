/**
 * 
 * created on 2007-4-25
 */
package net.ninecube.venus.domain;

import net.ninecube.core.domain.Entity;

/**
 * 
 * @hibernate.meta attribute="auto-generated"
 * @hibernate.class  table="TPointType" 
 * @author jxb
 */
public class PointType implements Entity {

	private Long idno;
	private String name;
	private String description;

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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	
}
