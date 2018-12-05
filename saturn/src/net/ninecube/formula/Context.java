/**
 * created on 2006-4-6
 */
package net.ninecube.formula;

import net.ninecube.formula.impl.ChainResultCollector;
import net.ninecube.lang.Frequence;
import net.ninecube.lang.FrequenceDate;
import net.ninecube.lang.TypedMap;


/**
 * @author JXB
 */
public class Context extends TypedMap<String, Object> implements Cloneable{
	public static final String CONFIG_SAVETODB = "CONFIG_SAVETODB";
	public static final String CONFIG_RECALCULATE = "CONFIG_RECALCULATE";
	
	private Formula formula;
	private ResultCollector resultCollector;
	private Target target;
	private FrequenceDate targetDate;
	private Arguments variables = new Arguments();
	
	//~
	
	public Context clone() {
		Context ret = (Context) super.clone();
		ret.formula = this.formula;
		ret.target = this.target;
		ret.targetDate = this.targetDate;
		ret.variables = this.variables.clone();
		return ret;
	}
	
	//~
	
	public Formula getFormula() {
		return this.formula;
	}
	
	public Formula setFormula(Formula formula) {
		Formula ret = this.formula;
		this.formula = formula;
		return ret;
	}

	public Target getTarget() {
		return target;
	}
	
	public void setTarget(Target target) {
		this.target = target;
	}

	public Frequence getTargetFrequence() {
		if (getTargetDate() == null) return null;
		return getTargetDate().getFrequence();
	}

	public FrequenceDate getTargetDate() {
		return this.targetDate;
	}
	
	public void setTargetDate(FrequenceDate date) {
		this.targetDate = date;
	}
	
	public void setTargetDate(String date) {
		this.targetDate = new FrequenceDate(date);
	}

	public Arguments getVariables() {
		return variables;
	}

	public void setVariables(Arguments variables) {
		this.variables = variables;
	}

	public ResultCollector getResultCollector() {
		if (this.resultCollector == null)
			this.resultCollector = new ChainResultCollector();
		return resultCollector;
	}

	public void setResultCollector(ResultCollector resultCollector) {
		this.resultCollector = resultCollector;
	}
}
