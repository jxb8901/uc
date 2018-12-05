package net.ninecube.saturn.function.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.ninecube.saturn.function.FunctionDefinition;
import net.ninecube.saturn.function.FunctionProvider;
import net.ninecube.saturn.function.operation.PointOperation;

public class FunctionProviderImpl extends FunctionProvider {
	private Map<String,FunctionDefinition> funcNameMap = new HashMap<String, FunctionDefinition>();
	private Map<String,FunctionDefinition>funcMapMap = new HashMap<String,FunctionDefinition>();
	
	{init();}
	public void init(){
		addFunctionDefinition(new FunctionDefinitionImpl("addtest","信用卡消费或取现{金额1}元人民币，即可获得{积分1}分",new FunctionAdd(),Arrays.asList(new String[]{"金额1","积分1"})));

		addFunctionDefinition(new FunctionDefinitionImpl("按比例积分","{积分类型}按{计算字段}算每满{金额}元积{积分}分",new FunProportionPoint(),Arrays.asList(new String[]{"积分类型","计算字段","金额","积分"})));
		
		addFunctionDefinition(new FunctionDefinitionImpl("+=","{积分类型}翻{倍数}倍", new FuncAddAssign(PointOperation.ADD_ASSIGN_OP),Arrays.asList(new String[]{"积分类型","倍数"})));
		addFunctionDefinition(new FunctionDefinitionImpl("*=","{积分类型}增加{增加额}积分", new FuncAddAssign(PointOperation.MUL_ASSIGN_OP),Arrays.asList(new String[]{"积分类型","增加额"})));
		//addFunctionDefinition(new FunctionDefinitionImpl("addtest","信用卡消费或取现{金额1}元人民币，即可获得{积分1}分",new FunctionAdd(),Arrays.asList(new String[]{"amount","point"})));
		addFunctionDefinition(new FunctionDefinitionImpl("送短信","送短信{内容}", new FunctionSMSBonus(),Arrays.asList(new String[]{"内容"})));
		
	}
	
	public FunctionDefinition getByDesc(String desc) {
		return null;
	}

	public FunctionDefinition getByMapping(String mapping) {
		return funcMapMap.get(getFuncMapKey(mapping));
	}

	public FunctionDefinition getByName(String name) {
		return funcNameMap.get(name);
	}

	public void addFunctionDefinition(FunctionDefinition fundef){
		funcNameMap.put(fundef.getName(), fundef);
		funcMapMap.put(getFuncMapKey(fundef.getMapping()), fundef);
	}
	
	String getFuncMapKey(String map){
		String result = "";
		result = map.replaceAll("\\{[^\\}]*\\}", "\\{\\}");
		
		return result;
	}
}
