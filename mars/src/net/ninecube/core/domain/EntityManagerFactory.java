package net.ninecube.core.domain;

import org.hibernate.SessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import net.ninecube.core.context.ServiceLocator;

public class EntityManagerFactory implements BeanFactoryAware {
	private static EntityManagerFactory instance;
	private SessionFactory sessionFactory;
	private BeanFactory beanFactory;
	
	public static EntityManagerFactory get(){
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Entity> EntityManager<T> getEntityManager(Class<T> entityClass){
		String mname = entityClass.getSimpleName() + "Manager";
		if(ServiceLocator.get().containBean(mname)){
			return (EntityManager<T>) beanFactory.getBean(mname);
		}
		return new ReadonlyEntityManager<T>(sessionFactory, entityClass);
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
	
	public void init() {
		instance = this;
	}
	
}
