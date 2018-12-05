/**
 * created on 2006-4-6
 */
package net.ninecube.formula.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.ninecube.db.DBException;
import net.ninecube.db.DBManager;
import net.ninecube.formula.Arguments;
import net.ninecube.formula.Config;
import net.ninecube.formula.Context;
import net.ninecube.formula.FormulaResolver;
import net.ninecube.formula.Result;
import net.ninecube.formula.db.FormulaValue;
import net.ninecube.formula.exception.FormulaSyntaxException;
import net.ninecube.saturn.RuleEngine;
import net.ninecube.saturn.database.Column;
import net.ninecube.saturn.database.DataSet;
import net.ninecube.saturn.database.DatabaseManager;
import net.ninecube.saturn.database.impl.SelectDataSet;
import net.ninecube.util.StringUtil;

import org.apache.log4j.Logger;
import org.codehaus.jrc.ast.BaseAstVisitor;
import org.codehaus.jrc.expression.Aggregate;
import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.expression.Expressions;

/**
 * 原子公式，公式的值直接从数据库表中使用聚焦函数汇总而出。
 * 原子公式可以表示成下面的SQL语句形式：
 * select sum(table.fieldName) from table where ...
 * 其中的sum即getType()的返回值
 * table.fieldName 即getTableFieldName()
 * where子句通过getFormula()方法得到
 * 
 * 支持指标维度概念
 * 
 * @author JXB
 */
public class RuleFormula extends AbstractFormula implements DimensionFormula {
	private static final Logger log = Logger.getLogger(RuleFormula.class);
	public enum FunctionType {
		sum, count, max, min;
		
		public Expression getProject(DataSet ds, String name) {
			Expression c = StringUtil.isEmpty(name) ? Expressions.wildcard(null) : ds.getColumnByAlias(name).getSqlCode();
			switch (this) {
			case count: return Expressions.aggregate(Aggregate.Count, c);
			case sum: return Expressions.aggregate(Aggregate.Sum, c);
			case max: return Expressions.aggregate(Aggregate.Max, c);
			case min: return Expressions.aggregate(Aggregate.Min, c);
			}
			return null;
		}
	}
	
	protected static final Pattern fieldSplitPattern = Pattern.compile("[, ]+");
	protected static final Pattern pattern = Pattern.compile(
			"\\s*(count|sum|max|min)\\s*(?:\\(([^\\)]+)\\))?"+ // sum(table.name), count
			"\\s*(?:(?:by)?\\s*\\(([^\\)]+)\\))?" + // by(table.name1, table.name2, ... )
			"\\s*(?:where)?\\s*(.*)"); // where ...

	private RuleEngine ruleEngine;
	private FunctionType type;
	private String[] dimensions;
	private Expression project;
	private Expression[] groupby;
	private DataSet where;
	
	public RuleFormula(FormulaResolver resolver,  RuleEngine ruleEngine, 
			Long id, String name, String type, String formula) throws FormulaSyntaxException {
		super(resolver, id, name, type, formula);
		this.ruleEngine = ruleEngine;
		parse(ruleEngine, formula);
	}
	

	public RuleFormula(FormulaResolver resolver,  RuleEngine ruleEngine, 
			Long id, String name, String formula) throws FormulaSyntaxException {
		this(resolver, ruleEngine, id, name,"", formula);
	}
	
	private void parse(RuleEngine ruleEngine, String formula) {
		Matcher m = pattern.matcher(formula);
		if (!m.matches()) throw new FormulaSyntaxException(formula);
		this.type = FunctionType.valueOf(m.group(1));
		this.where = ruleEngine.getFilter(m.group(4));
		// 非count操作必须指定字段名
		if (this.type != FunctionType.count && m.group(2) == null)
			throw new FormulaSyntaxException("must be " + this.type + "(table.name)");
		this.project = this.type.getProject(this.where, m.group(2));
		if (!StringUtil.isEmpty(m.group(3))) { // group by
			initDimension(this.where, m.group(3));
		}
	}
	
	private void initDimension(DataSet ds, String by) {
		this.dimensions = fieldSplitPattern.split(by);
		this.groupby = new Expression[dimensions.length];
		for (int i = 0; i < dimensions.length; i++) {
			groupby[i] = ds.getColumnByAlias(dimensions[i]).getSqlCode();
		}
	}
	
