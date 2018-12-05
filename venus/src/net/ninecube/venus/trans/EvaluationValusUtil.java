/**
 * 
 * created on 2007-5-29
 */
package net.ninecube.venus.trans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.ninecube.formula.Formula;
import net.ninecube.lang.DateRange;
import net.ninecube.lang.FrequenceDate;
import net.ninecube.reports.engine.ChartReportEngine;
import net.ninecube.reports.engine.output.ChartEngineOutput;
import net.ninecube.reports.objects.ReportChart;
import net.ninecube.reports.objects.chart.TimeChartValue;
import net.ninecube.venus.query.Dimension;
import net.ninecube.venus.query.EvaluationType;
import net.ninecube.venus.query.EvaluationValue;
import net.ninecube.venus.query.EvaluationValues;
import net.ninecube.venus.query.impl.EvaluationValuesImpl;
import net.ninecube.venus.query.impl.ReferenceEvaluationValues;

import org.jfree.data.xy.XYDataset;

/**
 * 
 * @author jxb
 */
public class EvaluationValusUtil {
	
	public static TimeChartValue[] createChartValues(EvaluationValues evs, String metric, String dimensionType, String dimension) {
		List<TimeChartValue> ret = new ArrayList<TimeChartValue>();
		createChartValues(ret, evs, metric, dimensionType, dimension);
		return ret.toArray(new TimeChartValue[0]);
	}
	
	private static void createChartValues(List<TimeChartValue>ret, EvaluationValues evs, String metric, String dimensionType, String dimension) {
		if (evs instanceof ReferenceEvaluationValues) {
			createChartValues(ret, (ReferenceEvaluationValues)evs, metric, dimensionType, dimension);
		}
		else if (evs instanceof EvaluationValuesImpl) {
			createChartValues(ret, (EvaluationValuesImpl)evs, metric, dimensionType, dimension);
		}
		else {
			throw new IllegalArgumentException("unknown class: " + evs.getClass());
		}
	}
	
	private static void createChartValues(List<TimeChartValue>ret, ReferenceEvaluationValues evs, String metric, String dimensionType, String dimension) {
		if (evs.getMain() instanceof EvaluationValuesImpl) {
			createChartValues(ret, (EvaluationValuesImpl)evs.getMain(), metric, dimensionType, dimension);
		}
		else {
			createChartValuesWithAbs(ret, (ReferenceEvaluationValues) evs.getMain(), metric, dimensionType, dimension);
		}
		if (evs.getRefer() instanceof EvaluationValuesImpl) {
			createChartValues(ret, (EvaluationValuesImpl)evs.getRefer(), metric, dimensionType, dimension);
		}
		else {
			createChartValuesWithAbs(ret, (ReferenceEvaluationValues) evs.getRefer(), metric, dimensionType, dimension);
		}
	}
	
	private static void createChartValuesWithAbs(List<TimeChartValue>ret, ReferenceEvaluationValues evs, String metric, String dimensionType, String dimension) {
		ReferenceEvaluationValues revs = (ReferenceEvaluationValues) evs;
		EvaluationType old = revs.getEvaluateionType();
		revs.setEvaluateionType(EvaluationType.Abs);
		EvaluationValue[] ev = revs.getValues(metric, dimensionType, dimension);
		BigDecimal[] values = new BigDecimal[ev.length];
		for (int i = 0; i < ev.length; i++) values[i] = (BigDecimal)ev[i].toValue();
		String series = evs.getTarget().getName() + "." + getSeries(metric, dimensionType, dimension) + " 增长[" + revs.getRefer().getDateRange().getStart().toDateString() + "]";
		createChartValues(ret, evs.getMain(), values, series);
		revs.setEvaluateionType(old);
	}
	
	private static void createChartValues(List<TimeChartValue>ret, EvaluationValuesImpl evs, String metric, String dimensionType, String dimension) {
		BigDecimal[] values = evs.getValues(metric, dimensionType, dimension);
		String series = evs.getTarget().getName() + "." + getSeries(metric, dimensionType, dimension);
		createChartValues(ret, evs, values, series);
	}
	
	private static void createChartValues(List<TimeChartValue>ret, EvaluationValues evs, BigDecimal[] values, String series) {
		DateRange daterange = evs.getDateRange();
		int index = 0;
		for (FrequenceDate date : daterange) {
			TimeChartValue val = new TimeChartValue();
			val.setTime(date.toDate());
			val.setSeries(series);
			val.setValue(values[index++].doubleValue());
			ret.add(val);
		}
	}
	
	private static String getSeries(String metric, String dimensionType, String dimension) {
		if (Dimension.isDefault(dimensionType, dimension)) {
			return metric;
		}
		return metric + "[" + dimensionType + ":" + dimension + "]";
	}
}
