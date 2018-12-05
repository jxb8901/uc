/**
 * 
 * created on 2007-4-27
 */
package net.ninecube.venus.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;

import net.ninecube.core.util.OpenSessionInViewFilterSimulator;
import net.ninecube.lang.Frequence;
import net.ninecube.lang.FrequenceDate;
import net.ninecube.saturn.Rule;
import net.ninecube.saturn.RuleEngine;
import net.ninecube.util.DateUtil;
import net.ninecube.venus.domain.PromotionPlan;

/**
 * 
 * @author jxb
 */
public class CalculatePromotionPlan implements Runnable {
	private static final Logger log = Logger.getLogger(CalculatePromotionPlan.class);
	// TODO: 修改为Date
	private Date targetDate;

	public void run() {
		OpenSessionInViewFilterSimulator openSessionInView = new OpenSessionInViewFilterSimulator(TaskUtil.getSessionFactory());
		try {
			openSessionInView.before();

			log.debug("准备执行规则");
			List<PromotionPlan> pps = TaskUtil.getPromotionplanManager().getValid(targetDate);
			for (PromotionPlan p : pps) {
				log.debug("执行规则:"+p.getName() + " ; with freq dates : " + getFrequenceDates(p,targetDate));
				Map<String, String> paras = new HashMap<String, String>();
				if(p.getRuleParams() != null)
					paras.putAll(p.getRuleParams());
				paras.put(RuleEngine.KEY_PROMOTION_PLAN_ID, p.getIdno().toString());
				Rule rule = TaskUtil.getRuleEngine().createRule(p.getRule().getRule(), p.getRule().getMapping());
				TaskUtil.getRuleEngine().execute(
						getFrequenceDates(p, targetDate), 
						p.getTarget().getRule(), 
						rule, paras);
			}
		}
		finally {
			openSessionInView.after();
		}
	}
	
	private List<FrequenceDate> getFrequenceDates(PromotionPlan p, Date date) {
		// TODO: 判断是否是“月末”“年末”“营销结束”，如果是修改为相应频度并执行
		//TODO: "每周","每月",是相对于指定的日期是否是周末，月末来算，还是相对于营销计划的开始日期？？
		List<FrequenceDate> freqDates = FrequenceDate.getAllEndFrequenceDate(date);
		log.debug("compare promotion end date ; promotion end date : " + p.getEndDate() + " , compute date : " + date + " , compare result : " + date.equals(p.getEndDate()) + " ; class : promotion date " + p.getEndDate().getClass() + " , cal date : " + date.getClass());
		if(date.equals(p.getEndDate()) )
			freqDates.add(new FrequenceDate(p.getStartDate(), DateUtil.addFieldValue(date, Calendar.DAY_OF_MONTH, 1), Frequence.REGION));

		return freqDates;
	}
	
	//~ getter and setter

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}
}
