/**
 * 
 * created on 2007-5-10
 */
package net.ninecube.core.webwork.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.ninecube.core.context.RequestContext;
import net.ninecube.core.security.User;
import net.ninecube.util.RandomStringUtils;
import net.ninecube.util.StringUtil;

import org.apache.log4j.MDC;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.AroundInterceptor;

/**
 * 
 * @author jxb
 */
public class SessionInterceptor extends AroundInterceptor {

	@Override
	protected void before(ActionInvocation invocation) throws Exception {
		String ip = getIp(ServletActionContext.getRequest());
		// 设置log4j日志需要的变量
		MDC.put("ip", ip);
		MDC.put("random", RandomStringUtils.randomAlphanumeric(4));

		RequestContext.get().release();
		Map<String, Object> usersession = getWebworkSession();
		RequestContext.get().setSession(usersession);
		RequestContext.get().setUser((User) usersession.get(RequestContext.KEY_CURRENT_USER));
		RequestContext.get().setIp(ip);
	}

	@Override
	protected void after(ActionInvocation dispatcher, String result) throws Exception {
		// 检查用户是否登出，如果是，则使session失效
		if (RequestContext.get().getUser() == null) {
			HttpServletRequest request = ServletActionContext.getRequest();
			if (request != null) {
				HttpSession session = request.getSession(false);
				if (session != null) session.invalidate();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> getWebworkSession() {
		return ActionContext.getContext().getSession();
	}

	private String getIp(HttpServletRequest request) {
		if (request != null) {
			String ip = request.getRemoteAddr();
			if (!StringUtil.isEmpty(ip)) return ip;
		}
		return "0.0.0.0";
	}
}
