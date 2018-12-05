package net.ninecube.saturn.function.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.ninecube.saturn.function.Function;
import net.ninecube.saturn.function.FunctionDefinition;

public class FunctionDefinitionImpl implements FunctionDefinition {
	private String name;

	private String mapping;

	private Function func;

	private List<String> funcParamNames;

	private List<String> mappingParams;

	public FunctionDefinitionImpl(String name, String mapping, Function func,
			List<String> funcParamNames) {
		this.name = name;
		this.mapping = mapping;
		this.func = func;
		this.funcParamNames = funcParamNames;
	}

	public String getName() {
		return name;
	}

	public String getMapping() {
		return mapping;
	}

	public Function getFunction() {
		return func;
	}

	public List<String> getParameterNames() {
		return funcParamNames;
	}

	public List<String> getMappingParamNames() {
		if (mappingParams != null)
			return mappingParams;
		mappingParams = getParams(mapping);
		return mappingParams;
	}

	public String getFunctionCallRule(String mappingCall) {
		String result = name + "(";
		Map<String , String> args = getMappingCallArgs(mappingCall);
		String errMsg = "Invalid mapping call , the call arguments is incorrect with function parameters! \r\n";
		errMsg += "function parameters : " + funcParamNames + " \r\ncall arguments : " + args;
		if(funcParamNames.size() !=args.size()){
			throw new IllegalArgumentException(errMsg);
		}
		for(int i = 0 ; i < funcParamNames.size() ; i++){
			String arg = args.get(funcParamNames.get(i));
			if(arg == null)
				throw new IllegalArgumentException(errMsg);
			if(i > 0) result += ",";
			result += arg;
		}
		result += ")";
		
		return result;
	}
	
	public Map<String, String> getMappingCallArgs(String mappingCall){
		Map<String, String> args = new HashMap<String, String>();
		List<String> paramArray = getMappingParamNames();
		List<String> argArray = getParams(mappingCall);
		if(paramArray.size() != argArray.size()){
			String errMsg = "Invalid mapping call , the call parameter is incorrect ! \r\n";
			errMsg += "mapping : " + mapping + " \r\nmapping call : " + mappingCall;
			throw new IllegalArgumentException(errMsg);
		}
		for(int i = 0 ; i < paramArray.size() ; i++){
			args.put(paramArray.get(i), argArray.get(i));
		}
		return args;
	}

	private List<String> getParams(String str){
		List<String> result = new ArrayList<String>();
		Pattern pattern = Pattern.compile("\\{([^\\}]*)\\}");
		Matcher matcher = pattern.matcher(str);
		while(matcher.find()){
			result.add(matcher.group(1));
		}
		return result;
	}

}
