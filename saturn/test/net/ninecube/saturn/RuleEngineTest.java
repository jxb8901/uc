package net.ninecube.saturn;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ninecube.formula.ruleadapter.MetricFunctionProviderImpl;
import net.ninecube.lang.Frequence;
import net.ninecube.lang.FrequenceDate;
import net.ninecube.saturn.database.DataSet;
import net.ninecube.saturn.database.DataSetUtil;
import net.ninecube.saturn.database.DataSetUtilTestBase;
import net.ninecube.saturn.database.DatabaseManager;
import net.ninecube.saturn.exception.RuleSyntaxExceptions;
import net.ninecube.saturn.function.FunctionProvider;
import net.ninecube.saturn.impl.RuleEngineImpl;

import org.apache.log4j.Logger;

public class RuleEngineTest extends RuleBaseTestCase {
	private static Logger log = Logger.getLogger(RuleEngineTest.class);
	private RuleEngine re;

	protected void setUp() throws Exception {
		new DataSetUtilTestBase().setUp();
		super.setUp();
		re =RuleEngine.getInstance();
		FunctionProvider.ChainedFunctionProviderImpl cfp = new FunctionProvider.ChainedFunctionProviderImpl();
		cfp.add(FunctionProvider.getInstance());
		cfp.add(new MetricFunctionProviderImpl());
		FunctionProvider.setInstance(cfp);
	}
	
	public void testFilter(){
		this.assertNotNull(re.getFilter(null));
		this.assertNotNull(re.getFilter(""));
		this.assertNotNull(re.getFilter("  "));
		// String filter = "客户.性别 == '女' and 交易.交易类型 == '1'"; 
		String filter = "交易.交易类型 == \"现金\"";
		DataSet ds = re.getFilter(filter);
		this.assertNotNull(ds);
		System.out.println(ds);
		
		ds = re.getFilter(" 客户.客户类型=='企业' ");
		System.out.println(ds);
		this.assertNotNull(ds);
		this.assertNotNull(ds.getRelation());
	}
	
	public void testFuncMapCall(){
		String rule = "<信用卡消费或取现{100}元人民币，即可获得{3}分>";
		executeRule("D20050506","客户.性别 == '女'", rule);
		rule = "addtest(5,2)";
		executeRule("D20050506","客户.性别 == '女'", rule);
		// TODO : resolve this problem.
		rule = "<{客户积分.消费积分}按{交易.金额}算每满{200}元积{5}分>";
		//re.execute("D20050506", rule);
	}
	
	public void testSMSBonus(){
		String rule = ("送短信('中秋关怀！！！')");
		executeRule("D20050506","客户.性别 == '女'", rule);
	}
	
	public void testGetDefaultParameters(){
		Map<String, ?> paras = re.createRule("", "消费<金额1:100>元积<积分1:3>分").getDefaultVarValues();
		log.debug("paras : " + paras);
		this.assertTrue(paras.containsKey("金额1"));
	}
	
	public void testValidate(){
		String rule = "  ifa{ " + 
			"\r\n积分11.POS积分 += 交易.金额/100*3 " + 
			"\r\nfunc({vartest})";
		rule += "\r\n[cycleRule frequency = \"月\"]";
		rule += "\r\nc = d + 5";
		RuleSyntaxExceptions ex = null;
		try{
			re.validate(re.createRule(rule, ""));
		}catch(RuleSyntaxExceptions rex){
			ex = rex;
		}
		this.assertNotNull(ex);
		this.assertEquals(7,ex.getExceptions().size());
		
	}
	
	public void testValidateParameters() {
		String rule = "积分.消费积分 += 交易.金额 / {金额1} * {积分1}";
		String mapping = "交易金额每满<金额1:100>元人民币，即可积<积分1:1>分";
		Map<String, String> params = new HashMap<String, String>();
		params.put("金额1", "200");
		params.put("积分1", "3");
		re.validate(re.createRule(rule, mapping));
		re.validate(re.createRule(rule, mapping), params);
	}
	
	public void testDateType(){
		String rule = " 客户.性别 == '女' and 交易.交易日期.年月 == 2005年12月";
		re.validateFilter(rule);
		rule = " 客户.性别 == '女' and 交易.交易日期.年月 == 2005年";
		try {
			re.validateFilter(rule);
			this.fail();
		} catch (RuleSyntaxExceptions e) {
			this.assertEquals(1, e.getExceptions().size());
		}

	}
	
	public void testMetricFunction(){
		String rule = " 客户.性别 == '女' and 个人客户.POS交易笔数 > 3";
		Context context = new Context(DatabaseManager.get());
		context.setCurrentFrequence(new FrequenceDate("D20060812"));
		DataSet ds = ((RuleEngineImpl)re).getFilter(context, rule);
		this.assertEquals("select target_data_id=MetricValue.F_TargetDataID,formula_value="+
				"MetricValue.F_QuotaValue from MetricValue where ((((((MetricValue.F_Frequence=D "+
				"and MetricValue.F_Date=2006-08-12) and MetricValue.F_TargetType=Customer) and "+
				"MetricValue.F_TargetId=1) and MetricValue.F_DimensionType=*) and "+
				"MetricValue.F_Dimension=*) and MetricValue.F_MetricId=1) AS table1 inner join knc_prsn on "+
				"table1.target_data_id=knc_prsn.custno where (knc_prsn.sextp=1 and "+
				"table1.formula_value>3)", ds.toString());
		log.debug("ds : " + ds);
		
		context = new Context(DatabaseManager.get());
		context.setCurrentFrequence(new FrequenceDate("D20060812"));
		rule = " 客户.性别 == '女' and 个人客户.交易笔数(交易.交易类型:'POS') > 3";
		ds = ((RuleEngineImpl)re).getFilter(context, rule);
		log.debug("ds : " + ds);
		this.assertEquals("select target_data_id=MetricValue.F_TargetDataID,formula_value="+
				"MetricValue.F_QuotaValue from MetricValue where ((((((MetricValue.F_Frequence=D and "+
				"MetricValue.F_Date=2006-08-12) and MetricValue.F_TargetType=Customer) and "+
				"MetricValue.F_TargetId=1) and MetricValue.F_DimensionType=交易.交易类型) and "+
				"MetricValue.F_Dimension=POS) and MetricValue.F_MetricId=2) AS table2 inner join "+
				"knc_prsn on table2.target_data_id=knc_prsn.custno where (knc_prsn.sextp=1 and "+
				"table2.formula_value>3)", ds.toString());
			
	}
	
	protected void executeRule(String frequence, String filter, String rule){
		re.execute(Arrays.asList(new FrequenceDate[]{new FrequenceDate(frequence)}), filter, re.createRule(rule, ""));
	}
	
}
