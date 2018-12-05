/**
 * 
 * created on 2007-3-18
 */
package net.ninecube.formula.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ninecube.formula.Formula;
import net.ninecube.formula.FormulaFactory;
import net.ninecube.formula.FormulaResolver;
import net.ninecube.formula.exception.FormulaNotFoundException;
import net.ninecube.formula.exception.FormulaSyntaxException;
import net.ninecube.lang.Frequence;
import net.ninecube.saturn.RuleEngine;

/**
 * 
 * @author jxb
 * 
 */
public class MapFormulaResolver implements FormulaResolver, FormulaFactory {
	private Map<String, Formula> formulas = new HashMap<String, Formula>();
	private boolean cacheable = true;
	private RuleEngine ruleEngine;
	
	//~ cache 
	
	public Formula resolve(String formulaRef) {
		Formula ret = formulas.get(formulaRef);
		if (ret == null) throw new FormulaNotFoundException(formulaRef);
		return ret;
	}
	
	public boolean hasFormula(String ref) {
		return this.formulas.containsKey(ref);
	}
	
	public void unregister(String formulaId) {
		formulas.remove(formulaId);
	}
	
	public AbstractFormula register(AbstractFormula formula) {
		if (this.cacheable) formulas.put(formula.getName(), formula);
		return formula;
	}
	
	public AbstractFormula register(Long id, String name, String rule) {
		return register(id,name,"",rule);
	}

	
	public AbstractFormula register(Long id, String name, String type, String rule) {
		AbstractFormula formula = createFormula(id, name, type, rule);
		if (this.cacheable) formulas.put(formula.getName(), formula);
		return formula;
	}
	
	public void clear() {
		formulas.clear();
	}
	
	//~ factory method
	public AbstractFormula createFormula(Long id, String name, String formula) {
		return createFormula(id,name,"",formula);
	}
	
	public AbstractFormula createFormula(Long id, String name, String type, String formula) {
		AbstractFormula ret;
		try {
			ret = createRuleFormula(id, name, type, formula);
		} catch (FormulaSyntaxException e) {
			ret = createDefaultFormula(id, name, type, formula);
		}
		return this.register(ret);
	}
	
	private AbstractFormula createDefaultFormula(Long id, String ref, String type, String formula) {
		return new DefaultFormula(this, id, ref, type, formula);
	}
	
	private AbstractFormula createRuleFormula(Long id, String ref,String type, String formula) {
		return new RuleFormula(this, this.ruleEngine, id, ref , type, formula);
	}

	public List<Formula> getFormulaByTargetTypeAndFrequence(
			String targetType, Frequence freq) {
		return new ArrayList<Formula>();
	}
	
	//~ ioc

	public void setRuleEngine(RuleEngine ruleEngine) {
		this.ruleEngine = ruleEngine;
	}

	public void setCacheable(boolean cacheable) {
		this.cacheable = cacheable;
	}
}
