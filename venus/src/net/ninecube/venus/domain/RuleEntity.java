/**
 * 
 * created on 2007-3-4
 */
package net.ninecube.venus.domain;

import net.ninecube.core.domain.Entity;

/**
 * 
 * @hibernate.meta attribute="auto-generated"
 * @hibernate.class  table="TRules" 
 * @author jxb
 * 
 */
public class RuleEntity implements Entity {

	private Long idno;
	private String name;
	private String mapping;
	private String rule;
	private String type;
	private String description;
	
	//~ getter and setter
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @hibernate.property column="frule" 
	 * type="org.springframework.orm.hibernate3.support.ClobStringType"
	 * lazy="true"
	 */
	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	/**
	 * @hibernate.property
	 */
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * @hibernate.property 
	 * type="org.springframework.orm.hibernate3.support.ClobStringType"
	 * lazy="true"
	 */
	public String getMapping() {
		return mapping;
	}
	public void setMapping(String mapping) {
		this.mapping = mapping;
	}
	
}
