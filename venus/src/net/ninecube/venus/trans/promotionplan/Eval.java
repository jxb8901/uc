/**
 * 
 * created on 2007-4-23
 */
package net.ninecube.venus.trans.promotionplan;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork.Preparable;

import net.ninecube.core.exception.ObjectNotFoundException;
import net.ninecube.core.trans.Transaction;
import net.ninecube.core.webwork.util.DateRangeByNameConverter;
import net.ninecube.lang.DateRange;
import net.ninecube.lang.Frequence;
import net.ninecube.lang.FrequenceDate;
import net.ninecube.util.DateUtil;
import net.ninecube.util.StringUtil;
import net.ninecube.venus.domain.CustomerGroup;
import net.ninecube.venus.domain.CustomerGroupManager;
import net.ninecube.venus.domain.Evaluation;
import net.ninecube.venus.domain.EvaluationManager;
import net.ninecube.venus.domain.Metric;
import net.ninecube.venus.domain.PromotionPlan;
import net.ninecube.venus.domain.PromotionPlanManager;
import net.ninecube.venus.query.EvaluationValues;
import net.ninecube.venus.query.MetricValue;
import net.ninecube.venus.query.MetricValueManager;
import net.ninecube.venus.query.impl.EvaluationValuesImpl;
import net.ninecube.venus.trans.AbstractEvalTransaction;

/**
 * 
 * @author jxb
 */
public class Eval extends AbstractEvalTransaction {
	
	private PromotionPlan promotionPlan;
	private PromotionPlan referGroup;

	protected CustomerGroup __getCustomerGroup() {
		return this.promotionPlan != null ? this.promotionPlan.getTarget() : null;
	}
	protected CustomerGroup __getReferGroup() {
		return this.referGroup != null ? this.referGroup.getTarget() : null;
	}
	
	@SuppressWarnings("unchecked")
	protected Map<Frequence, DateRange> getDateRanges() {
		/* 设置默认的日期时间范围 */
		if (this.promotionPlan != null) {
			for (Frequence freq : Frequence.getAll()) {
				if (freq.equals(Frequence.REGION)) continue;
				String name = "daterange" + "_" + freq + "_" + "Start";
				if (!super.getParameters().containsKey(name))
					super.getParameters().put(name, this.promotionPlan.getStartDate());
				name = "daterange" + "_" + freq + "_" + "End";
				if (!super.getParameters().containsKey(name))
						super.getParameters().put(name, this.promotionPlan.getEndDate());
			}
		}
		/* */
		
		return super.getDateRanges();
	}

	public void prepare() throws Exception {
		Long promotionPlanId = this.getLongParameter("idno");
		if (promotionPlanId == null) promotionPlanId = this.getLongParameter("promotionPlan.idno");

		if (promotionPlanId != null) {
			this.promotionPlan = this.ppm.load(promotionPlanId);
			
			Long evaluationId = this.getLongParameter("evaluation.idno");
			if (evaluationId != null)
				this.evaluation = this.em.load(evaluationId);
			else
				this.evaluation = this.promotionPlan.getEvaluation();
			
			Long referGroupId = this.getLongParameter("referGroup.idno");
			if (referGroupId != null) this.referGroup = this.ppm.load(referGroupId);
		}
	}
	
	public PromotionPlan getPromotionPlan() {
		return promotionPlan;
	}

	public PromotionPlan getReferGroup() {
		return referGroup;
	}
}
