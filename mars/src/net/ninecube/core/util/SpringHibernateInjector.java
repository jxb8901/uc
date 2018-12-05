/**
 * 
 * created on 2007-3-5
 */
package net.ninecube.core.util;

import java.util.List;

import org.hibernate.event.PostLoadEvent;
import org.hibernate.event.PostLoadEventListener;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

/**
 * 
 * @see http://www.blogjava.net/TrampEagle/articles/30263.html
 */

public class SpringHibernateInjector implements PostLoadEventListener, BeanFactoryAware {

	private AutowireCapableBeanFactory beanFactory;
	private List<String> allowedPackagePrefix;
	private List<String> deniedPackagePrefix;

	public void onPostLoad(PostLoadEvent event) {
		Object hibernateObject = event.getEntity();
		if (acceptObject(hibernateObject)) {
			beanFactory.autowireBeanProperties(hibernateObject, 
					AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, 
					false);
		}
	}
	
	private boolean acceptObject(Object obj) {
		if (obj == null) return false;
		// 只有在允许列表中存在且在拒绝列表中不存在时，才接受
		// 允许列表为空时认为“允许”
		// 拒绝列表为空时认为“允许”
		return containPrefix(obj.getClass().getName(), this.allowedPackagePrefix, true) &&
			!containPrefix(obj.getClass().getName(), this.deniedPackagePrefix, false);
	}
	
	private boolean containPrefix(String prefix, List<String> prefixes, boolean defaultValue) {
		if (prefixes == null || prefixes.isEmpty()) return defaultValue;
		for (String p : prefixes) {
			if (prefix.startsWith(p)) return true;
		}
		return false;
	}

	public void setBeanFactory(BeanFactory factory) {
		beanFactory = (AutowireCapableBeanFactory) factory;
	}

	public void setAllowedPackagePrefix(List<String> allowedPackagePrefix) {
		this.allowedPackagePrefix = allowedPackagePrefix;
	}

	public void setDeniedPackagePrefix(List<String> deniedPackagePrefix) {
		this.deniedPackagePrefix = deniedPackagePrefix;
	}
}
