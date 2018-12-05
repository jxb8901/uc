package net.ninecube.core.webwork.interceptor;

import net.ninecube.core.context.RequestContext;
import net.ninecube.core.exception.NoPermissionException;
import net.ninecube.core.security.PermissionChecker;
import net.ninecube.core.security.User;

import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.ActionProxy;
import com.opensymphony.xwork.interceptor.Interceptor;

/**
 * @author mike
 *
 */
public class AuthorizeInterceptor implements Interceptor {
	private PermissionChecker permissionChecker;
	
	public void destroy() {
	}

	public void init() {
	}

	public String intercept(ActionInvocation invocation) throws Exception {
		ActionProxy proxy = invocation.getProxy();
		String requiredPerm = proxy.getNamespace() + "/" + proxy.getActionName();
		User curUser = RequestContext.get().getUser();
		if(!this.permissionChecker.isGrant(curUser, requiredPerm)){
			throw new NoPermissionException("Require permission '" + requiredPerm + "';");
		}
		RequestContext.get().setPermissionChecker(this.permissionChecker);
		return invocation.invoke();
	}
	
	public void setPermissionChecker(PermissionChecker permissionChecker){
		this.permissionChecker = permissionChecker;
	}

}
