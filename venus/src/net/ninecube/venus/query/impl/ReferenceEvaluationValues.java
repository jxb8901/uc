/**
 * 
 * created on 2007-4-25
 */
package net.ninecube.venus.query.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import net.ninecube.venus.query.TargetEntity;
import net.ninecube.lang.DateRange;
import net.ninecube.lang.Frequence;
import net.ninecube.venus.domain.Metric;
import net.ninecube.venus.query.EvaluationType;
import net.ninecube.venus.query.EvaluationValues;
import net.ninecube.venus.query.EvaluationValue;
import net.ninecube.venus.query.EvaluationValues.MetricInfo;

/**
 * 
 * @author jxb
 */
public class ReferenceEvaluationValues implements EvaluationValues {
	private EvaluationValues main;
	private EvaluationValues refer;
	private EvaluationType evaluateionType = EvaluationType.List;

	public ReferenceEvaluationValues(EvaluationValues main, EvaluationValues refer) {
		this.main = main;
		this.refer = refer;
	}

	public EvaluationValue[] getValues(String metric) {
		return getReferenceValues(this.main.getValues(metric), this.refer.getValues(metric));
	}

	public EvaluationValue[] getValues(String metric, String dimensionType, String dimensionValue) {
		return getReferenceValues(this.main.getValues(metric, dimensionType, dimensionValue),
				this.refer.getValues(metric, dimensionType, dimensionValue));
	}
	
	private EvaluationValue[] getReferenceValues(Object[] main, Object[] refer) {
		EvaluationValue[] ret = new EvaluationValue[main.length];
		for (int i = 0; i < ret.length; i++)
			ret[i] = this.evaluateionType.reference(main[i], refer[i]);
		return ret;
	}

	public EvaluationValue[] getValues(Date date) {
		return getValues(this.getDateRange().getIndex(date));
	}
	
	public EvaluationValue[] getValues(int dateIndex) {
		Object[] mainValue = this.main.getValues(dateIndex);
		Object[] referValue = this.refer.getValues(dateIndex);
		EvaluationValue[] ret = new EvaluationValue[mainValue.length];
		int index = 0, indexMain = 0, indexRefer = 0;
		for (int i = 0; i < this.main.getMetrics().size(); i++) {
			MetricInfo mainMetricInfo = this.main.getMetrics().get(i);
			MetricInfo referMetricInfo = this.refer.getMetrics().get(i);
			ret[index++] = this.evaluateionType.reference(mainValue[indexMain++], referValue[indexRefer++]);
			for (int j = 0; j < mainMetricInfo.getDimensionTypes().length; j++) {
				String mainType = mainMetricInfo.getDimensionTypes()[j];
				if (j < referMetricInfo.getDimensionTypes().length) {
					String referType = referMetricInfo.getDimensionTypes()[j];
					if (referType.equals(mainType)) {
						for (int k = 0; k < mainMetricInfo.getDimensions(mainType).length; k++) {
							String mainDim = mainMetricInfo.getDimensions(mainType)[k];
							if (k < referMetricInfo.getDimensions(referType).length) {
								String referDim = referMetricInfo.getDimensions(referType)[k];
								if (referDim.equals(mainDim)) {
									ret[index++] = this.evaluateionType.reference(mainValue[indexMain++], referValue[indexRefer++]);
									continue;
								}
							}
							ret[index++] = this.evaluateionType.reference(mainValue[indexMain++], MetricInfoImpl.ZERO);
						}
						continue;
					}
				}
				for (int k = 0; k < mainMetricInfo.getDimensions(mainType).length; k++) {
					ret[index++] = this.evaluateionType.reference(mainValue[indexMain++], MetricInfoImpl.ZERO);
				}
				
			}
		}
		return ret;
	}
	
	private EvaluationValue getReferenceValue(Object[] main, int indexMain, Object[] refer, int indexRefer) {
		return this.evaluateionType.reference(main[indexMain], 
				indexRefer > refer.length ? MetricInfoImpl.ZERO : refer[indexRefer]);
	}

	public DateRange getDateRange() {
		return this.main.getDateRange();
	}

	public Frequence getFrequence() {
		return this.main.getFrequence();
	}

	public List<? extends MetricInfo> getMetrics() {
		return this.main.getMetrics();
	}

	public EvaluationType getEvaluateionType() {
		return evaluateionType;
	}

	public void setEvaluateionType(EvaluationType evaluateionType) {
		this.evaluateionType = evaluateionType;
	}

	public void setFullEvaluateionType(EvaluationType evaluateionType) {
		this.evaluateionType = evaluateionType;
		if (this.main instanceof ReferenceEvaluationValues) {
			((ReferenceEvaluationValues)this.main).setFullEvaluateionType(evaluateionType);
		}
		if (this.refer instanceof ReferenceEvaluationValues) {
			((ReferenceEvaluationValues)this.refer).setFullEvaluateionType(evaluateionType);
		}
	}

	public EvaluationValues getRefer() {
		return refer;
	}

	public EvaluationValues getMain() {
		return main;
	}
	
	public TargetEntity getTarget() {
		return this.main.getTarget();
	}

}
