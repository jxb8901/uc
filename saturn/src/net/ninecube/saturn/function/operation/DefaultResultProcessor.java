/**
 * 
 * created on 2007-4-9
 */
package net.ninecube.saturn.function.operation;

import net.ninecube.db.DBUtil;
import net.ninecube.saturn.Context;

import org.apache.log4j.Logger;

/**
 * 
 * @author jxb
 * 
 */
public class DefaultResultProcessor implements ResultProcessor {
	private static final Logger log = Logger.getLogger(DefaultResultProcessor.class);
	private String name;
	private boolean processed = false;
	private String[] initSqls;
	private String[] finishSqls;

	public DefaultResultProcessor(String name, String[] initSqls, String[] finishSqls) {
		this.name = name;
		this.initSqls = initSqls;
		this.finishSqls = finishSqls;
	}

	public void setProcessed() {
		this.processed = true;
	}

	public String getName() {
		return this.name;
	}

	public void onFinished(Context context) {
		if (this.processed) commit(finishSqls);
	}

	public void onInit(Context context) {
		commit(initSqls);
	}
	
	private void commit(String[] sqls){
		for(String sql : sqls) {
			log.debug("update sql2:" + sql);
			DBUtil.update(sql, null);
		}
	}
}
