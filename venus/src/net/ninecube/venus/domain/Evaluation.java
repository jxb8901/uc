/**
 * 
 * created on 2007-2-1
 */
package net.ninecube.venus.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.ninecube.core.domain.Entity;
import net.ninecube.lang.Frequence;

/**
 * @hibernate.meta attribute="auto-generated"
 * @hibernate.class  table="TEvaluation" 
 * @author  jxb
 */
public class Evaluation implements Entity {
	private Long idno;
	private String name;
	// 得分公式
	private String formula;
	private List<Metric> metrics = new ArrayList<Metric>();
	private String type;
	private String description;
	private Date creationDate=new Date();
	private String creator;
	
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
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @hibernate.bag inverse="false" lazy="true" table="TEvalMetrics"
	 * @hibernate.key column="eid"
	 * @hibernate.many-to-many column="mid" class="net.ninecube.venus.domain.Metric"
	 */
	public List<Metric> getMetrics() {
		return metrics;
	}
	public void setMetrics(List<Metric> metrics) {
		this.metrics = metrics;
	}
	public List<Metric> getMetrics(Frequence freq) {
		List<Metric> ret = new ArrayList<Metric>();
		for (Metric m : this.getMetrics())
			if (freq.equals(m.getFrequence()))
				ret.add(m);
		return ret;
	}
	public Set<Frequence> getFrequences() {
		Set<Frequence> ret = new TreeSet<Frequence>();
		for (Metric m : this.getMetrics())
			ret.add(m.getFrequence());
		return ret;
	}
}
