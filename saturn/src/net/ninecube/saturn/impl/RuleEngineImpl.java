package net.ninecube.saturn.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.ninecube.lang.FrequenceDate;
import net.ninecube.saturn.Context;
import net.ninecube.saturn.ContextListener;
import net.ninecube.saturn.Node;
import net.ninecube.saturn.Rule;
import net.ninecube.saturn.RuleEngine;
import net.ninecube.saturn.ast.RuleParser;
import net.ninecube.saturn.ast.SimpleNode;
import net.ninecube.saturn.database.DataSet;
import net.ninecube.saturn.database.DatabaseException;
import net.ninecube.saturn.database.DatabaseManager;
import net.ninecube.saturn.exception.ExceptionProcessFactory;
import net.ninecube.saturn.exception.RuleSyntaxException;
import net.ninecube.saturn.exception.RuleSyntaxExceptions;
import net.ninecube.saturn.function.operation.ResultProcessorFactory;
import net.ninecube.saturn.sql.SQLExecutor;
import net.ninecube.saturn.sql.SQLExecutorImpl;
import net.ninecube.util.DateUtil;
import net.ninecube.util.StringUtil;

import org.apache.log4j.Logger;

public class RuleEngineImpl extends RuleEngine{
	private DatabaseManager databaseManager= DatabaseManager.get();

	private static final Logger log = Logger.getLogger(RuleEngineImpl.class);

	public Rule compile(String rule) {
		return null;
	}
	
	public void execute(List<FrequenceDate> freqdates, String filter, Rule rule){
		execute(freqdates,filter,rule,rule.getDefaultVarValues());
	}
	
	public void execute(List<FrequenceDate> freqdates, String filter, Rule rule, Map<String,?> vars){
		long startMill = System.currentTimeMillis();
		//TODO: check variables with Rule
		
		Context context = createContext(freqdates, vars, getFilter(filter));

		log.debug("execute rule : " + rule);
		
		context.init();
		execute(context,rule, vars, false);
		context.finish();
		
		long endMill = System.currentTimeMillis();
		log.info("start time : " + startMill + " ; end time : " + endMill + " ; total millionsecond : " + (endMill - startMill) );
	}
	
	protected void execute(Context context, Rule rule, Map<String,?> vars, boolean validateonly) throws RuleSyntaxException{
		RuleSyntaxExceptions rsex = new RuleSyntaxExceptions();
		try{
			validateRuleParameter(rule, vars);
		}catch(RuleSyntaxExceptions re){
			rsex = re;
		}
		try{
			executeInContext(context,rule.getRule(),validateonly);
		}catch(RuleSyntaxExceptions re){
			rsex.addException(re.getExceptions());
		}
		if(!rsex.isEmpty()) throw rsex;
		
	}
	
	/**
	 * 在当前上下文中执行指定的规则
	 * @param context
	 * @param rule
	 */

	public Object executeInContext(Context context,String rule){
		return executeInContext(context,rule,false);
	}
	
	protected Object executeInContext(Context context,String rule, boolean validateonly){
		List<Exception> exceptions = new ArrayList<Exception>();
		RuleParser parser = new RuleParser(rule);
		
		log.debug("rule :" + rule);
		Node node = null;
		try {
			node = parser.parse(context);
		} catch (RuleSyntaxExceptions e) {
			exceptions.addAll(e.getExceptions());
		}
		log.debug("==============");
		log.debug("rule node dump :\r\n" + (node == null ? node : ((SimpleNode) node).getDumpStr()));
		if(!exceptions.isEmpty())
			throw new RuleSyntaxExceptions(exceptions);
		if(validateonly) return null;
		return node.execute(context);
	}	
	
	public void validate(Rule rule){
		validate(rule, rule.getDefaultVarValues());
	}
		
	public void validate(Rule rule, Map<String, ?> vars){
		Context context = createContext(FrequenceDate
				.getAllEndFrequenceDate(DateUtil.fromYYYYMMDD("20061231")),
				vars, getFilter(""));	
		execute(context, rule, vars, true);
	}
	
	protected void validateRuleParameter(Rule rule, Map<String, ?> paras) throws RuleSyntaxExceptions{
		List<Exception> exceptions = new ArrayList<Exception> ();
		
		if(!exceptions.isEmpty()) throw new RuleSyntaxExceptions(exceptions);
	}
	
	public DataSet getFilter(String filter){
		return getFilter(filter, null);
	}
	
	public DataSet getFilter(String filter, FrequenceDate fdate){
		Context context = new Context(databaseManager);
		if(fdate != null) context.setCurrentFrequence(fdate);
		return getFilter(context, filter);
	}

	public  DataSet getFilter(Context context , String filter){
	// 若过滤器规则为空字符串，则返回一个空的DataSet
		log.debug("parse filter : " + filter);
		
		try {
			if(!StringUtil.isEmpty(filter) && filter.trim().length() > 0) {
				RuleParser parser = new RuleParser(filter);
				Node node = null;
				node = parser.parseFilter(context);
				node.execute(context);
			}
		} catch (DatabaseException e) {
			throw new RuleSyntaxExceptions().addException(e);
		}
		return context.getDataSet();
	}
	
	public void validateFilter(String filter){
		getFilter(filter);
	}
	
	private Context createContext(List<FrequenceDate> freqdates, Map<String,?> vars, DataSet ds) {
		Context context = new Context(databaseManager);
		context.setSqlExecutor(new SQLExecutorImpl());
		for (ContextListener listener : ResultProcessorFactory.getAll()) {
			context.addListener(listener);
		}
		context.setFrequenceDates(freqdates);
		context.setVariables(vars);
		if (ds != null) context.setDataSet(ds);
		return context;
	}

	public void setDatabaseManager(DatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}
	
	public static RuleEngineImpl getInstance(){
		return new RuleEngineImpl();
	}

	public Rule createRule(String rule, String mapping){
		return new RuleImpl(rule, mapping);
	}

}
