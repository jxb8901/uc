package net.ninecube.saturn.function;

import java.util.List;

public interface FunctionDefinition {
	public String getName();

	public String getMapping();

	public Function getFunction();

	public List<String> getParameterNames();
	
	
	/**
	 * 根据函数mapping调用产生相应的函数调用规则串
	 * @param mappingCall
	 * @return
	 */
	public String getFunctionCallRule(String mappingCall);

}
