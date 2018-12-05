package net.ninecube.saturn.function.operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ninecube.saturn.Context;
import net.ninecube.saturn.RuleEngine;
import net.ninecube.saturn.database.Column;
import net.ninecube.saturn.database.DataSet;

import org.apache.log4j.Logger;
import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.expression.Expressions;

public abstract class AbstractOperation implements Operation {
	private static final Logger log = Logger.getLogger(AbstractOperation.class);

	private Map<String, Object> attributes = new HashMap<String, Object>();

	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	public void setAttribute(String name, Object val) {
		attributes.put(name, val);
	}

	public Object execute(Context context) {
		((ResultProcessor)context.getListenerByName(getProcessorName())).setProcessed();
		DataSet ds = populate(context);
				
		log.debug("operation sql : " + ds.getSql());
		context.getSqlExecutor().execute(ds.getSql());
		return null;
	}
	
	protected Expression getPromotionPlanID(Context context){
		String proplanidstr = "" + context.getVariables().get(RuleEngine.KEY_PROMOTION_PLAN_ID);
		if(proplanidstr == null || "".equals(proplanidstr)) proplanidstr = "-1";
		Long pid = new Long(-1);
		try{
			pid = new Long(proplanidstr);
		}catch(Exception exc){}
		return Expressions.literal(pid);
	}
	
	protected abstract String getOperateTableName();
	
	protected abstract DataSet populate(Context context);
	
	protected abstract String getProcessorName();
}
