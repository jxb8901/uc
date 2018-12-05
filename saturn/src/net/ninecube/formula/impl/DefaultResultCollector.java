/**
 * created on 2006-4-10
 */
package net.ninecube.formula.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.ninecube.db.DBException;
import net.ninecube.db.DBManager;
import net.ninecube.formula.Arguments;
import net.ninecube.formula.Config;
import net.ninecube.formula.Context;
import net.ninecube.formula.Formula;
import net.ninecube.formula.Result;
import net.ninecube.formula.db.FormulaValue;
import net.ninecube.formula.exception.FormulaSyntaxException;
import net.ninecube.saturn.database.DatabaseManager;
import net.ninecube.saturn.database.Table;
import net.ninecube.util.DataTypeUtil;

import org.apache.log4j.Logger;

/**
 * @author JXB
 */
public class DefaultResultCollector extends CacheResultCollector {
	private static final Logger log = Logger.getLogger(DefaultResultCollector.class);
	
	@Override
	public boolean isAccepted(Context context, Result result) {
		if (result == null) return true;
		return context.getBoolean(Context.CONFIG_SAVETODB);
	}
	
	protected void saveResult(Context context, Result result) {
		FormulaValue fv = new FormulaValue(DatabaseManager.get(), context);
		fv.setValue(result.getValue());
		
		int i = DBManager.executeUpdate(fv.insert());
		if (i <= 0) {
			log.debug("保存公式计算结果出错："+context.getFormula().getName());
			throw new DBException("保存结果出错");
		}
	}
	
	protected Result selectResult(Context context) {
		log.debug("context arguments : " + context.getVariables());
		FormulaValue fv = new FormulaValue(DatabaseManager.get(), context);
		setDimensionParams(context, fv);
		if (isBatchType(context) && Config.getInstance().getTargetDataId(context) != null)
			fv.setTargetDataId(Config.getInstance().getTargetDataId(context));
		
		Object[] rs = DBManager.executeSelect(fv.select());
		if (rs == null) {
			rs = DBManager.executeSelect(fv.setDefaultDimension().select());
			if (rs == null){
				if (isBatchType(context) && Config.getInstance().getTargetDataId(context) != null) {
					rs = DBManager.executeSelect(fv.setDefaultTargetDataId().select());
					if(rs == null) return null;
					return new DefaultResult(context, new BigDecimal(0));
				}
				return null;
			}
			return new DefaultResult(context, new BigDecimal(0));
		}
		if (rs[0] == null) return new DefaultResult(context, new BigDecimal(0));
		return new DefaultResult(context, DataTypeUtil.obj2BigDecimal(rs[0]));
	}
	
	protected void setDimensionParams(Context context, FormulaValue fv) {
		if ((context.getFormula() instanceof DimensionFormula)) {
			DimensionFormula rf = (DimensionFormula) context.getFormula();
			Map.Entry<String, String> param = Config.getInstance().getDimensionArgument(context);
			if (rf.hasDimension() && param != null) {
				String type = rf.getDimension(param.getKey());
				if (type == null) {
					if (Arguments.isUnnamedArgument(param.getKey()))
						throw new FormulaSyntaxException("必须指定维度类型");
					else
						throw new FormulaSyntaxException("非法维度类型：" + param.getKey());
				}
				fv.setDimensionType(type);
				fv.setDimensionValue(rf.getDimensionValue(type, param.getValue()));
			}
		}
	}
	
	protected boolean isBatchType(Context context) {
		return Config.getInstance().getEvaluateType(context) == Config.EvaluateType.BATCH;
	}
}
