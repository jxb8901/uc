/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package net.ninecube.core.webwork.interceptor;

import java.util.Iterator;
import java.util.Map;

import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.AroundInterceptor;
import com.opensymphony.xwork.interceptor.NoParameters;
import com.opensymphony.xwork.util.InstantiatingNullHandler;
import com.opensymphony.xwork.util.OgnlValueStack;
import com.opensymphony.xwork.util.XWorkConverter;
import com.opensymphony.xwork.util.XWorkMethodAccessor;

/**
 * 支持ognl中的object indexed properties的自动邦定拦截器
 * 
 * @author jxb
 * @see com.opensymphony.xwork.interceptor.ParameterInterceptor
 */
public class ObjectIndexedPropertiesParametersInterceptor extends AroundInterceptor {
	// ~ Methods
	// ////////////////////////////////////////////////////////////////

	private static final String PATTERN_OBJECT_INDEXED_PROPERTIES = "[a-zA-Z]+\\.[a-zA-Z]+\\[(\\'|\\\")[a-zA-Z]+(\\'|\\\")\\]";

	protected void after(ActionInvocation dispatcher, String result) throws Exception {
	}

	protected void before(ActionInvocation invocation) throws Exception {
		if (!(invocation.getAction() instanceof NoParameters)) {
			final Map parameters = ActionContext.getContext().getParameters();

			if (log.isDebugEnabled()) {
				log.debug("Setting params " + parameters);
			}

			try {
				invocation.getInvocationContext().put(InstantiatingNullHandler.CREATE_NULL_OBJECTS, Boolean.TRUE);
				// invocation.getInvocationContext().put(XWorkMethodAccessor.DENY_METHOD_EXECUTION,
				// Boolean.TRUE);
				// 允许方法调用. -- 可能会有安全问题.
				invocation.getInvocationContext().put(XWorkMethodAccessor.DENY_METHOD_EXECUTION, Boolean.FALSE);
				invocation.getInvocationContext().put(XWorkConverter.REPORT_CONVERSION_ERRORS, Boolean.TRUE);

				if (parameters != null) {
					final OgnlValueStack stack = ActionContext.getContext().getValueStack();

					for (Iterator iterator = parameters.entrySet().iterator(); iterator.hasNext();) {
						Map.Entry entry = (Map.Entry) iterator.next();
						String param = entry.getKey().toString();
						if (param.matches(PATTERN_OBJECT_INDEXED_PROPERTIES)) {
							if (log.isDebugEnabled()) {
								log.debug("setting params:" + param + "=" + entry.getValue());
							}
							stack.setValue(param, entry.getValue());
						}
					}
				}
			} finally {
				invocation.getInvocationContext().put(InstantiatingNullHandler.CREATE_NULL_OBJECTS, Boolean.FALSE);
				invocation.getInvocationContext().put(XWorkMethodAccessor.DENY_METHOD_EXECUTION, Boolean.FALSE);
				invocation.getInvocationContext().put(XWorkConverter.REPORT_CONVERSION_ERRORS, Boolean.FALSE);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.println("user.property['email']".matches(PATTERN_OBJECT_INDEXED_PROPERTIES));
		System.out.println("user.property[\"email\"]".matches(PATTERN_OBJECT_INDEXED_PROPERTIES));
	}
}
