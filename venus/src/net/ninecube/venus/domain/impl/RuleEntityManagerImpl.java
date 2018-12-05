package net.ninecube.venus.domain.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.ninecube.core.domain.AbstractEntityManager;
import net.ninecube.saturn.RuleEngine;
import net.ninecube.venus.domain.PromotionPlan;
import net.ninecube.venus.domain.RuleEntity;
import net.ninecube.venus.domain.RuleEntityManager;

public class RuleEntityManagerImpl extends AbstractEntityManager<RuleEntity> implements RuleEntityManager {
	private static final Log log = LogFactory.getLog(RuleEntityManagerImpl.class);
	private RuleEngine ruleEngine;

	@Override
	public RuleEntity onSave(RuleEntity r) {
		this.ruleEngine.validate(this.ruleEngine.createRule(r.getRule(), r.getMapping()));
		return r;
	}

	public void setRuleEngine(RuleEngine ruleEngine) {
		this.ruleEngine = ruleEngine;
	}
}
