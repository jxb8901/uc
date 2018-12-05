/**
 * 
 * created on 2007-1-30
 */
package net.ninecube.venus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import junit.framework.TestCase;
import net.ninecube.core.domain.AbstractEntityManager;
import net.ninecube.core.domain.Entity;
import net.ninecube.core.domain.EntityManager;
import net.ninecube.venus.domain.CustomerGroup;
import net.ninecube.venus.domain.CustomerGroupManager;
import net.ninecube.venus.domain.PromotionPlan;
import net.ninecube.venus.domain.impl.CustomerGroupManagerImpl;
import net.ninecube.venus.domain.impl.PromotionPlanManagerImpl;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;

/**
 * 
 * @author jxb
 * 
 */
public class AspertJPointcutTest extends TestCase {
	
	public void xtestMatchExplicit() throws Exception {
		//String expression = "execution(* net.ninecube..domain..*Manager*.*(..))";
		String expression = "execution(* *(..))";

		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression(expression);
		
		Class c = PromotionPlanManagerImpl.class;
		Method m1 = PromotionPlanManagerImpl.class.getMethod(
				"save", new Class[]{Entity.class});
		Method m2 = PromotionPlanManagerImpl.class.getMethod(
				"save", new Class[]{PromotionPlan.class});
		
		assertTrue(pointcut.matches(EntityManager.class));
		assertTrue(pointcut.matches(
				PromotionPlanManagerImpl.class.getMethod(
						"save", new Class[]{Entity.class}), 
						PromotionPlanManagerImpl.class));
		
		assertTrue(pointcut.matches(AbstractEntityManager.class));
		assertTrue(pointcut.matches(
				AbstractEntityManager.class.getMethod(
						"save", new Class[]{Entity.class}), 
				AbstractEntityManager.class));

		assertTrue(pointcut.matches(PromotionPlanManagerImpl.class));
		assertTrue(pointcut.matches(
				PromotionPlanManagerImpl.class.getMethod(
						"save", new Class[]{PromotionPlan.class}), 
						PromotionPlanManagerImpl.class));
		assertTrue(pointcut.matches(
				EntityManager.class.getMethod(
						"save", new Class[]{Entity.class}), 
						EntityManager.class));
		assertTrue(pointcut.matches(
				PromotionPlanManagerImpl.class.getMethod(
						"save", new Class[]{Entity.class}), 
						PromotionPlanManagerImpl.class));
	}
	
	@SuppressWarnings("unchecked")
	public void test() throws Exception {
		Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
				new Class[]{
					CustomerGroupManager.class, EntityManager.class
				}, new InvocationHandler() {
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						System.out.println("proxy:"+proxy.getClass());
						System.out.println("method:" + method.getName() +"," +method.getDeclaringClass() +","+ method.getParameterTypes()[0]);
						System.out.println("args:"+args[0].getClass());
						return null;
					}
				});
		CustomerGroup cg = new CustomerGroup();
		CustomerGroupManager cm = (CustomerGroupManager)proxy;
		cm.save(cg);
		EntityManager em = (EntityManager)proxy;
		em.save(cg);
		em.save((Entity)cg);
		EntityManager<Entity> eem = (EntityManager<Entity>)proxy;
		eem.save(cg);
		eem.save((Entity)cg);
		
		System.out.println(EntityManager.class.getMethod("save", new Class[]{Entity.class}).getDeclaringClass());
		System.out.println(CustomerGroupManager.class.getMethod("save", new Class[]{Entity.class}).getDeclaringClass());
		System.out.println(CustomerGroupManagerImpl.class.getMethod("save", new Class[]{Entity.class}).getDeclaringClass());
	}
}
