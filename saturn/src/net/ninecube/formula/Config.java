package net.ninecube.formula;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.LogFactory;

public class Config {
	private static final org.apache.commons.logging.Log log = LogFactory.getLog(Config.class);
	public static final String TARGET_TYPE_CUSTOMER = "Customer";
	private static Config config = new Config();
	
	
	public static enum EvaluateType{
		/** 默认计算类型，主要用来处理普通公式和RuleFormular中的非客户指标类公式*/
		DEFAULT, 
		/** 批量计算类型, 主要用来处理RuleFormular中的客户指标类公式,用来批量计算所有客户的指标值*/
		BATCH
	}
	
	
	public String getFomularValueTable(Context context){
		return getFomulaValueTable(context.getTarget(), context.getFormula());
	}
	
	public String getFomulaValueTable(Target target, Formula formula){
		return "MetricValue";
	}
	
	public String getTargetDataId(Context context){
		log.debug("arguments : " + context.getVariables());
		return context.getVariables().getArgument(0);
	}
	
	public Map.Entry<String, String> getDimensionArgument(Context context){
		for(Iterator<Map.Entry<String, String>> it = context.getVariables().entrySet().iterator() ; it.hasNext();){
			Map.Entry<String, String> e = it.next();
//			log.debug("argument : " + e.getKey() + " , is dimension para :  " + Arguments.isUnnamedArgument(e.getKey()));
			if(Arguments.isUnnamedArgument(e.getKey())) continue;
			return e;
		}
		return null;
	}
	
	/**
	 * 取得公式的计算方式
	 */
	public EvaluateType getEvaluateType(Context context){
		if(TARGET_TYPE_CUSTOMER.equals(context.getTarget().getType())){
			return EvaluateType.BATCH;
		}
		return EvaluateType.DEFAULT;
	}

	
	public static Config getInstance(){
		return config;
	}
	
}
