/**
 * 
 * created on 2007-1-8
 */
package net.ninecube.venus.domain;

import java.util.Date;
import java.util.Map;

import net.ninecube.core.domain.Entity;

/**
 * 
 * @hibernate.meta attribute="auto-generated"
 * @hibernate.class  table="TPromotionPlan" 
 * @hibernate.query name="getValidPromotionPlan" 
 * 	query="from PromotionPlan where :date >= startDate and :date <= endDate"
 * @author jxb
 * 
 */
public class PromotionPlan implements Entity {

	private Long idno;
	private String name;
	private CustomerGroup target;
	private String description;
	private RuleEntity rule;
	private Map<String, String> ruleParams;
	private Evaluation evaluation;
	private String status;
	private String creator;
	private Date creationDate;
	private Date startDate;
	private Date endDate;
	
	
	// TODO: 营销计划启动、停止
	
	//~ getter and setter

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
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
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
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @hibernate.many-to-one column="frule" not-null="false"
	 */
	public RuleEntity getRule() {
		return rule;
	}
	public void setRule(RuleEntity rule) {
		this.rule = rule;
	}
	/**
	 * @hibernate.property
	 */
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * @hibernate.property
	 */
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @hibernate.many-to-one not-null="true"
	 */
	public CustomerGroup getTarget() {
		return target;
	}
	public void setTarget(CustomerGroup target) {
		this.target = target;
	}
	
	/**
	 * 
	 * @hibernate.map  table="PromotionRuleParams"  lazy="true"  cascade="all"
	 * @hibernate.key  column="PID"
	 * @hibernate.index  column="name"  type="java.lang.String"  length="80"
	 * @hibernate.element    column="val"  type="java.lang.String"
	 */
	public Map<String, String> getRuleParams() {
		return ruleParams;
	}
	public void setRuleParams(Map<String, String> ruleParams) {
		this.ruleParams = ruleParams;
	}
	
}
