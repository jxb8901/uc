/**
 * 
 * created on 2006-11-30
 */
package net.ninecube.saturn.function.operation;

import java.util.Calendar;
import java.util.List;

import net.ninecube.saturn.Context;
import net.ninecube.saturn.RuleEngine;
import net.ninecube.saturn.database.Column;
import net.ninecube.saturn.database.DataSet;
import net.ninecube.saturn.database.DataSetUtil;
import net.ninecube.saturn.database.DatabaseManager;
import net.ninecube.saturn.database.Table;
import net.ninecube.util.DateUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.expression.Expressions;
import org.codehaus.jrc.expression.Operator;
import org.codehaus.jrc.predicate.BoolOperator;
import org.codehaus.jrc.predicate.Predicate;
import org.codehaus.jrc.predicate.Predicates;
import org.codehaus.jrc.relation.Relation;

/**
 * 支持的操作符有：＝，＝＝，＋＝，＊＝，含义如下： ＝：表示计算积分增加额，本次计算积分在以前基础上累加 ＝＝：表示重置积分，以前积分全部被覆盖
 * ＋＝：表示在之前积分规则的基础上额外累加积分 ＊＝：表示在之前积分规则的基础上将翻倍 由上述操作符组成的规则可分为两类：独立性规则和依赖性规则
 * 独立性规则可单独执行，依赖性规则必须依赖于独立性规则才能执行， ＝和＝＝组成的规则为独立性规则，＋＝和＊＝组成的规则为依赖性规则
 * 在两个规则合并(Operation的merge操作发生)时，独立性规则间会产生覆盖的 效果，系统会报警告，依赖性规则可以合并到独立性规则中。但两个依赖性
 * 规则合并时，系统会报错，因为依赖性规则不能单独执行。
 * 
 * @TODO: 积分类型必须用字段表示吗？
 */
public class PointOperation extends AbstractOperation {
	private Log log = LogFactory.getLog(PointOperation.class);
	public static final String[] finishSqls = new String[] {
		"insert into POINTJOURNAL (CUSTOMERID,TRANSTYPE,TRANSID,POINTTYPE,POINT,TRANSTIME, COMMENTS, PROMOTIONPLANID) " + 
			"select CUSTOMERID,TRANSTYPE,TRANSID,POINTTYPE,POINT,TRANSTIME, COMMENTS, PROMOTIONPLANID from POINTCAL ", 
		DatabaseManager.get().getNamedSql("UPDATE_POINTACCOUNT")
	};
	public static final String[] initSqls = new String[] {
		"delete from POINTCAL"
	};
	public static final String ResultProcessorName = "POINT";
	public static final String OPERATE_TABLE_NAME = "POINTCAL";
	
	public static final String ADD_ASSIGN_OP = "+=";
	public static final String MUL_ASSIGN_OP = "*=";
	public static final String ASSIGN_OP = "=";
	
	private String pointType;
	private String operator;
	private Expression value;

	/**
	 * @param 
	 */
	public PointOperation(String pointType, String op, Expression val) {
		this.pointType = pointType;
		this.operator = op;
		this.value = val;
	}
	
	protected String getProcessorName() {
		return ResultProcessorName;
	}

	protected DataSet populate(Context context) {
		DataSet ds = context.getDataSet();
		if (ADD_ASSIGN_OP.equals(operator)) {
			ds = ds.insert(ds.getColumnByAlias( "POINTCAL.CUSTOMERID"));
			// TODO: 中心表为空应该抛异常
			ds = ds.project(ds.getCentricTable().getPrimaryKey().getSqlCode());
			
			Table baseTable = ds.getBaseTable();
			Column col = (baseTable != null) ? baseTable.getPrimaryKey() : null;
			
			ds = ds.insert(ds.getColumnByAlias( "POINTCAL.TRANSID"));
			ds = ds.project(col != null? col.getSqlCode() : Expressions.literal(""));
			ds = ds.insert(ds.getColumnByAlias( "POINTCAL.TRANSTYPE"));
			ds = ds.project(Expressions.literal(baseTable != null? baseTable.getName() : ""));
			
			ds = ds.insert(ds.getColumnByAlias( "POINTCAL.POINTTYPE"));
			ds = ds.project(Expressions.literal(pointType));
			
			ds = ds.insert(ds.getColumnByAlias( "POINTCAL.POINT"));
			ds = ds.project(value);
			
			ds = ds.insert(ds.getColumnByAlias( "POINTCAL.TRANSTIME"));
			log.debug("dateRange : " + context.getCurrentFrequence());
			ds = ds.project(Expressions.literal(DateUtil.format(DateUtil.addFieldValue(
					context.getCurrentFrequence().getEndDate(), Calendar.DAY_OF_MONTH, -1),"yyyy-MM-dd")));

			ds = ds.insert(ds.getColumnByAlias( "POINTCAL.COMMENTS"));
			ds = ds.project(Expressions.literal("" + context.getCurrentFrequence()));
			
			ds = ds.insert(ds.getColumnByAlias("POINTCAL.PROMOTIONPLANID"));
			ds = ds.project(getPromotionPlanID(context));
		} else {
			Column pointColumn = ds.getColumnByAlias("POINTCAL.POINT");
			DataSet update = context.getDatabaseManager().newDataSet();
			update = update.update(pointColumn, Expressions.binary(pointColumn.getSqlCode(), Operator.Mul, value));
			update = update.where(Predicates.comparison(
					ds.getColumnByAlias("POINTCAL.POINTTYPE").getSqlCode(),
					BoolOperator.Eq, Expressions.literal(pointType), false));

			if (!ds.isEmpty()) {
				Column customerIdColumn = ds.getColumnByAlias("POINTCAL.CUSTOMERID");
				Column relativeColumn = DataSetUtil.getColumnOfAssociaion(customerIdColumn);
				ds = ds.project(relativeColumn.getSqlCode());
				Relation r = ds.getRelation();
				log.debug("update where ds : " + r);
				update = update.where(Predicates.in(customerIdColumn.getSqlCode(), r));
			}
			ds = update;
		}
		return ds;
	}

	@Override
	protected String getOperateTableName() {
		return OPERATE_TABLE_NAME;
	}
}
