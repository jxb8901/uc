/**
 * 
 * created on 2007-4-25
 */
package net.ninecube.venus.query.impl;

import java.math.BigDecimal;

import net.ninecube.lang.DateRange;
import net.ninecube.lang.Frequence;
import net.ninecube.util.DateUtil;
import net.ninecube.venus.query.EvaluationType;
import net.ninecube.venus.query.EvaluationValues;
import junit.framework.TestCase;

/**
 * 
 * @author jxb
 */
public class ReferenceEvaluationValuesTest extends TestCase {

	public void testGetValuesByDate() {
		EvaluationValues v1 = EvaluationValuesTest.getTestData(new DateRange(
				Frequence.DAY, DateUtil.fromYYYYMMDD("20070401"), DateUtil.fromYYYYMMDD("20070410")));
		EvaluationValues v2 = EvaluationValuesTest.getTestData(new DateRange(
				Frequence.DAY, DateUtil.fromYYYYMMDD("20060401"), DateUtil.fromYYYYMMDD("20060410")));
		
		ReferenceEvaluationValues v3 = new ReferenceEvaluationValues(v1, v2);
		EvaluationValuesTest.printXY(v3);
		Object[] values = v3.getValues(DateUtil.fromYYYYMMDD("20070401"));
		this.assertEquals("100 - 100", values[0].toString());
		
		v3.setEvaluateionType(EvaluationType.Abs);
		values = v3.getValues(DateUtil.fromYYYYMMDD("20070401"));
		this.assertEquals("0", values[0].toString());
	}

	public void testGetValuesByDateWithDifferentDimension() {
		EvaluationValues v1 = EvaluationValuesTest.getTestData(new DateRange(
				Frequence.DAY, DateUtil.fromYYYYMMDD("20070401"), DateUtil.fromYYYYMMDD("20070410")), true);
		EvaluationValues v2 = EvaluationValuesTest.getTestData(new DateRange(
				Frequence.DAY, DateUtil.fromYYYYMMDD("20060401"), DateUtil.fromYYYYMMDD("20060410")), false);

		ReferenceEvaluationValues v3 = new ReferenceEvaluationValues(v1, v2);
		EvaluationValuesTest.printXY(v3);
		v3 = new ReferenceEvaluationValues(v2, v1);
		EvaluationValuesTest.printXY(v3);
	}
}
