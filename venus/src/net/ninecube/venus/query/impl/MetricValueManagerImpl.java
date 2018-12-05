/**
 * 
 * created on 2007-4-18
 */
package net.ninecube.venus.query.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.ninecube.core.domain.AbstractEntityManager;
import net.ninecube.venus.query.TargetEntity;
import net.ninecube.lang.DateRange;
import net.ninecube.lang.Frequence;
import net.ninecube.lang.FrequenceDate;
import net.ninecube.venus.domain.CustomerGroup;
import net.ninecube.venus.domain.Evaluation;
import net.ninecube.venus.domain.Metric;
import net.ninecube.venus.query.EvaluationValues;
import net.ninecube.venus.query.MetricValue;
import net.ninecube.venus.query.MetricValueManager;

/**
 * 
 * @author jxb
 */
public class MetricValueManagerImpl extends AbstractEntityManager<MetricValue> implements MetricValueManager {
	private static final Log log = LogFactory.getLog(MetricValueManagerImpl.class);

	public List<MetricValue> find(TargetEntity target, Evaluation evaluation, DateRange daterange) {
		String targetType = getTargetClass(target);
		List<MetricValue> ret = super.findByNamedQuery("getMetricValues", 
				new String[]{"targetId", "targetType", "frequence", "targetDateStart", 
				"targetDateEnd", "evaluationId"}, 
				new Object[]{target.getId(), targetType, daterange.getFrequence(),
				daterange.getStart().toDate(), daterange.getEnd().toDate(),
				evaluation.getIdno()});
		if (log.isDebugEnabled())
			log.debug("find metric values, parameters:[target="+target.getId()+
					",targettype=" + targetType + ",evaluation="+evaluation.getIdno()+
					",daterange=" + daterange+"], result size: " + ret.size());
		return ret;
	}
	
	private static String getTargetClass(TargetEntity target) {
		String ret = target.getClass().getName();
		int index = ret.indexOf("$");
		if (index != -1) {
			log.info("Class被代理过，偿试找出代理前的类：" + ret);
			ret = ret.substring(0, index);
		}
		return ret;
	}
	
	public EvaluationValues evaluate(CustomerGroup customerGroup, Evaluation e, DateRange daterange) {
		if (daterange.size() > 100) {
			daterange = ajustDateRange(daterange, 100);
		}
		return new EvaluationValuesImpl(customerGroup, daterange, 
				e.getMetrics(daterange.getFrequence()), 
				this.find(customerGroup, e, daterange));
	}

	public List<EvaluationValues> evaluate(CustomerGroup customerGroup, Evaluation e) {
		Map<Frequence, DateRange> dateranges = Collections.emptyMap();
		return evaluate(customerGroup, e, dateranges, null, null);
	}
	
	public List<EvaluationValues> evaluate(CustomerGroup customerGroup, Evaluation e, 
			Map<Frequence, DateRange> dateranges,  CustomerGroup referTarget, Date referDate) {
		List<EvaluationValues> ret = new ArrayList<EvaluationValues>();
		Set<Frequence> freqs = e.getFrequences();
		for (Frequence f : freqs) {
			DateRange dr = dateranges.containsKey(f) ? dateranges.get(f) : getDefaultDateRange(f);
			EvaluationValues values = evaluate(customerGroup, e, dr);
			if (referDate != null) {
				FrequenceDate fd = new FrequenceDate(referDate, f);
				DateRange referDateRange = new DateRange(fd, fd.add(dr.size()));
				values = new ReferenceEvaluationValues(values, evaluate(customerGroup, e, referDateRange)); // 产生和基期的对比
				if (referTarget != null) {
					EvaluationValues refByTarget = evaluate(referTarget, e, dr);
					refByTarget = new ReferenceEvaluationValues(refByTarget, evaluate(referTarget, e, referDateRange)); // 产生另一客户群的基期对比
					values = new ReferenceEvaluationValues(values, refByTarget); // 产生和另一客户群的相对基期对比的增量
				}
			}
			else if (referTarget != null) {
				values = new ReferenceEvaluationValues(values, evaluate(referTarget, e, dr)); // 产生和另一客户群的对比
			}
			ret.add(values);
		}
		return ret;
	}

	public List<EvaluationValues> evaluate(CustomerGroup customerGroup) {
		return evaluate(customerGroup, customerGroup.getEvaluation());
	}
	
	protected DateRange getDefaultDateRange(Frequence frequence) {
		FrequenceDate end = new FrequenceDate(new Date(), frequence);
		int size = 2;
		if (Frequence.DAY.equals(frequence)) size = 10;
		else if (Frequence.MONTH.equals(frequence)) size = 6;
		else if (Frequence.QUARTER.equals(frequence)) size = 4;
		else if (Frequence.YEAR.equals(frequence)) size = 2;
		FrequenceDate start = end.add(0 - size);
		return new DateRange(start, end);
	}
	
	/**
	 * TODO: 如果日期范太大则作出调整，以控制数据查询返回的数据量
	 */
	protected static DateRange ajustDateRange(DateRange daterange, int i) {
		Date today = new Date();
		Date endDate = daterange.getEnd().getStartDate();
		if (endDate.compareTo(today) > 0) endDate = today;
		FrequenceDate end = new FrequenceDate(endDate, daterange.getFrequence());
		FrequenceDate start = end.add(1 - i);
		if (start.compareTo(daterange.getStart()) < 0) start = daterange.getStart();
		
		return new DateRange(start, end);
	}
}
