package net.ninecube.formula.impl;

import java.math.BigDecimal;

import net.ninecube.formula.BaseFormulaTestCase;
import net.ninecube.formula.Formula;
import net.ninecube.formula.Result;
import net.ninecube.formula.ResultCollector;

public class CacheResultCollectorTest extends BaseFormulaTestCase {

	public void testCacheResult() throws Exception {
		Formula f = fr.register(1L, "帐面余额", "1000");
		context.setTarget(targetManager.getTarget(1000L, "1")); // 机构号
		context.setTargetDate("D20060410");
		context.setFormula(f);
		
		ResultCollector rc = new CacheResultCollector();
		this.assertFalse(rc.getResult(context) != null);
		
		Result r = new DefaultResult(context, new BigDecimal(100));
		rc.addResult(context, r);
		
		//this.assertTrue(rc.isEvaluated(context));
		
		Result r1 = rc.getResult(context);
		this.assertNotNull(r1);
		this.assertEquals(r1, r);
	}
}
