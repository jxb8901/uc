/**
 * 
 * created on 2006-12-26
 */
package net.ninecube.core.webwork;

import java.util.Map;

import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.ActionProxy;
import com.opensymphony.xwork.ActionProxyFactory;
import com.opensymphony.xwork.DefaultActionProxyFactory;

/**
 * 
 * @author jxb
 * 
 */
public class VenusActionProxyFactory extends DefaultActionProxyFactory {
	
	public void init() {
		ActionProxyFactory.setFactory(this);
	}

	@Override
	public ActionInvocation createActionInvocation(ActionProxy actionProxy, Map extraContext, boolean pushAction) throws Exception {
		return new VenusActionInvocation(actionProxy, extraContext, pushAction);
	}

	@Override
	public ActionInvocation createActionInvocation(ActionProxy actionProxy, Map extraContext) throws Exception {
		return new VenusActionInvocation(actionProxy, extraContext);
	}

	@Override
	public ActionInvocation createActionInvocation(ActionProxy actionProxy) throws Exception {
		return new VenusActionInvocation(actionProxy);
	}

}
