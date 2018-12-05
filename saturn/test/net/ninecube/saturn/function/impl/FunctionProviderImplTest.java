package net.ninecube.saturn.function.impl;

import net.ninecube.saturn.function.impl.FunctionProviderImpl;
import junit.framework.TestCase;

public class FunctionProviderImplTest extends TestCase {
	FunctionProviderImpl funprod;
	
	public void setUp(){
		funprod = new FunctionProviderImpl();
	}
	
	public void testGetFuncMapKey(){
		String rule = "信用卡消费或取现{金额100}元人民币，即可获得{3}分";
		String key = funprod.getFuncMapKey(rule);
		assertEquals("信用卡消费或取现{}元人民币，即可获得{}分", key);
		
	}
}
