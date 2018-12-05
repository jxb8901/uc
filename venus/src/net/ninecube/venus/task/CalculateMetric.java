/**
 * 
 * created on 2007-4-27
 */
package net.ninecube.venus.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.ninecube.db.DBManager;
import net.ninecube.formula.Formula;
import net.ninecube.formula.FormulaEngine;
import net.ninecube.formula.FormulaResolver;
import net.ninecube.formula.TargetManager;
import net.ninecube.lang.DateRange;
import net.ninecube.lang.Frequence;
import net.ninecube.lang.FrequenceDate;
import net.ninecube.util.DateUtil;
import net.ninecube.venus.query.TargetEntity;

import org.apache.log4j.Logger;

/**
 * 指标计算任务
 * @author jxb
 */
public class CalculateMetric implements Runnable {
	private static final Logger log = Logger.getLogger(CalculateMetric.class);
	private String targetType = TargetEntity.TARGETTYPE_ALL;
	private List<String> formulaNames;
	private DateRange targetDate;
	private Frequence frequence;
	private Date startTargetDate;
	private Date endTargetDate;
	private boolean saveToDB = false;
	private boolean recalculate = false;

	public void run() {
		// 计算标志  0:不在计算,1:正在计算
		// DBManager.executeUpdate("update adm.S_Parameter set F_Value='1' where F_Code='Calculate'",null);
		
		try {
			if (saveToDB) {
				log.debug("使用数据库事务");
				DBManager.beginTransction();
			}

			TaskUtil.getFormulaEngine().setRecalculate(true);
			TaskUtil.getFormulaEngine().setSaveToDB(isSaveToDB());
			TaskUtil.getFormulaEngine().eval(getFormulaNames(), getTargetType(), getTargetDate());
			
			if (isSaveToDB()) DBManager.commitTransction();
		}
		catch (Exception e) {
			log.debug("计算出错:", e);
			if (isSaveToDB()) {
				DBManager.rollbackTransaction();
			}
		}
		finally {
			if (isSaveToDB()) DBManager.endTransaction();
			//计算标志  0:不在计算,1:正在计算
			// DBManager.executeUpdate("update adm.S_Parameter set F_Value='0' where F_Code='Calculate'",null);
		}
	}
	
	//~ getter and setter

	public Boolean isSaveToDB() {
		return saveToDB;
	}

	public void setSaveToDB(Boolean saveToDB) {
		this.saveToDB = saveToDB;
	}

	public DateRange getTargetDate() {
		if (this.targetDate == null)
			return getDefaultTargetDate();
		return targetDate;
	}

	public void setTargetDate(DateRange range) {
		this.targetDate = range;
	}
	
	public void setFrequence(String frequence) {
		this.frequence = Frequence.get(frequence);
	}
	
	public void setStartTargetDate(Date date) {
		this.startTargetDate = date;
	}
	
	public void setEndTargetDate(Date date) {
		this.endTargetDate = date;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public void setFormulaNames(List<String> formulaNames) {
		this.formulaNames = formulaNames;
	}

	public List<String> getFormulaNames() {
		return formulaNames;
	}

	public Boolean isRecalculate() {
		return recalculate;
	}

	public void setRecalculate(Boolean recalculate) {
		this.recalculate = recalculate;
	}

	private DateRange getDefaultTargetDate() {
		Date current = new Date();
		if (this.frequence == null) this.frequence = Frequence.DAY;
		if (this.startTargetDate == null) this.startTargetDate = current;
		if (this.endTargetDate == null) this.endTargetDate = current;
		return new DateRange(this.frequence, this.startTargetDate, this.endTargetDate);
	}
}
