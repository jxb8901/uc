/**
 * 
 * created on 2007-1-21
 */
package net.ninecube.core.webwork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ninecube.core.config.TransactionConf;
import net.ninecube.core.config.TransactionType;

import com.opensymphony.xwork.config.entities.ActionConfig;
import com.opensymphony.xwork.config.entities.InterceptorMapping;
import com.opensymphony.xwork.config.entities.PackageConfig;
import com.opensymphony.xwork.config.entities.ResultConfig;

/**
 * 
 * @author jxb
 * 
 */
public class TransactionAdaptor {
	
	public void adaptor(TransactionConf t, PackageConfig packageContext) {
		if(t.getType() == TransactionType.xwork) return;
		String className = t.getTransactionClassName();
		if (t.getType() != null && t.getType().getTransactionClass() != null)
			className = t.getType().getTransactionClass().getName();
		addActionConfig(packageContext, t.getName(), className); 
	}
	
	private void addActionConfig(PackageConfig packageContext, String actionName, String className) {
		String methodName = null;

		Map<String, String> actionParams = new HashMap<String, String>();
		Map<String, ResultConfig> results = new HashMap<String, ResultConfig>();
		List<InterceptorMapping> interceptorList = new ArrayList<InterceptorMapping>();
		List externalrefs = new ArrayList();
		List exceptionMappings = new ArrayList();

		ActionConfig actionConfig = new ActionConfig(methodName, className, 
				actionParams, results, interceptorList, externalrefs, 
				exceptionMappings, packageContext.getName());
		packageContext.addActionConfig(actionName, actionConfig);
	}
}
