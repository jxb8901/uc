/**
 * 
 * created on 2007-2-1
 */
package net.ninecube.venus.domain;

import net.ninecube.core.domain.Entity;
import net.ninecube.lang.Frequence;

/**
 * @hibernate.meta attribute="auto-generated"
 * @hibernate.class  table="TMetric" 
 * @hibernate.query name="getMetricByName" 
 * 	query="from Metric where name=:name"
 * @hibernate.query name="getMetricByFrequence" 
 * 	query="from Metric where frequence=:frequence"
 * @hibernate.query name="getMetricByTypeAndFrequence" 
 * 	query="from Metric where type=:type and frequence=:frequence"
 * @author  jxb
 */
public class Metric implements Entity {
	private Long idno;
	private String name;
	/** 公式 */
	private String formula;
	private String description;
	/** 计算对象类型 */
	private String type;
	private Frequence frequence;
	/** 单位 */
	private String unit;
	/** 换算比例 */
	private String ratio;
	
	//~ getter and setter

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
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
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
	 * @hibernate.property type="net.ninecube.core.util.FrequenceUserType"
	 */
	public Frequence getFrequence() {
		return frequence;
	}
	public void setFrequence(Frequence frequence) {
		this.frequence = frequence;
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
	 * @hibernate.property
	 */
	public String getRatio() {
		return ratio;
	}
	public void setRatio(String ratio) {
		this.ratio = ratio;
	}
	/**
	 * @hibernate.property
	 */
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
}
