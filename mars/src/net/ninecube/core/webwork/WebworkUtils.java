/**
 * 
 * created on 2006-12-17
 */
package net.ninecube.core.webwork;

import net.ninecube.core.config.ActionConf;
import net.ninecube.core.config.ConfigException;
import net.ninecube.core.config.TransactionConf;
import net.ninecube.core.config.WebConfigManager;

import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionInvocation;

/**
 * 
 * @author jxb
 * 
 */
public class WebworkUtils {
	
	public static ActionConf getAction(ActionInvocation invocation) {
		ActionConf ret = getActionIfExists(invocation);
		if (ret == null) throw new ConfigException("can't find transaction config in action: '" + 
				invocation.getProxy().getNamespace() + "/" + invocation.getProxy().getActionName() + "'");
		return ret;
	}

	public static ActionConf getActionIfExists(ActionInvocation invocation) {
		if (invocation == null) return null;
		String actionName = invocation.getProxy().getActionName();
		String method = invocation.getProxy().getMethod();
		String namespace = invocation.getProxy().getNamespace();
		TransactionConf t = WebConfigManager.get().getConfiguration().getTransaction(namespace, actionName);
		if (t == null) return null;
		return t.getAction(method);
	}
	
	public static ActionConf getAction() {
		return getAction(ActionContext.getContext().getActionInvocation());
	}

	public static ActionConf getActionIfExists() {
		return getActionIfExists(ActionContext.getContext().getActionInvocation());
	}
	
	public static TransactionConf getTransaction(ActionInvocation invocation) {
		TransactionConf ret = getTransactionIfExists(invocation);
		if (ret == null) throw new ConfigException("can't find transaction config in action: '" + 
				invocation.getProxy().getNamespace() + "/" + invocation.getProxy().getActionName() + "'");
		return ret;
	}

	public static TransactionConf getTransactionIfExists(ActionInvocation invocation) {
		String actionName = invocation.getProxy().getActionName();
		String method = invocation.getProxy().getMethod();
		String namespace = invocation.getProxy().getNamespace();
		return WebConfigManager.get().getConfiguration().getTransaction(namespace, actionName);
	}
	
	public static TransactionConf getTransaction() {
		return getTransaction(ActionContext.getContext().getActionInvocation());
	}

	public static TransactionConf getTransactionIfExists() {
		return getTransactionIfExists(ActionContext.getContext().getActionInvocation());
	}

	/**
	 * 偿试根据类名查找交易定义，如果找不到返回null
	 */
	public static ActionConf getActionByClassAndAction(Class clazz, String action) {
		TransactionConf t =  WebConfigManager.get().getConfiguration().getTransactionByClass(clazz.getName());
		if (t == null) return null;
		return t.getAction(action);
	}
}
