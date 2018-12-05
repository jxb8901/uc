package net.ninecube.venus.domain;

import java.util.Date;
import java.util.List;

import net.ninecube.core.domain.EntityManager;
import net.ninecube.lang.FrequenceDate;

/**
 * 
 * @author jxb
 *
 */
public interface PromotionPlanManager extends EntityManager<PromotionPlan> {
	
	public List<PromotionPlan> getValid(Date date);
	
}
