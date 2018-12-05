/*
 * Created on 2004-3-18
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.ninecube.core.webwork.interceptor;

import java.util.Iterator;
import java.util.Map;

import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.Interceptor;

/**
 * 将请求参数中的XML特殊敏感字符转义
 * 
 * @author jxb
 */
public class EscapeXmlInterceptor implements Interceptor {
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String intercept(ActionInvocation invocation) throws Exception {
		Map parameters = ActionContext.getContext().getParameters();
		for (Iterator it = parameters.entrySet().iterator(); it.hasNext();) {
			Map.Entry e = (Map.Entry) it.next();
			Object value = e.getValue();
			if (value instanceof String) {
				e.setValue(filte((String) value));
			} else if (value instanceof String[]) {
				String[] v = (String[]) value;
				for (int i = 0; i < v.length; i++) {
					v[i] = filte(v[i]);
				}
				e.setValue(v);
			}
		}

		return invocation.invoke();
	}

	protected String filte(String s) {
		return s.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}

	public void destroy() {
	}

	public void init() {
	}
}
