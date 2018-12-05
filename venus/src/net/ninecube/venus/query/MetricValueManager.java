/**
 * 
 * created on 2007-4-18
 */
package net.ninecube.venus.query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.ninecube.core.domain.AbstractEntityManager;
import net.ninecube.core.domain.Entity;
import net.ninecube.core.domain.EntityQueryManager;
import net.ninecube.formula.Target;
import net.ninecube.lang.DateRange;
import net.ninecube.lang.Frequence;
import net.ninecube.lang.FrequenceDate;
import net.ninecube.venus.domain.CustomerGroup;
import net.ninecube.venus.domain.Evaluation;
import net.ninecube.venus.domain.Metric;

/**
 * 
 * @author jxb
 */
public interface MetricValueManager extends EntityQueryManager<MetricValue> {
	
	public EvaluationValues evaluate(CustomerGroup customerGroup, Evaluation evaluation, DateRange daterange);

	public List<EvaluationValues> evaluate(CustomerGroup customerGroup, Evaluation evaluation);
	public List<EvaluationValues> evaluate(CustomerGroup customerGroup, Evaluation evaluation, 
			Map<Frequence, DateRange> dateranges,  CustomerGroup referTarget, Date referDate);
}
