package net.ninecube.formula.impl;

import java.math.BigDecimal;

import net.ninecube.formula.BaseFormulaTestCase;
import net.ninecube.formula.Formula;
import net.ninecube.formula.Result;
import net.ninecube.formula.TestTargetManager;
import net.ninecube.formula.exception.FormulaNotFoundException;
import net.ninecube.formula.exception.FormulaValidateException;

public class DefaultFormulaTest extends BaseFormulaTestCase {
	
	public void testResultType() {
		context.setTarget(super.femaleCustomerGroup);
		context.setTargetDate("D20070321");
		Formula f = fr.register(1L, "帐面余额", "100");
		Result r = f.eval(context);
		this.assertTrue(r instanceof DefaultResult);
		
		f = fr.register(1L, "总人数", "count");
		f = fr.register(2L, "test", "总人数 / 30");
		r = f.eval(context);
		this.assertTrue(r instanceof DefaultResult);
		
		f = fr.register(2L, "test", "总人数");
		r = f.eval(context);
		this.assertTrue(r instanceof DefaultResult);
	}

	public void testGetEvalLevel() {
		fr.register(1L, "帐面余额", "100");
		fr.register(2L, "消化包袱", "200");
		fr.register(3L, "经营利润", "(帐面余额-消化包袱)*5%");
		fr.register(4L, "经营利润增量", "经营利润-消化包袱");
		
		this.assertEquals(0, fr.resolve("帐面余额").getEvalLevel());
		this.assertEquals(0, fr.resolve("消化包袱").getEvalLevel());
		this.assertEquals(1, fr.resolve("经营利润").getEvalLevel());
		this.assertEquals(2, fr.resolve("经营利润增量").getEvalLevel());
	}
	
	public void testCheckReference() {
		fr.register(1L, "消化包袱", "100");

		try {
			fr.register(2L, "经营利润", "消化包袱 + 固定资产 + 其它收入");
			this.fail("'固定资产'和'其它收入'未定义");
		} catch (FormulaValidateException e) {
			this.assertFalse(e.getFormulaNotFoundExceptions().isEmpty());
			FormulaNotFoundException e1 = (FormulaNotFoundException)e.getFormulaNotFoundExceptions().get(0);
			this.assertEquals("固定资产", e1.getFormulaName());
		}
	}
	
	public void testGetReferences() {
		fr.register(1L, "消化包袱", "100");
		fr.register(1L, "固定资产", "200");
		fr.register(1L, "经营利润", "消化包袱 + 固定资产");
		
		String[] ref = fr.resolve("经营利润").getReferences();
		this.assertEquals(2, ref.length);
		this.assertEquals("消化包袱", ref[0]);
		this.assertEquals("固定资产", ref[1]);
	}
	
	public void testCircularRef() {
		fr.register(1L, "帐面利润", "100");
		fr.register(1L, "循环引用公式A", "帐面利润 + 1");
		try {
			fr.register(1L, "循环引用公式A", "帐面利润 + 循环引用公式A");
			this.fail("有直接循环引用");
		} catch (FormulaValidateException e) {
			this.assertTrue(!e.getFormulaCycleReferenceExceptions().isEmpty());
		}
		fr.register(1L, "循环引用公式B", "帐面利润 + 循环引用公式A");
		fr.register(1L, "循环引用公式C", "帐面利润 + 循环引用公式B");
		try {
			fr.register(1L, "循环引用公式A", "帐面利润 + 循环引用公式C");
			this.fail("有间接循环引用");
		} catch (FormulaValidateException e) {
			this.assertTrue(!e.getFormulaCycleReferenceExceptions().isEmpty());
		}
	}
}
