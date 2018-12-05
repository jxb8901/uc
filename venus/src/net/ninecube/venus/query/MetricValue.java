/**
 * 
 * created on 2007-4-18
 */
package net.ninecube.venus.query;

import java.math.BigDecimal;
import java.util.Date;

import net.ninecube.core.domain.Entity;
import net.ninecube.lang.Frequence;
import net.ninecube.lang.FrequenceDate;
import net.ninecube.venus.domain.Metric;

/**
 * 
 * @hibernate.meta attribute="auto-generated"
 * @hibernate.class  table="MetricValue" 
 * @hibernate.query name="getMetricValues" 
 * 	query="select v from MetricValue v, Evaluation e, Metric m where v.target.id=:targetId \
 * 		and v.target.class=:targetType and v.frequence=:frequence and \
 * 		v.date between :targetDateStart and :targetDateEnd and \
 * 		v.metric in elements(e.metrics) and e.id=:evaluationId and m.frequence=v.frequence"
 * 
 * TODO: 上面的HQL会生成带子查询的SQL，如何写使用JOIN的HQL呢？
 * select v.frequence, v.date, v.dimensionType, v.dimension,
 * 		v.metricId, m.name, v.value 
 * from MetricValue v inner join TEvalMetrics t on t.mid=v.metricId
 * 		inner join TEvaluation e on e.id=t.eid
 * where v.targetType='客户群' and e.id=? and v.targetId=? and
 * 		v.frequence=? and v.date between ? and ?
 * TODO: 上面的HQL语句中的条件“v.target.id=? and v.target.class”本可以写为“v.target=?”
 * 但因hibernate生成出来的SQL语句是“(v.targetId, v.targetType)=?”这样的形式，这样的SQL语句
 * 在mysql下可以运行，但在hsqldb下不能运行，故写为上而复杂形式。
 * 另外上面的条件传递参数targetType时，必须传递与hibernate.meta-value的class部分对应的值，而不能传递
 * 与hibernate.meta-value的value部分对应的值，这点务必注意，否则查询不到任何记录。
 * 
 * @author jxb
 */
public class MetricValue implements Entity {

	private Long id;
	private TargetEntity target;
	private Metric metric;
	/**
	 * @hibernate.property access="field" column="F_Frequence" type="net.ninecube.core.util.FrequenceUserType"
	 */
	private Frequence frequence;
	/**
	 * @hibernate.property access="field" column="F_Date"
	 */
	private Date date;
	private FrequenceDate targetDate;
	private Dimension dimension;
	private BigDecimal value;

	/**
	 * @hibernate.component
	 */
	public Dimension getDimension() {
		return dimension;
	}
	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}
	/**
	 * @hibernate.id  generator-class="native" column="F_Id"
	 */
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @hibernate.many-to-one column="F_MetricId"
	 */
	public Metric getMetric() {
		return metric;
	}
	public void setMetric(Metric metric) {
		this.metric = metric;
	}
	/**
	 * @hibernate.any id-type="long" meta-type="string"
	 * @hibernate.meta-value value="CustomerGroup" class="net.ninecube.venus.domain.CustomerGroup"
	 * @hibernate.meta-value value="PromotionPlan" class="net.ninecube.venus.domain.PromotionPlan"
	 * @hibernate.any-column name="F_TargetType"
	 * @hibernate.any-column name="F_TargetId"
	 */
	public TargetEntity getTarget() {
		return target;
	}
	public void setTarget(TargetEntity target) {
		this.target = target;
	}
	/**
	 * @hibernate.property column="F_QuotaValue"
	 */
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	public FrequenceDate getTargetDate() {
		if (this.targetDate == null)
			this.targetDate = new FrequenceDate(this.date, this.frequence);
		return targetDate;
	}
	public void setTargetDate(FrequenceDate targetDate) {
		this.frequence = targetDate.getFrequence();
		this.date = targetDate.toDate();
	}
}
