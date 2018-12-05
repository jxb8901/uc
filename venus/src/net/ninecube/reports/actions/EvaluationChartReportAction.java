/**
 * 
 * created on 2007-5-29
 */
package net.ninecube.reports.actions;

import java.util.List;
import java.util.Map;

import net.ninecube.lang.Frequence;
import net.ninecube.reports.ORStatics;
import net.ninecube.reports.engine.ChartReportEngine;
import net.ninecube.reports.engine.input.ReportEngineInput;
import net.ninecube.reports.engine.output.ChartEngineOutput;
import net.ninecube.reports.objects.Report;
import net.ninecube.reports.objects.ReportChart;
import net.ninecube.reports.objects.ReportUser;
import net.ninecube.venus.query.EvaluationValues;
import net.ninecube.venus.trans.AbstractEvalTransaction;
import net.ninecube.venus.trans.EvaluationValusUtil;

import com.opensymphony.xwork.ActionContext;

/**
 * 
 * @author jxb
 */
public class EvaluationChartReportAction extends ChartReportAction {
	private static final Report REPORT = createReport();
	private Frequence frequence;
	private String metric;
	private String dimensionType;
	private String dimension;
	
	@Override
	@SuppressWarnings("unchecked")
	public String execute()
	{
		ActionContext.getContext().getSession().put(ORStatics.REPORT, REPORT);
		return super.execute();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected ChartEngineOutput getOutput() throws Exception {
		List<EvaluationValues> list = (List<EvaluationValues>) ActionContext.getContext().getSession().get(
				AbstractEvalTransaction.EVALUATION_VALUES);
		for (EvaluationValues evs : list) {
			if (evs.getFrequence().equals(this.frequence))
				return createChart(evs, this.metric, this.dimensionType, this.dimension);
		}
		throw new IllegalArgumentException("invalid frequence: " + this.frequence);
	}

	public static ChartEngineOutput createChart(EvaluationValues evs, String metric, String dimensionType, String dimension) {
		return ChartReportEngine.createChartOutput(REPORT.getReportChart(), 
				EvaluationValusUtil.createChartValues(evs, metric, dimensionType, dimension));
	}
	
	private static Report createReport() {
		Report ret = new Report();
		ret.setName("Evaluation Chart");
		ReportChart reportChart = new ReportChart();
		reportChart.setName("Evaluation Chart");
		reportChart.setChartType(ReportChart.TIME_CHART);
		reportChart.setShowLegend(true);
		reportChart.setShowTitle(true);
		reportChart.setShowValues(true);
		reportChart.setXaxisLabel("日期");
		reportChart.setYaxisLabel("指标值");
		ret.setReportChart(reportChart);
		return ret;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	public void setDimensionType(String dimensionType) {
		this.dimensionType = dimensionType;
	}

	public void setMetric(String metric) {
		this.metric = metric;
	}

	public void setFrequence(Frequence frequence) {
		this.frequence = frequence;
	}
}
