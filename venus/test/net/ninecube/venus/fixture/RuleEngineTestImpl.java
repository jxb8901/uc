/**
 * 
 * created on 2007-6-23
 */
package net.ninecube.venus.fixture;

import java.util.List;
import java.util.Map;

import net.ninecube.lang.FrequenceDate;
import net.ninecube.saturn.Rule;
import net.ninecube.saturn.RuleEngine;
import net.ninecube.saturn.database.DataSet;
import net.ninecube.saturn.exception.RuleSyntaxExceptions;

/**
 * 
 * @author jxb
 */
public class RuleEngineTestImpl extends RuleEngine {

	@Override
	public Rule createRule(String rule, String mapping) {
		return null;
	}

	@Override
	public void execute(List<FrequenceDate> freqdates, String filter, Rule rule) {
	}

	@Override
	public void execute(List<FrequenceDate> freqdates, String filter, Rule rule, Map<String, ?> vars) {
	}

	@Override
	public DataSet getFilter(String filter) {
		return null;
	}

	@Override
	public void validate(Rule rule) throws RuleSyntaxExceptions {
	}

	@Override
	public void validate(Rule rule, Map<String, ?> paras) throws RuleSyntaxExceptions {
	}

	@Override
	public void validateFilter(String filter) {
	}

	@Override
	public DataSet getFilter(String filter, FrequenceDate fdate) {
		return null;
	}

}
