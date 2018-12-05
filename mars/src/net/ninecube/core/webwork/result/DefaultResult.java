/**
 * 
 * created on 2006-12-17
 */
package net.ninecube.core.webwork.result;

import net.ninecube.core.config.ActionConf;
import net.ninecube.core.webwork.WebworkUtils;
import net.ninecube.core.webwork.util.FreemarkerUtil;
import net.ninecube.util.StringUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.webwork.views.freemarker.FreemarkerResult;
import com.opensymphony.xwork.ActionInvocation;

/**
 * 
 * @author jxb
 * 
 */
public class DefaultResult extends FreemarkerResult {
	private static final Log log = LogFactory.getLog(DefaultResult.class);
	private String resultSuffix = ".ftl";

	@Override
	public void execute(ActionInvocation invocation) throws Exception {
		ActionConf tc = WebworkUtils.getAction(invocation);		
		setLocation(invocation, tc, false);
		if (log.isDebugEnabled()) log.debug(getLogMessage(invocation));
		
		if (!FreemarkerUtil.isTemplateExists(super.location)) {
			if (setLocation(invocation, tc, true))
				if (log.isDebugEnabled()) log.debug("use default view! " + getLogMessage(invocation));
		}
		
		super.execute(invocation);
	}
	
	private boolean setLocation(ActionInvocation invocation, ActionConf tc, boolean useDefault) {
		String resultcode = invocation.getResultCode();
		String resultLocation = (useDefault ? tc.getDefaultResultView(resultcode) : tc.getResultView(resultcode));
		if (StringUtil.isEmpty(resultLocation)) return false;
		resultLocation += resultSuffix;
		super.setLocation(resultLocation);
		super.location = resultLocation; // TODO: 上面的调用为什么不起作用呢？
		return true;
	}
	
	private String getLogMessage(ActionInvocation invocation) {
		return "action result: '" + super.location + "' for action '" + 
		invocation.getProxy().getActionName() + "." + 
		invocation.getProxy().getMethod() + "-" + invocation.getResultCode() + "'";
	}
}
