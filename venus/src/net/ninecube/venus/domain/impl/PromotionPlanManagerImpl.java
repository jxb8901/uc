package net.ninecube.venus.domain.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.ninecube.core.domain.AbstractEntityManager;
import net.ninecube.core.exception.EntityException;
import net.ninecube.lang.FrequenceDate;
import net.ninecube.saturn.RuleEngine;
import net.ninecube.venus.domain.CustomerGroupManager;
import net.ninecube.venus.domain.PromotionPlan;
import net.ninecube.venus.domain.PromotionPlanManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PromotionPlanManagerImpl extends AbstractEntityManager<PromotionPlan> implements PromotionPlanManager {
	private static final Log log = LogFactory.getLog(PromotionPlanManagerImpl.class);
	private RuleEngine ruleEngine;
	private CustomerGroupManager customergroupManager;

	@Override
	public PromotionPlan onSave(PromotionPlan pp) {
		if (pp.getRule() == null) throw new EntityException("rule must not be null.");
		this.ruleEngine.validate(this.ruleEngine.createRule(pp.getRule().getRule(), pp.getRule().getMapping()), pp.getRuleParams());
		return pp;
	}

	/**
	 * TODO: 根据给定日期查找有效（可执行）的营销计划
	 * @param daterang 表示一个时间区间，如20070301-20070331
	 * @return
	 */
	public List<PromotionPlan> getValid(Date date){
		log.debug("date : " + date);
		return super.findByNamedQuery("getValidPromotionPlan", "date", date);
	}

	public void setRuleEngine(RuleEngine ruleEngine) {
		this.ruleEngine = ruleEngine;
	}
	
	public void setCustomergroupManager(CustomerGroupManager customergroupManager) {
		this.customergroupManager = customergroupManager;
	}
}
