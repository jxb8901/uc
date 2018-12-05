/**
 * created on 2006-4-10
 */
package net.ninecube.formula.impl;

import java.util.HashMap;
import java.util.Map;

import net.ninecube.formula.Context;
import net.ninecube.formula.Result;

import org.apache.log4j.Logger;

/**
 * @author JXB
 */
public class CacheResultCollector implements FilterResultCollector {
	private static final Logger log = Logger.getLogger(CacheResultCollector.class);
	
	/** 静态变量，所有子类共享相同的缓存 */
	private static final Map<String, Result> resultset = new HashMap<String, Result>();

	public boolean isAccepted(Context context, Result result) {
		return true;
	}

	public Result getResult(final Context context) {
		Result ret = resultset.get(getResultKey(context));
		if (ret == null) {
			ret = selectResult(context);
			if (ret != null)  cacheResult(context, ret);
		}
		return ret;
	}

	public void addResult(Context context, Result result) {		
		cacheResult(context, result);
		saveResult(context, result);
	}
	
	protected void cacheResult(Context context, Result result) {
		resultset.put(getResultKey(context), result);
	}
	
	protected void saveResult(Context context, Result result) {
		
	}
	
	protected Result selectResult(Context context) {
		return null;
	}
	
	protected String getResultKey(Context context) {
		StringBuffer ret = new StringBuffer();
		ret.append(context.getTargetDate().getFrequence().getType()).append(",");
		ret.append(context.getTargetDate().getEndDateString()).append(",");
		ret.append(context.getTarget().getType()).append(",");
		ret.append(context.getTarget().getId()).append(",");
		ret.append(context.getFormula().getId()).append(",");
		ret.append(context.getFormula().getName()).append(",");
		for(Map.Entry<String, String> entry : context.getVariables().entrySet()){
			ret.append(entry.getKey()).append(",");
			ret.append(entry.getValue()).append(",");
		}
		return ret.toString();
	}
	
	// only for test
	protected static void clearCache() {
		resultset.clear();
	}
}
