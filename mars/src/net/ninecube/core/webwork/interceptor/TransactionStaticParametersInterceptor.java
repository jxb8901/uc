/**
 * 
 * created on 2007-1-20
 */
package net.ninecube.core.webwork.interceptor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.ninecube.core.config.ActionConf;
import net.ninecube.core.config.FieldConf;
import net.ninecube.core.webwork.WebworkUtils;

import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.config.entities.Parameterizable;
import com.opensymphony.xwork.interceptor.StaticParametersInterceptor;
import com.opensymphony.xwork.util.OgnlValueStack;
import com.opensymphony.xwork.util.TextParseUtil;

/**
 * 设置交易静态参数，从WEBWORK中扩展，修改了以下方面：
 * 1：修改取静态参数的方式，增加了一个getStaticParameters方法取需要设置的静态参数
 * 2：修改原类两setValue的调用，似乎没有必要，而且当参数值需要parse时会出错
 * 
 * @author jxb
 * 
 */
public class TransactionStaticParametersInterceptor extends StaticParametersInterceptor {

	@Override
	protected void before(ActionInvocation invocation) throws Exception {
		ActionConf ac = WebworkUtils.getActionIfExists(invocation);
		if (ac == null) {
			super.before(invocation);
		}
		else {
	        Object action = invocation.getAction();
	
	        final Map parameters = getStaticParameters(ac);
	
	        if (log.isDebugEnabled()) {
	            log.debug("Setting static parameters " + parameters);
	        }
	
	        // for actions marked as Parameterizable, pass the static parameters directly
	        if (action instanceof Parameterizable) {
	            ((Parameterizable) action).setParams(parameters);
	        }
	
	        if (parameters != null) {
	            final OgnlValueStack stack = ActionContext.getContext().getValueStack();
	
	            for (Iterator iterator = parameters.entrySet().iterator();
	                 iterator.hasNext();) {
	                Map.Entry entry = (Map.Entry) iterator.next();
	                /* 为什么要在这里调用 setValue方法呢？
	                 * 如果参数值为${value}形式, setValue方法可能出错
	                stack.setValue(entry.getKey().toString(), entry.getValue());
	                */
	                Object val = entry.getValue();
	                if (val instanceof String) {
	                    val = TextParseUtil.translateVariables((String) val, stack);
	                }
	                stack.setValue(entry.getKey().toString(), val);
	            }
	        }
		}
	}

	public Map<String, ?> getStaticParameters(ActionConf ac) {
		Map<String, Object> ret = new HashMap<String, Object>();
		for (FieldConf fc : ac.getInputStaticFields()) {
			ret.put(fc.getName(), fc.getValue());
		}
		return ret;
	}
}
