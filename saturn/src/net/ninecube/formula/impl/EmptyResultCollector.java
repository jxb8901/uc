/**
 * created on 2006-4-10
 */
package net.ninecube.formula.impl;

import net.ninecube.formula.Context;
import net.ninecube.formula.Result;
import net.ninecube.formula.ResultCollector;

/**
 * @author JXB
 */
public class EmptyResultCollector implements ResultCollector {
	
	public static final ResultCollector instance = new EmptyResultCollector();

	public boolean isEvaluated(Context context) {
		return false;
	}

	public Result getResult(Context context) {
		return null;
	}

	public void addResult(Context context, Result result) {
	}

}
