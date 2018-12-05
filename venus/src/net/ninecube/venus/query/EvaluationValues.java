package net.ninecube.venus.query;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ninecube.lang.DateRange;
import net.ninecube.lang.Frequence;
import net.ninecube.venus.domain.Metric;

/**
 * 展示评价结果的值对象，一个对象实例仅表示一个频度的指标值
 * 
 * @author fox
 */
public interface EvaluationValues {
	public TargetEntity getTarget();
	
	/**
	 * 对于有维度的指标来说，本方法用于取汇总指标值
	 */
	public Object[] getValues(String metric);
	/**
	 * 用于有维度的指标，取维度指标值
	 */
	public Object[] getValues(String metric, String dimensionType, String dimensionValue);

	public Object[] getValues(Date date);
	
	public Object[] getValues(int dateIndex);
		
	public Frequence getFrequence();
	
	public List<? extends MetricInfo> getMetrics();

	public DateRange getDateRange();
	
	public static interface MetricInfo {
		public Metric getMetric();
		public String[] getDimensionTypes();
		public String[] getDimensions(String type);
		public String[] getDimensions();
	}
}
