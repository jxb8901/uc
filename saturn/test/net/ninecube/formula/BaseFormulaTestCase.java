/*
 * Created on 2004-9-17 BaseTestCase.java
 *
 */
package net.ninecube.formula;

import net.ninecube.formula.impl.ChainResultCollector;
import net.ninecube.formula.impl.DefaultTarget;
import net.ninecube.formula.impl.FormulaEngineImpl;
import net.ninecube.formula.impl.MapFormulaResolver;
import net.ninecube.formula.ruleadapter.MetricFunctionProviderImpl;
import net.ninecube.saturn.DBBaseTestCase;
import net.ninecube.saturn.RuleEngine;
import net.ninecube.saturn.function.FunctionProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


 
/**
 * 
 * @author JXB
 * @version 1.0
 */
public abstract class BaseFormulaTestCase extends DBBaseTestCase {
	protected Log log = LogFactory.getLog(BaseFormulaTestCase.class);
	protected FormulaEngineImpl fe;
	protected RuleEngine re;
	protected MapFormulaResolver fr;
	protected ResultCollector rc;
	protected Context context;
	protected TestTargetManager targetManager;
	protected Target femaleCustomerGroup;
	protected Target allCustomerGroup;
	protected Target personalCustomerGroup;
	protected Target vipCustomerGroup;
	private FunctionProvider orgProvider;
    
    protected void setUp() throws Exception {
    	super.setUp();
    	
   	
		context  = new Context();
		context.setResultCollector(rc);
		this.fe = new FormulaEngineImpl();
		this.re =RuleEngine.getInstance();
		this.fr = new MapFormulaResolver();
		this.fr.setRuleEngine(this.re);
		this.rc = new ChainResultCollector();
		this.fe.setFormulaResolver(fr);
		this.context  = new Context();
		
		this.targetManager = new TestTargetManager();
		targetManager.addTarget(new DefaultTarget(1l,"个人客户",Config.TARGET_TYPE_CUSTOMER,"",null));
		orgProvider = FunctionProvider.getInstance();
		FunctionProvider.setInstance(new MetricFunctionProviderImpl(fr,targetManager));
 		
		
		String female = " 客户.性别 == '女' ";
		this.femaleCustomerGroup = TestTargetManager.getTarget(1L, "客户群", female, this.re.getFilter(female));
		this.allCustomerGroup = TestTargetManager.getTarget(2L, "客户群", "", this.re.getFilter(""));
		this.personalCustomerGroup = targetManager.getTargetByName("个人客户","");

		Formula f = fr.register(1L, "交易额", Config.TARGET_TYPE_CUSTOMER, " sum(交易.金额)  ");
		String rule = " 个人客户.交易额 > 200 ";
		this.vipCustomerGroup = TestTargetManager.getTarget(1L, "Vip客户", rule, null);
    }
    
    public void tearDown(){
    	FunctionProvider.setInstance(orgProvider);
    }
}
