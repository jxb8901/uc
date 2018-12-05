/**
 * 
 * created on 2007-6-28
 */
package net.ninecube.formula.db;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.expression.Expressions;
import org.codehaus.jrc.predicate.BoolOperator;
import org.codehaus.jrc.predicate.Predicate;
import org.codehaus.jrc.predicate.Predicates;
import org.codehaus.jrc.relation.Relation;

import net.ninecube.db.Sql;
import net.ninecube.formula.Context;
import net.ninecube.formula.Formula;
import net.ninecube.formula.Target;
import net.ninecube.lang.Frequence;
import net.ninecube.lang.FrequenceDate;
import net.ninecube.saturn.database.Column;
import net.ninecube.saturn.database.DataSet;
import net.ninecube.saturn.database.DatabaseManager;

/**
 * 指标值表，负责指标值的保存与读取
 * @author jxb
 */
public class FormulaValue implements Cloneable {
	private static String TABLENAME = "formulavalues";
	private static String FREQUENCE = "frequence";
	private static String DATE = "date";
	private static String TARGET_TYPE = "target_type";
	private static String TARGET_ID = "target_id";
	private static String TARGET_DATA_ID = "target_data_id";
	private static String FORMULA_ID = "formula_id";
	private static String DIMENSION_TYPE = "dimension_type";
	private static String DIMENSION_VALUE = "dimension";
	private static String VALUE = "formula_value";
	
	private DatabaseManager databaseManager;
	
	private FrequenceDate frequenceDate;
	private String targetType;
	private Long targetId;
	private Object targetDataId; // String or Expression
	private Long formulaId;
	private Object dimensionType; // String or Expression
	private Object dimensionValue; // String or Expression
	private Object value; // BigDecimal or Expression
	private DataSet base;
	
	//~ constructor
	
	public FormulaValue(DatabaseManager manager) {
		this.databaseManager = manager;
	}
	
	public FormulaValue(DatabaseManager manager, Context context) {
		this(manager);
		this.setParameters(context);
		this.setDefaultDimension();
		this.setDefaultTargetDataId();
	}

	public FormulaValue clone() {
		try {
			return (FormulaValue)super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
	
	//~ business method
	
	public DataSet selectAsDataSet() {
		DataSet ret = build(false).project(this.getColumn(VALUE).getSqlCode());
		return ret;
	}
	
	public DataSet insertAsDataSet() {
		DataSet ret = build(true);
		ret = build(ret, true, VALUE, this.value);
		return ret;
	}

	public Sql select() {
		Sql ret = new Sql();
		ret.setSql(this.selectAsDataSet().getSql());
		return ret;
	}
	
	public Sql insert() {
		Sql ret = new Sql();
		ret.setSql(this.insertAsDataSet().getSql());
		return ret;
	}

	public FormulaValue setParameters(Context context) {
		this.setFrequenceDate(context.getTargetDate());
		this.setTargetType(context.getTarget().getType());
		this.setTargetId(context.getTarget().getId());
		this.setFormulaId(context.getFormula().getId());
		return this;
	}
	
	public FormulaValue setDefaultTargetDataId() {
		this.setTargetDataId("*");
		return this;
	}
	
	public FormulaValue setDefaultDimension() {
		this.setDimensionType(Formula.DEFAULT_DIMENSION_VALUE);
		this.setDimensionValue(Formula.DEFAULT_DIMENSION_VALUE);
		return this;
	}
	
	public FormulaValue groupby(Expression expr) {
		if (this.base == null) throw new IllegalArgumentException("dataset is null");
		this.base = this.base.groupby(expr);
		return this;
	}
	
	private Column getColumn(String name) {
		return this.databaseManager.getColumnByAlias(TABLENAME+"." + name);
	}
	
	private DataSet build(boolean insert) {
		DataSet ret = (this.base != null) ? this.base : this.databaseManager.newDataSet();
		ret = build(ret, insert, FREQUENCE, this.frequenceDate.getFrequence().getType());
		ret = build(ret, insert, DATE, this.frequenceDate.getStartDate());
		ret = build(ret, insert, TARGET_TYPE, this.targetType);
		ret = build(ret, insert, TARGET_ID, this.targetId);
		ret = build(ret, insert, TARGET_DATA_ID, this.targetDataId);
		ret = build(ret, insert, FORMULA_ID, this.formulaId);
		ret = build(ret, insert, DIMENSION_TYPE, this.dimensionType);
		ret = build(ret, insert, DIMENSION_VALUE, this.dimensionValue);
		return ret;
	}
	
	private DataSet build(DataSet ds, boolean insert, String field, Object value) {
		if (value == null) return ds;
		if (insert) {
			return ds.insert(getColumn(field)).project(asExpression(value));
		}
		else {
			return ds.where(equalPredicate(field, value));
		}
	}
	
	private Predicate equalPredicate(String field, Object value) {
		if (value == null) return Predicates.always(true);
		return Predicates.comparison(getColumn(field).getSqlCode(), 
				BoolOperator.Eq, 
				asExpression(value), 
				false);
	}
	
	private Expression asExpression(Object value) {
		if (value instanceof Expression) return (Expression) value;
		return Expressions.literal(value);
	}
	
	//~ getter and setter

	public DataSet getBase() {
		return base;
	}

	public FormulaValue setBase(DataSet selectValue) {
		this.base = selectValue;
		return this;
	}

	public FormulaValue setFrequenceDate(FrequenceDate date) {
		this.frequenceDate = date;
		return this;
	}

	public FormulaValue setDimensionType(String dimensionType) {
		this.dimensionType = dimensionType;
		return this;
	}

	public FormulaValue setDimensionValue(String dimensionValue) {
		this.dimensionValue = dimensionValue;
		return this;
	}

	public FormulaValue setFormulaId(Long formulaId) {
		this.formulaId = formulaId;
		return this;
	}

	public FormulaValue setTargetDataId(String targetDataId) {
		this.targetDataId = targetDataId;
		return this;
	}

	public FormulaValue setTargetType(String targetType) {
		this.targetType = targetType;
		return this;
	}

	public FormulaValue setTargetId(Long target) {
		this.targetId = target;
		return this;
	}

	public FormulaValue setValue(BigDecimal value) {
		this.value = value;
		return this;
	}
	
	//~ setter for dataset

	public FormulaValue setValue(Expression value) {
		this.value = value;
		return this;
	}

	public FormulaValue setTargetDataId(Expression targetDataId) {
		this.targetDataId = targetDataId;
		return this;
	}

	public FormulaValue setDimensionType(Expression dimensionType) {
		this.dimensionType = dimensionType;
		return this;
	}

	public FormulaValue setDimensionValue(Expression dimensionValue) {
		this.dimensionValue = dimensionValue;
		return this;
	}
}
