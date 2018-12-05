/**
 * 
 * created on 2007-4-23
 */
package net.ninecube.venus.trans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.Preparable;

import net.ninecube.core.exception.ObjectNotFoundException;
import net.ninecube.core.trans.Transaction;
import net.ninecube.core.webwork.util.DateRangeByNameConverter;
import net.ninecube.venus.query.TargetEntity;
import net.ninecube.lang.DateRange;
import net.ninecube.lang.Frequence;
import net.ninecube.lang.FrequenceDate;
import net.ninecube.util.DateUtil;
import net.ninecube.util.StringUtil;
import net.ninecube.venus.domain.CustomerGroup;
import net.ninecube.venus.domain.CustomerGroupManager;
import net.ninecube.venus.domain.Evaluation;
import net.ninecube.venus.domain.EvaluationManager;
import net.ninecube.venus.domain.Metric;
import net.ninecube.venus.domain.PromotionPlanManager;
import net.ninecube.venus.query.Dimension;
import net.ninecube.venus.query.EvaluationValues;
import net.ninecube.venus.query.MetricValue;
import net.ninecube.venus.query.MetricValueManager;
import net.ninecube.venus.query.impl.EvaluationValuesImpl;

/**
 * 
 * @author jxb
 */
public abstract class AbstractEvalTransaction extends Transaction implements Preparable {
	public static final String EVALUATION_VALUES = "VENUS_EVALUATION_VALUES";
	protected MetricValueManager mvm;
	protected CustomerGroupManager cgm;
	protected PromotionPlanManager ppm;
	protected EvaluationManager em;
	
	protected Evaluation evaluation;
	protected Date referDate;
	
	protected List<EvaluationValues> result;

	@Override
	public String execute() {
		Map<Frequence, DateRange> dateranges = getDateRanges();
		if (this.__getCustomerGroup() != null && this.evaluation != null)
			result = this.mvm.evaluate(this.__getCustomerGroup(), this.evaluation,
					dateranges, this.__getReferGroup(), referDate);
		else { // TODO: test
			result = new ArrayList<EvaluationValues>();
			result.add(getTestData(Frequence.DAY, dateranges));
			result.add(getTestData(Frequence.MONTH, dateranges));
			result.add(getTestData(Frequence.YEAR, dateranges));
		}
		putResult2Session();
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	private void putResult2Session() {
		ActionContext.getContext().getSession().put(EVALUATION_VALUES, result);
	}
	
	protected abstract CustomerGroup __getCustomerGroup();
	protected abstract CustomerGroup __getReferGroup();
	
	@SuppressWarnings("unchecked")
	protected Map<Frequence, DateRange> getDateRanges() {
		Map<Frequence, DateRange> ret = new HashMap<Frequence, DateRange>();
		DateRangeByNameConverter.convert(super.getParameters());
		Object o = super.getParameters().get("daterange");
		if (o instanceof DateRange) {
			DateRange r = (DateRange) o;
			ret.put(r.getFrequence(), r);
		}
		else if (o instanceof List) {
			List<DateRange> list = (List<DateRange>) o;
			for (DateRange r : list)
				ret.put(r.getFrequence(), r);
		}
		return ret;
	}
	
	protected static EvaluationValues getTestData(Frequence freq, Map<Frequence, DateRange> dateranges) {
		List<Metric> metrics = new ArrayList<Metric>();
		metrics.add(getMetric("总人数", "x"));
		metrics.add(getMetric("消费额", "*"));
		metrics.add(getMetric("消费笔数", "*"));
		metrics.add(getMetric("网银消费额", "d"));
		
		List<MetricValue> mvs = new ArrayList<MetricValue>();
		DateRange daterange = dateranges.containsKey(freq) ? dateranges.get(freq) :
			new DateRange(new FrequenceDate(new Date(), freq), 10);
		for (FrequenceDate date : daterange) {
			for (Metric m : metrics) {
				mvs.add(getMetricValue(m, date, "" + (int)(Math.random() * 100), "*", "*"));
				if ("*".equals(m.getType())) {
					mvs.add(getMetricValue(m, date, "" + (int)(Math.random() * 100), "交易类型", "POS"));
					mvs.add(getMetricValue(m, date, "" + (int)(Math.random() * 100), "交易类型", "NET"));
					mvs.add(getMetricValue(m, date, "" + (int)(Math.random() * 100), "客户性别", "女"));
					mvs.add(getMetricValue(m, date, "" + (int)(Math.random() * 100), "客户性别", "男"));
				}
			}
		}
		
		return new EvaluationValuesImpl(getTarget(), daterange, metrics, mvs);
	}
	
	protected static TargetEntity getTarget() {
		CustomerGroup ret = new CustomerGroup();
		ret.setName("测试");
		return ret;
	}
	
	protected static Metric getMetric(String name, String type) {
		Metric ret = new Metric();
		ret.setName(name);
		ret.setType(type);
		return ret;
	}
	
	protected static MetricValue getMetricValue(Metric metric, FrequenceDate date, String val, String dimType, String dim) {
		MetricValue ret = new MetricValue();
		ret.setMetric(metric);
		Dimension d = new Dimension();
		d.setType(dimType); d.setValue(dim);
		ret.setDimension(d);
		ret.setValue(new BigDecimal(val));
		ret.setTargetDate(date);
		return ret;
	}

	public void setMetricvalueManager(MetricValueManager mvm) {
		this.mvm = mvm;
	}

	public void setCustomergroupManager(CustomerGroupManager cgm) {
		this.cgm = cgm;
	}

	public void setPromotionplanManager(PromotionPlanManager ppm) {
		this.ppm = ppm;
	}

	public void setEvaluationManager(EvaluationManager em) {
		this.em = em;
	}

	public void setReferDate(Date referDate) {
		this.referDate = referDate;
	}

	public Date getReferDate() {
		return referDate;
	}

	public List<EvaluationValues> getResult() {
		return result;
	}

	public Evaluation getEvaluation() {
		return evaluation;
	}
}
