/**
 * 
 * created on 2007-3-22
 */
package net.ninecube.formula.impl;

import net.ninecube.formula.Context;
import net.ninecube.formula.Result;
import net.ninecube.formula.ResultCollector;

/**
 * 
 * @author jxb
 * 
 */
public interface FilterResultCollector extends ResultCollector {

	/**
	 * 判断该结果收集器是否能处理本次计算
	 * 高度程序会在isEvaluated,getResult,addResult方法前调用本方法，以决定是否调用
	 * 当在isEvaluated,getResult时调用时，result参数为null
	 * @param context 计算上下文
	 * @param result 计算结果，可能为null
	 */
	public boolean isAccepted(Context context, Result result);
}
