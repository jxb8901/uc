package net.ninecube.saturn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.ninecube.lang.FrequenceDate;
import net.ninecube.saturn.ast.RuleParser;
import net.ninecube.saturn.ast.SimpleNode;
import net.ninecube.saturn.database.DataSet;
import net.ninecube.saturn.database.DatabaseManager;
import net.ninecube.saturn.exception.ExceptionProcessFactory;
import net.ninecube.saturn.exception.RuleSyntaxException;
import net.ninecube.saturn.exception.RuleSyntaxExceptions;
import net.ninecube.saturn.function.operation.ResultProcessorFactory;
import net.ninecube.saturn.impl.RuleEngineImpl;
import net.ninecube.saturn.sql.SQLExecutor;
import net.ninecube.saturn.sql.SQLExecutorImpl;
import net.ninecube.util.DateUtil;
import net.ninecube.util.StringUtil;

import org.apache.log4j.Logger;

public abstract class RuleEngine {
	public static final String KEY_PROMOTION_PLAN_ID="promotionPlan.id";
	
	public abstract Rule createRule(String rule, String mapping);
		
	public abstract void execute(List<FrequenceDate> freqdates, String filter, Rule rule);
	
	public abstract void execute(List<FrequenceDate> freqdates, String filter, Rule rule, Map<String,?> vars);
	
	public abstract void validate(Rule rule) throws RuleSyntaxExceptions;
	
	public abstract void validate(Rule rule, Map<String, ?> paras) throws RuleSyntaxExceptions;
	
	public abstract DataSet getFilter(String filter);
	
	public abstract DataSet getFilter(String filter , FrequenceDate fdate);
	
	public abstract void validateFilter(String filter) throws RuleSyntaxExceptions;
	
	public static RuleEngine getInstance(){
		return new RuleEngineImpl();
	}
	
	
}
