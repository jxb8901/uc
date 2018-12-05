package net.ninecube.saturn.function;


import java.util.Arrays;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import net.ninecube.saturn.function.impl.FunctionAdd;
import net.ninecube.saturn.function.impl.FunctionDefinitionImpl;

import org.apache.log4j.Logger;

public class FunctionDefinitionImplTest extends TestCase {
	private static Logger log = Logger.getLogger(FunctionDefinitionImplTest.class);
	private FunctionDefinitionImpl funDef;

	public void setUp() {
		funDef = new FunctionDefinitionImpl("test","信用卡消费或取现{金额1}元人民币，即可获得{积分1}分",new FunctionAdd(),Arrays.asList(new String[] {"积分1","金额1"}));
	}

	public void testGetMappingParamNames() {
		List<String> params = funDef.getMappingParamNames();
		log.debug("map param names : " + params);
		super.assertEquals(Arrays.asList(new String[]{"金额1","积分1"}), params);
	}

	public void testGetMappingCallArgs(){
		String mapcalstr = "信用卡消费或取现{100}元人民币，即可获得{3}分";
		Map<String, String> args = funDef.getMappingCallArgs(mapcalstr);
		log.debug("map call args : " + args);
		this.assertEquals(2, args.size());
		this.assertEquals("100", args.get("金额1"));
		this.assertEquals("3", args.get("积分1"));
	}
	
	public void testGetFunctionCallRule() {
		String mapcalstr = "信用卡消费或取现{100}元人民币，即可获得{3}分";
		String funrule = funDef.getFunctionCallRule(mapcalstr);
		log.debug("function call rule : " + funrule);
		assertEquals("test(3,100)" , funrule);
	}

}
