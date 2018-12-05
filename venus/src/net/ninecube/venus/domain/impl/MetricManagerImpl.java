package net.ninecube.venus.domain.impl;

import java.util.ArrayList;
import java.util.List;

import net.ninecube.core.domain.AbstractEntityManager;
import net.ninecube.formula.Context;
import net.ninecube.formula.Formula;
import net.ninecube.formula.FormulaFactory;
import net.ninecube.formula.exception.FormulaNotFoundException;
import net.ninecube.formula.impl.AbstractFormula;
import net.ninecube.lang.Frequence;
import net.ninecube.util.StringUtil;
import net.ninecube.venus.domain.Metric;
import net.ninecube.venus.domain.MetricManager;

import org.apache.log4j.Logger;

public class MetricManagerImpl extends AbstractEntityManager<Metric> implements MetricManager {
	private static final Logger log = Logger.getLogger(MetricManagerImpl.class);
	private FormulaFactory formulaFactory;
	
	//~ implements FormulaResolver
	
	public Formula resolve(String name) {
		Metric metric = this.getByName(name);
		if (metric == null) throw new FormulaNotFoundException(name);
		return this.getFormula(metric);
	}
	
	public List<Formula> getFormulaByTargetTypeAndFrequence(String type, Frequence frequence) {
		List<Metric> list = this.getByTypeAndFreqence(type, frequence);
		log.debug("get formula by targettype '" + type + "' and frequence '" + frequence + "', size: " + list.size());
		List<Formula> ret = new ArrayList<Formula>();
		for (Metric m : list) {
			ret.add(getFormula(m));
		}
		return ret;
	}
	
	//~ implements MetricManager

	@SuppressWarnings("unchecked")
	public Metric getByName(String name) {
		return this.getByNamedQuery("getMetricByName", "name", name);
	}

	public List<Metric> getByTypeAndFreqence(String type, Frequence frequence) {
		if (StringUtil.isEmpty(type) || Formula.DEFAULT_DIMENSION_VALUE.equals(type))
			return this.findByNamedQuery("getMetricByFrequence", 
					new String[]{"frequence"}, 
					new Object[]{frequence});
		else
			return this.findByNamedQuery("getMetricByTypeAndFrequence", 
					new String[]{"type", "frequence"}, 
					new Object[]{type, frequence});
	}
	
	@Override
	protected Metric onSave(Metric m) {
		Formula f = this.getFormula(m);
		f.validate(new Context());
		return m;
	}
	
	protected Formula getFormula(Metric metric) {
		Formula ret = this.formulaFactory.createFormula(
				metric.getIdno(), metric.getName(), metric.getFormula());
		if (ret instanceof AbstractFormula) {
			AbstractFormula f = (AbstractFormula) ret;
			f.setFrequence(metric.getFrequence());
			f.setTargetType(metric.getType());
		}
		return ret;
	}
	
	//~ ioc
	
	public void setFormulaFactory(FormulaFactory formulaFactory) {
		this.formulaFactory = formulaFactory;
	}
}
