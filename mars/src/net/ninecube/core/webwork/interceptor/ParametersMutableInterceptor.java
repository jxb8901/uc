/*
 * Created on 2004-3-18
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.ninecube.core.webwork.interceptor;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.Interceptor;

/**
 * 将当前请求的参数Map由不可变改为可变，以便后续可以修改请求参数
 * 
 * @author jxb
 */
public class ParametersMutableInterceptor implements Interceptor {
	private static final Log log = LogFactory.getLog(ParametersMutableInterceptor.class);

	@SuppressWarnings("unchecked")
	public String intercept(ActionInvocation invocation) throws Exception {
		Map mutableParameters = new HashMap();
		mutableParameters.putAll(ActionContext.getContext().getParameters());
		ActionContext.getContext().setParameters(mutableParameters);
		if (log.isDebugEnabled()) log.debug("request parameters: " + mutableParameters);

		return invocation.invoke();
	}

	public void destroy() {
	}

	public void init() {
	}
}
