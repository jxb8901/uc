package net.ninecube.venus.query.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ninecube.lang.DateRange;
import net.ninecube.lang.Frequence;
import net.ninecube.venus.domain.Metric;
import net.ninecube.venus.query.EvaluationType;
import net.ninecube.venus.query.EvaluationValues;
import net.ninecube.venus.query.MetricValue;
import net.ninecube.venus.query.TargetEntity;

/**
 * 展示评价结果的值对象，一个对象实例仅表示一个频度的指标值
 * 
 * @author fox
 */
public class EvaluationValuesImpl implements EvaluationValues{
	private TargetEntity target;
	private DateRange dateRange;
	private List<MetricInfoImpl> metrics = new ArrayList<MetricInfoImpl>();
	
	public EvaluationValuesImpl(TargetEntity target, DateRange dateRange, List<Metric> metrics, List<MetricValue> metricvalues) {
		this.target = target;
		this.dateRange = dateRange;
		for (Metric m : metrics) this.metrics.add(new MetricInfoImpl(m, this.dateRange.size()));
		
		for (MetricValue mv : metricvalues) {
			int dateIndex = this.dateRange.getIndex(mv.getTargetDate().toDate());
			MetricInfoImpl mi = this.getMetricInfo(mv.getMetric());
			mi.addValue(dateIndex, mv.getDimension(), mv.getValue());
		}
	}
	
	private MetricInfoImpl getMetricInfo(Metric metric) {
		for (MetricInfoImpl mi : this.metrics) {
			if (mi.getMetric().equals(metric)) return mi;
		}
		throw new IllegalArgumentException("invalid metric: '" + metric);
	}

	public BigDecimal[] getValues(String metric) {
		for (MetricInfoImpl mi : this.metrics) {
			if (mi.getMetric().getName().equals(metric)) return mi.getTotalValues();
		}
		throw new IllegalArgumentException("invalid metric: '" + metric);
	}

	public BigDecimal[] getValues(String metric, String dimensionType, String dimension) {
		for (MetricInfoImpl mi : this.metrics) {
			if (mi.getMetric().getName().equals(metric)) 
				return mi.getDimensionValues(dimensionType, dimension);
		}
		throw new IllegalArgumentException("invalid metric: '" + metric);
	}

	public BigDecimal[] getValues(Date date) {
		return getValues(this.dateRange.getIndex(date));
	}

	public BigDecimal[] getValues(int dateIndex) {
		List<BigDecimal> ret = new ArrayList<BigDecimal>();
		for (MetricInfoImpl mi : this.metrics) {
			ret.addAll(Arrays.asList(mi.getValuesByDate(dateIndex)));
		}
		return ret.toArray(new BigDecimal[0]);
	}
	
	public Frequence getFrequence() {
		return this.dateRange.getFrequence();
	}
	
	public List<? extends MetricInfo> getMetrics(){
		return metrics;
	}

	public DateRange getDateRange() {
		return dateRange;
	}

	public TargetEntity getTarget() {
		return target;
	}
}
