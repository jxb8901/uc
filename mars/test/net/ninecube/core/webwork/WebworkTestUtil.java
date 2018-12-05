/**
 * 
 * created on 2007-1-24
 */
package net.ninecube.core.webwork;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ninecube.core.config.WebConfigManager;
import net.ninecube.core.config.impl.WebConfigManagerImpl;
import net.ninecube.core.security.PermissionChecker;
import net.ninecube.core.security.User;
import net.ninecube.core.webwork.interceptor.AuthorizeInterceptor;
import net.ninecube.core.webwork.util.VenusBasicTypeConverter;
import net.ninecube.core.webwork.util.VenusObjectTypeDeterminer;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionProxy;
import com.opensymphony.xwork.ActionProxyFactory;
import com.opensymphony.xwork.ObjectFactory;
import com.opensymphony.xwork.config.ConfigurationManager;
import com.opensymphony.xwork.config.entities.InterceptorMapping;
import com.opensymphony.xwork.spring.SpringObjectFactory;

/**
 * 
 * @author jxb
 * 
 */
public class WebworkTestUtil {
	
	public static void setup(ApplicationContext context) throws Exception {
		SpringObjectFactory f = new SpringObjectFactory();
		f.setApplicationContext(context);
		f.setAutowireStrategy(AutowireCapableBeanFactory.AUTOWIRE_BY_NAME);
		f.setUseClassCache(false);
		ObjectFactory.setObjectFactory(f);
		setup();
	}

	public static void setup() throws Exception {
		ConfigurationManager.clearConfigurationProviders();
		ValidatorAdaptor vs = (ValidatorAdaptor) Class.forName("net.ninecube.core.webwork.ValidatorAdaptorImpl").newInstance();
		new VenusActionValidatorManager(vs).init();
		new VenusActionProxyFactory().init();
		WebConfigManager wcm = new WebConfigManagerImpl("venus-test.xml");
		wcm.init();
		new VenusConfigurationProvider(wcm).init();
		new VenusObjectTypeDeterminer().init();
		new VenusBasicTypeConverter().init();
		
		Map<String, String> params = new HashMap<String, String>();
		HashMap<Object, Object> extraContext = new HashMap<Object, Object>();
		extraContext.put(ActionContext.PARAMETERS, params);
		extraContext.put(ActionContext.APPLICATION, new HashMap());
		extraContext.put(ActionContext.SESSION, new HashMap());
		extraContext.put(ServletActionContext.HTTP_REQUEST, new MockHttpServletRequest());
		extraContext.put(ServletActionContext.HTTP_RESPONSE, new MockHttpServletResponse());
		extraContext.put(ServletActionContext.SERVLET_CONTEXT, new MockServletContext());
		ServletActionContext.getContext().setContextMap(extraContext);
		ServletActionContext.getContext().put(ActionContext.DEV_MODE, Boolean.TRUE);
	}
	
	public static ActionProxy getActionProxy(String namespace, String action) throws Exception {
		ActionProxy ret = ActionProxyFactory.getFactory().createActionProxy(
				namespace, action, ServletActionContext.getContext().getContextMap());
		disableAuthorize(ret);
		return ret;
	}

	@SuppressWarnings("unchecked")
	private static void disableAuthorize(ActionProxy proxy) {
		List<InterceptorMapping> interceptors = proxy.getConfig().getInterceptors();
		for (InterceptorMapping interceptor : interceptors) {
			if (interceptor.getInterceptor() instanceof AuthorizeInterceptor) {
				((AuthorizeInterceptor) interceptor.getInterceptor())
				.setPermissionChecker(new PermissionChecker() {
					@Override
					public boolean isGrant(User user, String permission) {
						return true;
					}
				});
			}
		}
	}
}
