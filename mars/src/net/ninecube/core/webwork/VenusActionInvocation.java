/**
 * 
 * created on 2006-12-26
 */
package net.ninecube.core.webwork;

import java.util.Map;

import net.ninecube.core.config.ActionConf;
import net.ninecube.core.webwork.result.ServletActionRedirectResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.webwork.dispatcher.StreamResult;
import com.opensymphony.xwork.ActionChainResult;
import com.opensymphony.xwork.ActionProxy;
import com.opensymphony.xwork.DefaultActionInvocation;
import com.opensymphony.xwork.ObjectFactory;
import com.opensymphony.xwork.Result;
import com.opensymphony.xwork.XworkException;
import com.opensymphony.xwork.config.entities.ResultConfig;

/**
 * 
 * TODO: 1.根据交易配置取interceptor 
 * 
 * @author jxb
 * 
 */
public class VenusActionInvocation extends DefaultActionInvocation {
	private static final Log LOG = LogFactory.getLog(VenusActionInvocation.class);

	public VenusActionInvocation(ActionProxy proxy, Map extraContext, boolean pushAction) throws Exception {
		super(proxy, extraContext, pushAction);
	}

	public VenusActionInvocation(ActionProxy proxy, Map extraContext) throws Exception {
		super(proxy, extraContext);
	}

	public VenusActionInvocation(ActionProxy proxy) throws Exception {
		super(proxy);
	}

	@Override
	public Result createResult() throws Exception {
		ActionConf ac = WebworkUtils.getActionIfExists(this);
		if (ac != null) {
			String resultview = ac.getResultView(super.resultCode);
			// download
			if (resultview.startsWith("^")) {
				return createStreamResult();
			}
			// chain/redirect action, example:
			// >query
			// >query!submit
			// >/user/query
			// >/user/query!submit
			else if (resultview.startsWith(">") || resultview.startsWith("<")) {
				int namespaceIndex = resultview.lastIndexOf("/");
				int actionIndex = resultview.indexOf("!");
				String namespace, transaction, action = null;
				if (namespaceIndex != -1) {
					namespace = resultview.substring(1, namespaceIndex);
					if (actionIndex != -1) {
						transaction = resultview.substring(namespaceIndex, actionIndex);
						action = resultview.substring(actionIndex);
					} else {
						transaction = resultview.substring(namespaceIndex);
					}
				} else {
					namespace = ac.getOwner().getNamespace();
					if (actionIndex != -1) {
						transaction = resultview.substring(1, actionIndex);
						action = resultview.substring(actionIndex);
					} else {
						transaction = resultview.substring(1);
					}
				}
				Class clazz = resultview.startsWith(">") ? ActionChainResult.class : ServletActionRedirectResult.class; /* webwork中的实现会导致空指针 */
				return createChainResult(namespace, transaction, action, clazz);
			}
		}
		return super.createResult();
	}

	public Result createStreamResult() {
		ResultConfig resultConfig = new ResultConfig();
		resultConfig.setName(this.getResultCode());
		resultConfig.setClassName(StreamResult.class.getName());
		resultConfig.addParam("inputName", "inputStream");
		resultConfig.addParam("contentType", super.getStack().findValue("contentType", String.class));
		if (super.getStack().findValue("contentLength") != null)
			resultConfig.addParam("contentLength", super.getStack().findValue("contentLength"));
		resultConfig.addParam("contentDisposition", "inline; filename=" + super.getStack().findValue("fileName", String.class));

		try {
			return ObjectFactory.getObjectFactory().buildResult(resultConfig, invocationContext.getContextMap());
		} catch (Exception e) {
			LOG.error("There was an exception while instantiating the result of type " + resultConfig.getClassName(), e);
			throw new XworkException(e, resultConfig);
		}
	}

	public Result createChainResult(String namespace, String transaction, String action, Class clazz) {
		ResultConfig resultConfig = new ResultConfig();
		resultConfig.setName(this.getResultCode());
		resultConfig.setClassName(clazz.getName());
		resultConfig.addParam("actionName", transaction);
		resultConfig.addParam("namespace", namespace);
		if (action != null) resultConfig.addParam("method", action);

		try {
			return ObjectFactory.getObjectFactory().buildResult(resultConfig, invocationContext.getContextMap());
		} catch (Exception e) {
			LOG.error("There was an exception while instantiating the result of type " + resultConfig.getClassName(), e);
			throw new XworkException(e, resultConfig);
		}
	}
}
