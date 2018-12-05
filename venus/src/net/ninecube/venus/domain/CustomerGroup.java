/**
 * 
 * created on 2006-10-24
 */
package net.ninecube.venus.domain;

import java.util.Date;
import java.util.Set;

import net.ninecube.core.domain.Entity;
import net.ninecube.venus.query.TargetEntity;
import net.ninecube.saturn.database.DataSet;

/**
 * @hibernate.meta attribute="auto-generated"
 * @hibernate.class  table="TCustomerGroup" 
 * @author  jxb
 */
public class CustomerGroup implements Entity, TargetEntity {
	private Long idno;
	private String name;
	private CustomerGroup parent;
	private Set<CustomerGroup> children;
	// 评价规则
	private String rule;
	// 评价体系
	private Evaluation evaluation;
	private String description;
	private Date creationDate=new Date();
	private String creator;
	
	public String getRealRule() {
		if (this.parent == null) return this.getRule();
		return "(" + this.parent.getRealRule() + ") and (" + this.getRule() + ")";
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @hibernate.id  generator-class="native"
	 */
	public Long getIdno() {
		return idno;
	}
	public void setIdno(Long id) {
		this.idno = id;
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
	 * @hibernate.property column="frule"
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
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @hibernate.many-to-one
	 */
	public Evaluation getEvaluation() {
		return evaluation;
	}
	public void setEvaluation(Evaluation evaluation) {
		this.evaluation = evaluation;
	}
	/**
	 * @hibernate.many-to-one column="parent"
	 */
	public CustomerGroup getParent() {
		return parent;
	}
	public void setParent(CustomerGroup parent) {
		this.parent = parent;
	}
	/**
	 * @hibernate.set inverse="true" lazy="true"
	 * @hibernate.key column="parent" 
	 * @hibernate.one-to-many class="net.ninecube.venus.domain.CustomerGroup"
	 */
	public Set<CustomerGroup> getChildren() {
		return children;
	}
	public void setChildren(Set<CustomerGroup> children) {
		this.children = children;
	}
	
	//~ impelements Target
	
	public Long getId() {
		return this.getIdno();
	}
	public String getType() {
		return this.getClass().getSimpleName();
	}
	
}
