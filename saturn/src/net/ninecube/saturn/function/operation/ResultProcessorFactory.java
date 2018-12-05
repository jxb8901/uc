/**
 * 
 * created on 2007-4-9
 */
package net.ninecube.saturn.function.operation;

import java.util.ArrayList;
import java.util.List;

import net.ninecube.saturn.ContextListener;

/**
 * 
 * @author jxb
 * 
 */
public class ResultProcessorFactory {

	public static List<ContextListener> getAll() {
		List<ContextListener> ret = new ArrayList<ContextListener>();
		ret.add(new DefaultResultProcessor(PointOperation.ResultProcessorName,
				PointOperation.initSqls, PointOperation.finishSqls));
		ret.add(new DefaultResultProcessor(SmsOperation.ResultProcessorName,
				SmsOperation.initSqls, SmsOperation.finishSqls));
		return ret;
	}
}
