/**
 * created on 2006-4-6
 */
package net.ninecube.formula.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.ninecube.formula.Context;
import net.ninecube.formula.Formula;
import net.ninecube.formula.FormulaEngine;
import net.ninecube.formula.FormulaResolver;
import net.ninecube.formula.Log;
import net.ninecube.formula.Target;
import net.ninecube.formula.TargetManager;
import net.ninecube.formula.exception.DataNotFoundException;
import net.ninecube.formula.exception.FormulaNotFoundException;
import net.ninecube.lang.DateRange;
import net.ninecube.lang.FrequenceDate;

import org.apache.log4j.Logger;

/**
 * @author JXB
 * 
 */
public class FormulaEngineImpl implements FormulaEngine {
	private static final Logger log = Logger.getLogger(FormulaEngineImpl.class);

	private Context config = new Context();
	private FormulaResolver resolver;
	private TargetManager targetManager;
	
	//~
	
	public boolean setSaveToDB(boolean saveToDB) {
		return setConfig(Context.CONFIG_SAVETODB, saveToDB);
	}
	
	public boolean setRecalculate(boolean recalculate) {
		return setConfig(Context.CONFIG_RECALCULATE, recalculate);
	}
	
	public void validateFormulas() {
		//@ TODO: 公式语法检查
	}

	public void eval(String targetType, DateRange daterange) {
		Log.log.debug("===============开始批量计算指标公式===============");
		Log.log.debug("考核对象类型："+targetType+"，日期范围："+daterange);
		log.debug("formula resolver: " + this.resolver.getClass().getName());
		log.debug("target manager: " + this.targetManager.getClass().getName());
		List<Formula> formulas = this.resolver.getFormulaByTargetTypeAndFrequence(targetType, daterange.getFrequence());
		List<Target> targets = targetManager.getTargetsByType(targetType);
		try {
			eval(formulas, targets, daterange);
			Log.log.debug("===============批量计算结束===============");
		} catch (Exception e) {
			Log.log.debug("===============批量计算出错:"+e.getMessage()+"===============");
			Log.log.error(e.getMessage(), e);
		}
	}
	
	public void eval(List<String> formulaNames, String targetType, DateRange daterange) {
		if (formulaNames == null || formulaNames.isEmpty()) {
			eval(targetType, daterange);
		}
		else {
			List<Formula> formulas = new ArrayList<Formula>();
			for (String name : formulaNames) formulas.add(resolver.resolve(name));
			eval(formulas, targetManager.getTargetsByType(targetType), daterange);
		}
	}
	
	public void eval(Formula formula, FrequenceDate date) {
		if (formula.getTargetType() == null) 
			throw new IllegalArgumentException("公式未定义考核对象类型时，必须显示指定该参数");
		eval(formula.getTargetType(), formula, date);
	}
	
	public void eval(String targetType, Formula formula, FrequenceDate date) {

		List<Formula> formulas = new ArrayList<Formula>();
		formulas.add(formula);
		List<Target> targets = targetManager.getTargetsByType(targetType);
		
		Log.log.debug("考核对象类型 ："+targetType);
		Log.log.debug("公式 ："+formula);
		Log.log.debug("考核对象："+targets);
		Log.log.debug("时间范围："+date);
		
		eval(formulas, targets, new DateRange(date, date));

		Log.log.debug("考核对象类型 ："+targetType);
		Log.log.debug("公式 ："+formula);
		Log.log.debug("考核对象："+targets);
		Log.log.debug("时间范围："+date);
	}
	
	public void eval(List<Formula> formulas, List<Target> targets, DateRange daterange) {
		Log.log.debug("要计算的公式数量："+formulas.size());
		Log.log.debug("要计算的考核对象数量："+targets.size());
		Log.log.debug("要计算的时间范围："+daterange.size());
		
		long total = formulas.size()*targets.size()*daterange.size();
		Log.log.debug("计算量："+total);
		long time = System.currentTimeMillis();
		
		long i = 0;
		// 在指标、考核对象、日期上迭代
		for (FrequenceDate date : daterange) {			
			for (Formula formula : formulas) {
				for (Target target : targets) {
					eval(i++, formula, target, date);
				}
			}
		}
		time = System.currentTimeMillis() - time;
		Log.log.debug("计算耗时：["+(time/1000.0) + "]秒, 平均每指标耗时：["+(time/1000.0/total)+"]秒");
		Log.log.debug("要计算的公式数量："+formulas.size());
		Log.log.debug("要计算的考核对象数量："+targets.size());
		Log.log.debug("要计算的时间范围："+daterange.size());
		Log.log.debug("计算量："+total);
	}

	private void eval(long i, Formula formula, Target target, FrequenceDate targetDate) {
		Log.log.debug("开始计算公式["+i+"]：" + target + ", " + targetDate + ", " + formula.getName());
		Context context = new Context();
		context.setTarget(target);
		context.setTargetDate(targetDate);
		context.putAll(this.config);
		try {
			formula.eval(context);
		} 
		catch (DataNotFoundException e) {
			Log.log.warn("部分数据不存在，公式计算出错["+i+"]：" + target + ", " + targetDate + ", " + formula.getName());
			return;
		}
		catch (FormulaNotFoundException e) {
			Log.log.warn("找不到公式定义，公式计算出错:"+e.getMessage());
			return;
		}
		Log.log.debug("公式计算结束["+i+"]：" + target + ", " + targetDate + ", " + formula.getName());
	}
	
	private boolean setConfig(String name, boolean value) {
		boolean ret = config.getBoolean(name, false);
		config.put(name, value);
		return ret;
	}

	public void setTargetManager(TargetManager targetManager) {
		this.targetManager = targetManager;
	}
	
	public void setFormulaResolver(FormulaResolver resolver) {
		this.resolver = resolver;
	}
}
