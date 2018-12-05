/*
 * Created on 2004-3-19
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.ninecube.core.context;

import javax.sql.DataSource;

import net.ninecube.lang.BaseException;
import net.ninecube.db.DBManager;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * @author jxb
 */
public class ServiceLocator implements BeanFactoryAware {
	private static ServiceLocator instance = new ServiceLocator();
	private BeanFactory factory;
	private DataSource dataSource;

	public static ServiceLocator get() {
		return instance;
	}
	
	public static void set(ServiceLocator instance) {
		ServiceLocator.instance = instance;
	}
	
	public Object getBean(String name) {
		if (factory != null) {
			return factory.getBean(name);
		}

		throw new BaseException("未初始化Bean工厂");
	}
	
	public boolean containBean(String name){
		return factory.containsBean(name);
	}
	
	public void init() {
		ServiceLocator.set(this);
		DBManager.setDataSource(this.dataSource);
	}

	public void setBeanFactory(org.springframework.beans.factory.BeanFactory factory) {
		this.factory = factory;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
