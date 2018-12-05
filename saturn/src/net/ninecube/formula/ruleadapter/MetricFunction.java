package net.ninecube.formula.ruleadapter;

import java.util.List;
import java.util.Map;

import net.ninecube.formula.Config;
import net.ninecube.formula.Formula;
import net.ninecube.formula.Target;
import net.ninecube.lang.FrequenceDate;
import net.ninecube.saturn.Context;
import net.ninecube.saturn.RuleEngine;
import net.ninecube.saturn.database.AliasDataSet;
import net.ninecube.saturn.database.DataSetUtil;
import net.ninecube.saturn.database.DatabaseManager;
import net.ninecube.saturn.database.impl.WhereDataSet;
import net.ninecube.saturn.function.Function;
import net.ninecube.util.DateUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jrc.ast.BaseAstVisitor;
import org.codehaus.jrc.expression.Expression;

public class MetricFunction implements Function {
	private Log log = LogFactory.getLog(MetricFunction.class);
	private Formula formula;
	private Target target;
	private String name;


	public MetricFunction(Formula formula,Target target){
		this.formula = formula;
		this.target = target;
	}
	
	public Object execute(Context context, List indexArgs, Map namedArgs) {
		FrequenceDate fd = context.getCurrentFrequence();
		String rule="formulavalues.frequence == '" + fd.getFrequence().getType() + "' ";
		rule += " and formulavalues.date == '" + DateUtil.format(fd.getEndDate(true),"yyyy-MM-dd") + "' ";
		rule += " and formulavalues.target_type == '" + target.getType() + "'";
		rule += " and formulavalues.target_id == '" + target.getId() + "'";
		rule += " and formulavalues.dimension_type ==  '" + getDimensionType(namedArgs) + "'";
		rule += " and formulavalues.dimension == '" + getDimensionValue(namedArgs) + "'";
		rule += " and formulavalues.formula_id == " + formula.getId() + " ";
		String formulatb = Config.getInstance().getFomulaValueTable(target, formula);
		rule = rule.replaceAll("formulavalues", formulatb);
		
		RuleEngine re = RuleEngine.getInstance();
		log.debug("** sssql : " + re.getFilter(rule));
		AliasDataSet ads = DataSetUtil.alias(re.getFilter(rule), new String[]
		         {formulatb + ".target_data_id", formulatb + ".formula_value"}, new String[]{"target_data_id", "formula_value"});
		context.setDataSet(context.getDataSet().merge(ads));
		
		return ads.getTable().getTableColumnByAlias("formula_value");
	}
	
	private String getDimentionKey(Map namedArgs){
		log.debug("nameArgs : " + namedArgs);
		if(namedArgs.isEmpty()) return null;
		return (String)namedArgs.keySet().iterator().next();
	}
	
	public String getDimensionType(Map namedArgs){
		String type = getDimentionKey(namedArgs);
		if(type == null) return "*";
		return type;
	}
	
	public String getDimensionValue(Map namedArgs){
		String type = getDimentionKey(namedArgs);
		if(type == null) return "*";
		final Expression e = DatabaseManager.get().getColumnByAlias(type).getSqlValue(namedArgs.get(type));
		// @TODO: 如何取得真正的维度值？ 
		return e.accept(new BaseAstVisitor<String>() {
			@Override
			protected String getResult() {
				return e.toString();
			}
			@Override
			public String visitLiteral(Object val) {
			    return val.toString();
			}
		});
	}

}
