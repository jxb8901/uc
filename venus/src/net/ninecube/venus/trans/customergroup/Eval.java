/**
 * 
 * created on 2007-4-23
 */
package net.ninecube.venus.trans.customergroup;

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
	private CustomerGroup customerGroup;
	private CustomerGroup referGroup;

	protected CustomerGroup __getCustomerGroup() {
		return this.customerGroup;
	}
	protected CustomerGroup __getReferGroup() {
		return this.referGroup;
	}

	public void prepare() throws Exception {
		Long customerGroupId = this.getLongParameter("idno");
		if (customerGroupId == null) customerGroupId = this.getLongParameter("customerGroup.idno");

		if (customerGroupId != null) {
			this.customerGroup = this.cgm.load(customerGroupId);
			
			Long evaluationId = this.getLongParameter("evaluation.idno");
			if (evaluationId != null)
				this.evaluation = this.em.load(evaluationId);
			else
				this.evaluation = this.customerGroup.getEvaluation();
			
			Long referGroupId = this.getLongParameter("referGroup.idno");
			if (referGroupId != null) this.referGroup = this.cgm.load(referGroupId);
		}
	}

	public CustomerGroup getCustomerGroup() {
		return customerGroup;
	}

	public CustomerGroup getReferGroup() {
		return referGroup;
	}
}
