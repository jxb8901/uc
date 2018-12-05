/**
 * 
 * created on 2007-6-22
 */
package net.ninecube.venus.fixture;

import java.util.Map;

import net.ninecube.formula.Context;
import net.ninecube.formula.Formula;
import net.ninecube.formula.FormulaFactory;
import net.ninecube.formula.Result;
import net.ninecube.formula.exception.FormulaNotFoundException;
import net.ninecube.formula.exception.FormulaSyntaxException;
import net.ninecube.lang.Frequence;
import net.ninecube.venus.domain.Metric;

/**
 * 
 * @author jxb
 */
public class FormulaFactoryTestImpl implements FormulaFactory {

	public Formula createFormula(Long id, String name, String formula) {
		return new FormulaTestImpl();
	}

	public static class FormulaTestImpl implements Formula {

		public Result eval(Context context) {
			return null;
		}

		public int getEvalLevel() {
			return 0;
		}

		public String getFormula() {
			return null;
		}

		public Frequence getFrequence() {
			return null;
		}

		public Long getId() {
			return null;
		}

		public String getName() {
			return null;
		}

		public Map<String, String> getProperties() {
			return null;
		}

		public String getProperty(String name) {
			return null;
		}

		public String[] getReferences() {
			return null;
		}

		public String getTargetType() {
			return null;
		}

		public void validate(Context context) throws FormulaSyntaxException, FormulaNotFoundException {
		}
		
	}
}
