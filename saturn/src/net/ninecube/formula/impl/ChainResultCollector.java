/**
 * created on 2006-4-10
 */
package net.ninecube.formula.impl;

import java.util.ArrayList;
import java.util.List;

import net.ninecube.formula.Context;
import net.ninecube.formula.Result;
import net.ninecube.formula.ResultCollector;

import org.apache.log4j.Logger;

/**
 * @author JXB
 */
public class ChainResultCollector implements ResultCollector {
	private static final Logger log = Logger.getLogger(ChainResultCollector.class);
	private List<FilterResultCollector> chain = new ArrayList<FilterResultCollector>();
	
	public ChainResultCollector() {
		this.chain.add(new CacheResultCollector());
		this.chain.add(new DefaultResultCollector());
		this.chain.add(new FileResultCollector());
	}

	public Result getResult(final Context context) {
		for (FilterResultCollector r : chain) {
			if (r.isAccepted(context, null)) {
				Result ret = r.getResult(context);
				if (ret != null) return ret;
			}
		}
		return null;
	}

	public void addResult(Context context, Result result) {
		for (FilterResultCollector r : chain)
			if (r.isAccepted(context, result))
				r.addResult(context, result);
	}
}