	private FormulaValue[] getGroupByRelations(Context context, FormulaValue selectAll) {
		if (hasDimension()) {
			FormulaValue[] selectBy = new FormulaValue[this.groupby.length];
			for (int i = 0; i < this.groupby.length; i++) {
				selectBy[i] = selectAll.clone();
				selectBy[i].setDimensionValue(this.groupby[i]);
				selectBy[i].setDimensionType(Expressions.literal(this.dimensions[i]));
				selectBy[i].groupby(this.groupby[i]);
			}
			return selectBy;
		}
		return null;
	}
	
	protected DataSet getDataSet(Context context) {
		DataSet base = this.ruleEngine.getFilter(context.getTarget().getRule(),context.getTargetDate()).merge(where);
		base.setDateRange(new Date[] { context.getTargetDate().getStartDate(), 
				context.getTargetDate().getEndDate() });
		log.debug("the base dataset is '" + base + "'");
		return base;
	}
	
	@Override
	protected Result execute(Context context) {
		FormulaValueResult dsr = getDataSetResult(context);
		this.saveResult(context, dsr);
		return context.getResultCollector().getResult(context);
	}
	
	protected FormulaValueResult getDataSetResult(Context context) {
		FormulaValueResult ret = new FormulaValueResult();
		ret.selectAll = new FormulaValue(DatabaseManager.get(), context);
		ret.selectAll.setBase(getDataSet(context));
		ret.selectAll.setValue(this.project);
		if(Config.getInstance().getEvaluateType(context) == Config.EvaluateType.BATCH){
			Expression primaryKey = ret.selectAll.getBase().getCentricTable().getPrimaryKey().getSqlCode();
			ret.selectAll.setTargetDataId(primaryKey).groupby(primaryKey);
			
			ret.computedMark = new FormulaValue(DatabaseManager.get(), context);
			ret.computedMark.setValue(new BigDecimal("0"));
		}
		
		log.debug("execute rule formula, select all dataset is '" + ret.selectAll + "'");
		ret.groupBy = getGroupByRelations(context, ret.selectAll);
		return ret;
	}
	
	protected static class FormulaValueResult {
		public FormulaValue selectAll;
		public FormulaValue[] groupBy;
		/** 用来生成一条标记记录，标记该公式在当前Context下已经计算过。 */
		public FormulaValue computedMark;
	}
	
	//~ getter and setter

	public FunctionType getType() {
		return type;
	}
	
	public boolean hasDimension() {
		return this.groupby != null;
	}
	
	public String[] getDimensions() {
		return this.dimensions;
	}
	
	/**
	 * 将name变换为与公式中设置的维度名称一致的值
	 * 如果不存在则返回null
	 */
	public String getDimension(String name) {
		if (this.dimensions.length == 1) return this.dimensions[0];
		for (String s : this.dimensions)
			if (name.equals(s)) return s;
		return null;
	}
	
	/**
	 * @TODO: 变量维度值为与维度类型匹配的值，对于enumer类型的维度值，
	 * 则需要将字面值转换为数据库值
	 */
	public String getDimensionValue(String name, String value) {
		Column c = this.where.getColumnByAlias(name);
		final Expression e = c.getSqlValue(value);
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

	protected static void saveResult(Context context, FormulaValueResult dsr) {
		// 当dsr.computedMark为空时，SQL语句的形式为 insert into TABLE ... (select agg(col), .... from ...) 
		// 其中的select子句中使用了聚集函数，所以insert语句至少会产生一条记录
		// 当dsr.computedMark非空时，SQL语句的形式为 insert into TABLE ... (select agg(col), .... from ... group by ... )
		// 因为有groupby子句，所以insert语句可能不会产生任何记录，这时不报错
		if (DBManager.executeUpdate(dsr.selectAll.insert()) <= 0 && dsr.computedMark == null) {
			log.debug("保存公式计算结果出错："+context.getFormula().getName());
			throw new DBException("保存结果出错");
		}
		if (dsr.groupBy != null) {
			for (FormulaValue r : dsr.groupBy) 
				DBManager.executeUpdate(r.insert());
		}
		if(dsr.computedMark != null){
			DBManager.executeUpdate(dsr.computedMark.insert());
		}
	}
}
