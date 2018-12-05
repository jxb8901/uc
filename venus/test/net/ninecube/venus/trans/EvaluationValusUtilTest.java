package net.ninecube.venus.trans;


import java.util.HashMap;

import net.ninecube.lang.DateRange;
import net.ninecube.lang.Frequence;
import net.ninecube.reports.actions.EvaluationChartReportAction;
import net.ninecube.reports.objects.chart.TimeChartValue;
import net.ninecube.venus.query.EvaluationValues;
import junit.framework.TestCase;

/**
 * 
 * @author jxb
 */
public class EvaluationValusUtilTest extends TestCase {

	public void testCreateDataSet() {
		EvaluationValues evs = AbstractEvalTransaction.getTestData(
				Frequence.DAY, new HashMap<Frequence, DateRange>());
		TimeChartValue[] values = EvaluationValusUtil.createChartValues(evs, "消费额", null, null);
		print(values);
		
		EvaluationChartReportAction.createChart(evs, "消费额", null, null);
	}

	private static void print(TimeChartValue[] values) {
		for (TimeChartValue value : values) {
			System.out.println(value.getSeries()+" - " + value.getTime() + " - " + value.getValue());
		}
	}
}
