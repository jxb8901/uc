/**
 * 
 * created on 2006-12-17
 */
package net.ninecube.reports.dispatcher;

import java.io.FileNotFoundException;

import net.ninecube.core.config.ActionConf;
import net.ninecube.core.webwork.WebworkUtils;
import net.ninecube.core.webwork.util.FreemarkerUtil;
import net.ninecube.reports.actions.QueryReportResultAction;
import net.ninecube.reports.objects.Report;
import net.ninecube.util.StringUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;

import com.opensymphony.webwork.views.freemarker.FreemarkerResult;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.util.OgnlValueStack;

/**
 * 
 * TODO: 考虑取消此类，在默认模板中使用include的方式实现
 * @author jxb
 * 
 */
public class QueryReportFreemarkerResult extends FreemarkerResult {
	private static final Log log = LogFactory.getLog(QueryReportFreemarkerResult.class);

	@Override
	public void execute(ActionInvocation invocation) throws Exception {
		setLocation(invocation);
		super.execute(invocation);
	}
	
	private void setLocation(ActionInvocation invocation) {
		String resultLocation = getTemplateFile(invocation);
		if (!StringUtil.isEmpty(resultLocation)) {
			super.setLocation(resultLocation);
			super.location = resultLocation; // TODO: 上面的调用为什么不起作用呢？
		}
	}

	protected String getTemplateFile(ActionInvocation invocation) 
	{
		Object action = invocation.getAction();
		if (action instanceof QueryReportResultAction)
		{
			QueryReportResultAction queryReportAction = (QueryReportResultAction) action;

			String ret = queryReportAction.getReportTemplateFile();
			if (!StringUtil.isEmpty(ret))
			{
				return ret;
			}
		}

		return null;
	}
}
