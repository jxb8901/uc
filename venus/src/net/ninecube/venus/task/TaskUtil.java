/**
 * 
 * created on 2007-4-27
 */
package net.ninecube.venus.task;

import org.hibernate.SessionFactory;

import net.ninecube.core.context.ServiceLocator;
import net.ninecube.formula.FormulaEngine;
import net.ninecube.formula.FormulaResolver;
import net.ninecube.formula.TargetManager;
import net.ninecube.saturn.RuleEngine;
import net.ninecube.venus.domain.PromotionPlanManager;

/**
 * 
 * @author jxb
 */
public class TaskUtil {

	public static FormulaEngine getFormulaEngine() {
		return (FormulaEngine) ServiceLocator.get().getBean("formulaEngine");
	}

	public static FormulaResolver getFormulaResolver() {
		return (FormulaResolver) ServiceLocator.get().getBean("formulaResolver");
	}

	public static TargetManager getTargetManager() {
		return (TargetManager) ServiceLocator.get().getBean("targetManager");
	}

	public static PromotionPlanManager getPromotionplanManager() {
		return (PromotionPlanManager) ServiceLocator.get().getBean("promotionplanManager");
	}

	public static RuleEngine getRuleEngine() {
		return (RuleEngine) ServiceLocator.get().getBean("ruleEngine");
	}

	public static SessionFactory getSessionFactory() {
		return (SessionFactory) ServiceLocator.get().getBean("sessionFactory");
	}
}
