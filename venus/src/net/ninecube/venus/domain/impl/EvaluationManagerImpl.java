package net.ninecube.venus.domain.impl;

import java.util.ArrayList;
import java.util.List;

import net.ninecube.core.domain.AbstractEntityManager;
import net.ninecube.formula.Formula;
import net.ninecube.formula.FormulaResolver;
import net.ninecube.formula.exception.FormulaNotFoundException;
import net.ninecube.formula.impl.AbstractFormula;
import net.ninecube.formula.impl.MapFormulaResolver;
import net.ninecube.lang.Frequence;
import net.ninecube.util.StringUtil;
import net.ninecube.venus.domain.Evaluation;
import net.ninecube.venus.domain.EvaluationManager;
import net.ninecube.venus.domain.Metric;
import net.ninecube.venus.domain.MetricManager;

public class EvaluationManagerImpl extends AbstractEntityManager<Evaluation> 
			implements EvaluationManager {
	private MetricManager metricManager;
	
//	protected Evaluation onSave(Evaluation e) {
//		if (!StringUtil.isEmpty(e.getFormula())) {
//			e.getMetrics().clear();
//			for (String ref : StringUtil.split(e.getFormula())) {
//				e.getMetrics().add(metricManager.getByName(ref));
//			}
//		}
//		return e;
//	}

	public void setMetricManager(MetricManager metricManager) {
		this.metricManager = metricManager;
	}
}
