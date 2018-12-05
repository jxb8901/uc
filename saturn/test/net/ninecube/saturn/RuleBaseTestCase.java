package net.ninecube.saturn;

import net.ninecube.formula.Config;
import net.ninecube.formula.TestTargetManager;
import net.ninecube.formula.impl.DefaultTarget;
import net.ninecube.formula.impl.MapFormulaResolver;
import net.ninecube.formula.ruleadapter.MetricFunctionProviderImpl;
import net.ninecube.saturn.function.FunctionProvider;
import net.ninecube.saturn.function.impl.FunctionProviderImpl;

public class RuleBaseTestCase extends DBBaseTestCase {
	private FunctionProvider orgProvider;
	

	public RuleBaseTestCase(){
		
	}
	
	public RuleBaseTestCase(String name){
		super(name);
	}

	protected void setUp() throws Exception{
		super.setUp();
		MapFormulaResolver fr = new MapFormulaResolver();
		fr.setRuleEngine(RuleEngine.getInstance());
		fr.register(1l,"POS交易笔数",Config.TARGET_TYPE_CUSTOMER,"count(交易.金额)");
		fr.register(2l,"交易笔数",Config.TARGET_TYPE_CUSTOMER,"count(交易.金额) by (交易.交易类型)");
		TestTargetManager targetManager = new TestTargetManager();
		targetManager.addTarget(new DefaultTarget(1l,"个人客户",Config.TARGET_TYPE_CUSTOMER,"",null));
		orgProvider = FunctionProvider.getInstance();
		FunctionProvider.ChainedFunctionProviderImpl cfp = new FunctionProvider.ChainedFunctionProviderImpl();
		cfp.add(new FunctionProviderImpl());
		cfp.add(new MetricFunctionProviderImpl(fr,targetManager));
		FunctionProvider.setInstance(cfp);
		
	}
	
	protected void tearDown()throws Exception{
		super.tearDown();
		FunctionProvider.setInstance(orgProvider);
	}
	
	public void testvoid (){
		
	}


}
