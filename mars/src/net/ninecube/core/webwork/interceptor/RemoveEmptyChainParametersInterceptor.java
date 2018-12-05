/*
 * Created on 2004-3-18
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.ninecube.core.webwork.interceptor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.ninecube.util.StringUtil;

import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.Interceptor;

/**
 * 将当前请求参数中name形如"customer.idno"且值为空的参数从parameters中去除
 * 因为webwork会自动创建null对象，所以这样的对象在hibernate中保存时会引发
 * org.hibernate.TransientObjectException异常
 * @TODO: 有其它更好的方法吗？
 * 
 * @author jxb
 */
public class RemoveEmptyChainParametersInterceptor implements Interceptor {

	@SuppressWarnings("unchecked")
	public String intercept(ActionInvocation invocation) throws Exception {
		Map mutableParameters = new HashMap();
		Map<String, Object> parameters = invocation.getInvocationContext().getParameters();
		Iterator<Map.Entry<String, Object>> iterator = parameters.entrySet().iterator();
		for (; iterator.hasNext(); ) {
			Map.Entry<String, Object> en = iterator.next();
			if (StringUtil.isEmpty(en.getValue()) && en.getKey().indexOf(".") != -1)
				iterator.remove();
		}

		return invocation.invoke();
	}

	public void destroy() {
	}

	public void init() {
	}
}
