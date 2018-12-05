/**
 * 
 * created on 2007-4-16
 */
package net.ninecube.venus.query.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.ninecube.lang.DateRange;
import net.ninecube.lang.FrequenceDate;
import net.ninecube.util.DateUtil;
import net.ninecube.venus.domain.CustomerGroup;
import net.ninecube.venus.domain.Metric;
import net.ninecube.venus.query.Dimension;
import net.ninecube.venus.query.EvaluationValues;
import net.ninecube.venus.query.MetricValue;
import net.ninecube.venus.query.EvaluationValues.MetricInfo;
import net.ninecube.venus.query.impl.EvaluationValuesImpl;
import junit.framework.TestCase;

/**
 * 
 * @author jxb
 * 
 */
public class EvaluationValuesTest extends TestCase {
	
	public void testGetValues() {
		EvaluationValues vs = getTestData();
		this.assertEquals(vs.getDateRange().size(), vs.getValues(vs.getMetrics().get(0).getMetric().getName()).length);
		this.assertEquals(6, vs.getValues(new FrequenceDate("D20070103").toDate()).length);
	}
	
	public void testAPIUsage() {
		EvaluationValues vs = getTestData();
		printXY(vs);
		printYX(vs);
	}

	public static void printXY(EvaluationValues vs) {
		StringBuffer out = new StringBuffer();
		// header(first line: 指标名)
		out.append(vs.getFrequence()).append("\t");
		for (MetricInfo m : vs.getMetrics()) {
			out.append(m.getMetric().getName());
			out.append("(").append(m.getDimensions().length).append(")");
			out.append("\t");
		}
		out.append("\n");
		// header(second line：维度名)
		out.append("-").append("\t");
		for (MetricInfo m : vs.getMetrics()) {
			for (String type : m.getDimensionTypes()) {
				out.append(type);
				out.append("(").append(m.getDimensions(type).length).append(")");
				out.append("\t");
			}
		}
		out.append("\n");
		// header(third line：维度值)
		out.append("-").append("\t");
		for (MetricInfo m : vs.getMetrics()) {
			for (String type : m.getDimensionTypes()) {
				for (String value : m.getDimensions(type))
					out.append(value).append("\t");
			}
		}
		out.append("\n");
		// body
		for (FrequenceDate fd : vs.getDateRange()) {
			out.append(fd.toDate()).append("\t");
			for (Object b : vs.getValues(fd.toDate())) {
				out.append(b).append("\t");
			}
			out.append("\n");
		}
		System.out.println(out);
	}
	
	public static void printYX(EvaluationValues vs) {
		StringBuffer out = new StringBuffer();
		// header
		out.append(vs.getFrequence()).append("\t");
		for (FrequenceDate fd : vs.getDateRange()) {
			out.append(fd).append("\t");
		}
		out.append("\n");
		// body
		for (MetricInfo m : vs.getMetrics()) {
			for (String type : m.getDimensionTypes()) {
				for (String value : m.getDimensions(type)) {
					out.append(m.getMetric().getName()).append("-").append(type).append("-").append(value).append("\t");
					for (Object b : vs.getValues(m.getMetric().getName(), type, value)) {
						out.append(b).append("\t");
					}
					out.append("\n");
				}
			}
		}
		System.out.println(out);
	}
	
	protected static EvaluationValues getTestData(DateRange daterange, boolean fullDimension) {
		Metric m1 = getMetric("总人数");
		Metric m2 = getMetric("消费额");
		List<Metric> metrics = new ArrayList<Metric>();
		metrics.add(m1);
		metrics.add(m2);
		List<MetricValue> mvs = new ArrayList<MetricValue>();
		mvs.add(getMetricValue(m1, daterange.getStart(), "100", "*", "*"));
		mvs.add(getMetricValue(m2, daterange.getStart(), "200", "*", "*"));
		mvs.add(getMetricValue(m2, daterange.getStart(), "130", "交易类型", "POS"));
		mvs.add(getMetricValue(m2, daterange.getStart(), "70", "交易类型", "NET"));
		if (fullDimension) {
			mvs.add(getMetricValue(m2, daterange.getStart(), "90", "客户性别", "女"));
			mvs.add(getMetricValue(m2, daterange.getStart(), "110", "客户性别", "男"));
		}
		
		return new EvaluationValuesImpl(new CustomerGroup(), daterange, metrics, mvs);
	}
	
	protected static EvaluationValues getTestData(DateRange daterange) {
		return getTestData(daterange, true);
	}
	
	protected static EvaluationValues getTestData() {
		return getTestData(new DateRange(new FrequenceDate("D20070101"), new FrequenceDate("D20070131")));
	}
	
	private static Metric getMetric(String name) {
		Metric ret = new Metric();
		ret.setName(name);
		return ret;
	}
	
	private static MetricValue getMetricValue(Metric metric, FrequenceDate date, String val, String dimType, String dim) {
		MetricValue ret = new MetricValue();
		ret.setMetric(metric);
		Dimension d = new Dimension();
		d.setType(dimType); d.setValue(dim);
		ret.setDimension(d);
		ret.setValue(new BigDecimal(val));
		ret.setTargetDate(date);
		return ret;
	}
}
