package net.ninecube.formula;

import java.math.BigDecimal;

import net.ninecube.formula.impl.EmptyResultCollector;

public class FormulaEngineTest extends BaseFormulaTestCase {
	
	
	public void testEvalFormulaRef() throws Exception {
		context.setResultCollector(new EmptyResultCollector());
		fr.register(1L, "a", "1");
		fr.register(2L, "b", "4");
		this.assertEquals(new BigDecimal("5"), fr.createFormula(3L, "x", "a + b").eval(context).getValue());

		fr.register(4L, "c", "a + b");
		this.assertEquals(new BigDecimal("10"), fr.createFormula(5L, "x", "a + b + c").eval(context).getValue());
		this.assertEquals(new BigDecimal("20"), fr.createFormula(6L, "x", "(a + b + c) * 2").eval(context).getValue());
	}
	
	public void testEvalRuleFormula() throws Exception {
		context.setTargetDate("D20070223");
		context.setTarget(femaleCustomerGroup);
		Formula f = fr.register(1L, "POS交易额", " sum(交易.金额) where 交易.交易渠道 == 'POS' ");
		Result r = f.eval(context);
		this.assertNotNull(r);
		this.assertEquals(new BigDecimal("100.00").intValue(), r.getValue().intValue());
	}
	
	public void testEvalRuleFormulaWithDimension() throws Exception {
		context.setTargetDate("D20070323");
		context.setTarget(allCustomerGroup);
		Formula f = fr.register(1L, "交易额", " sum(交易.金额) by(客户.性别, 交易.交易渠道) ");
		Result r = f.eval(context);
		this.assertNotNull(r);
		this.assertEquals(new BigDecimal("600.00").intValue(), r.getValue().intValue()); // 400 + 200
		
		f = fr.register(2L, "女性客户交易额", " 交易额(客户.性别:女) ");
		r = f.eval(context);
		this.assertEquals(new BigDecimal("400.00").intValue(), r.getValue().intValue());

		f = fr.register(3L, "网银交易额", " 交易额(交易.交易渠道:网银交易) ");
		r = f.eval(context);
		this.assertEquals(new BigDecimal("200.00").intValue(), r.getValue().intValue());

		f = fr.register(4L, "女性客户交易额", " 交易额(客户.性别:女) + 30 ");
		r = f.eval(context);
		this.assertEquals(new BigDecimal("430.00").intValue(), r.getValue().intValue());

		f = fr.register(3L, "xx交易额", " 交易额(交易.交易渠道:xxxxxx) ");
		r = f.eval(context);
		this.assertEquals(new BigDecimal("0"), r.getValue());
	}
	
	public void testFormulaFilterWithMetric() {
		context.setTargetDate("D20070323");
		context.setTarget(personalCustomerGroup);
		context.getVariables().setArgument(0, "1000000003");
		Formula f = fr.resolve("交易额");
		Result r = f.eval(context);
		this.assertNotNull(r);
		this.assertEquals(new BigDecimal("200.00").intValue(), r.getValue().intValue()); 
		context.getVariables().setArgument(0, "1000000001");
		r = f.eval(context);
		this.assertEquals(new BigDecimal("400.00").intValue(), r.getValue().intValue()); 
		
		context.setTarget(vipCustomerGroup);
		f = fr.register(2L, "交易笔数", "", " count(交易.金额)  ");
		r = f.eval(context);
		this.assertEquals(new BigDecimal("1").intValue(), r.getValue().intValue()); 
		 
		
	}
}
