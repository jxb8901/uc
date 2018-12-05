/**
 * 
 * created on 2007-1-20
 */
package net.ninecube.core.webwork.interceptor;

import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.ParametersInterceptor;

/**
 * 
 * @author jxb
 * 
 */
public class TransactionParametersInterceptor extends ParametersInterceptor {

	@Override
	protected void before(ActionInvocation invocation) throws Exception {
		super.before(invocation);
	}

	@Override
    protected boolean acceptableName(String name) {
        if (name.indexOf('=') != -1 || name.indexOf(',') != -1 || name.indexOf('#') != -1
                || name.indexOf(':') != -1) {
            return false;
        } else {
            return true;
        }
    }
}
