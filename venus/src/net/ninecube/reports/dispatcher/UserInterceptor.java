/**
 * 
 * created on 2007-5-20
 */
package net.ninecube.reports.dispatcher;

import net.ninecube.reports.objects.ReportUser;
import net.ninecube.reports.providers.GroupProvider;
import net.ninecube.reports.providers.ProviderException;
import net.ninecube.reports.providers.UserProvider;

import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.AroundInterceptor;

/**
 * 
 * @author jxb
 */
public class UserInterceptor extends AroundInterceptor {
	private GroupProvider groupProvider;
	private ReportUser defaultUser;

	@Override
	protected void after(ActionInvocation dispatcher, String result) throws Exception {
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void before(ActionInvocation invocation) throws Exception {
		ActionContext.getContext().getSession().put("user", this.defaultUser);
	}

	public void setGroupProvider(GroupProvider groupProvider) throws ProviderException {
		this.groupProvider = groupProvider;
		this.defaultUser = createReportUser();
	}

	private ReportUser createReportUser() throws ProviderException {
		ReportUser ret = new ReportUser();
		ret.setId(1);
		ret.setName("admin");
		ret.setPassword("admin");
		ret.setGroups(this.groupProvider.getReportGroups());
		return ret;
	}
}
