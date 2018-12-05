/**
 * created on 2006-4-6
 */
package net.ninecube.formula.impl;

import java.util.Collections;
import java.util.Map;

import net.ninecube.formula.Context;
import net.ninecube.formula.Formula;
import net.ninecube.formula.FormulaResolver;
import net.ninecube.formula.Result;
import net.ninecube.formula.exception.FormulaNotFoundException;
import net.ninecube.formula.exception.FormulaSyntaxException;
import net.ninecube.lang.Frequence;
import net.ninecube.util.StringUtil;

import org.apache.log4j.Logger;

/**
 * @author JXB
 */
public abstract class AbstractFormula implements Formula {
	private static final Logger log = Logger.getLogger(AbstractFormula.class);

	protected FormulaResolver formulaResolver;
	private Long id;
	private String name;
	private String formula;
	private Frequence frequence = Frequence.DAY;
	private String targetType;
	private Map<String, String> properties = Collections.emptyMap();
	
	public AbstractFormula(FormulaResolver formulaEngine,
			Long id, String name, String type, String formula) {
		this.formulaResolver = formulaEngine;
		this.id = id;
		this.name = name;
		this.targetType = type;
		this.formula = formula;
		this.initialize();
	}

	public AbstractFormula(FormulaResolver formulaEngine,
			Long id, String name, String formula) {
		this(formulaEngine,id,name,"",formula);
	}
	
	protected void initialize() {
		if (!StringUtil.isEmpty(getProperty(Frequence.KEY))) {
			this.frequence = Frequence.get(getProperty(Frequence.KEY));
		}
		else {
			//log.warn("公式未指定频度，使用默认频度D：" + this);
		}
		if (!StringUtil.isEmpty(getProperty("计算对象类型"))) {
			this.targetType = getProperty("计算对象类型");
		}
	}

	public Result eval(Context context) {
		Formula old = context.setFormula(this);
		log.debug("检查公式是否计算："+this.getName());
		Result ret = context.getResultCollector().getResult(context);
		if (ret == null) {
			log.debug("开始计算公式："+this.getName()+"["+this.getFormula()+"]");
			ret = execute(context);
		}
		else {
			log.debug("公式已经计算："+this.getName());
		}
		context.setFormula(old);
		return ret;
	}
	
	protected abstract Result execute(Context context);
	
	public void validate(Context context) throws 
				FormulaSyntaxException, FormulaNotFoundException {
		
	}

	public int getEvalLevel() {
		return 0;
	}

	public String[] getReferences() {
		return new String[0];
	}
	
	//~ getter and setter

	public String getFormula() {
		return this.formula;
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}
	
	public Frequence getFrequence() {
		return this.frequence;
	}

	public void setFrequence(Frequence frequence) {
		this.frequence = frequence;
	}
	
	public String getTargetType() {
		return this.targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public Map<String, String> getProperties() {
		return this.properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public String getProperty(String name) {
		return this.properties.get(name);
	}

}
