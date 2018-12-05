package net.ninecube;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.ninecube.core.domain.Entity;
import net.ninecube.core.domain.GenericEntityManager;
import net.ninecube.core.query.hibernate.HibernateUtil;
import net.ninecube.core.trans.EntityCreateTransaction;
import net.ninecube.reports.objects.*;
import net.ninecube.test.AbstractEntityManagerTestCase;
import net.ninecube.venus.domain.*;

import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.PropertyValueException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @author  jxb
 */
@SuppressWarnings("unchecked")
public abstract class CopyVenusData {
	private static final Log log = LogFactory.getLog(CopyVenusData.class);
	private static final String SOURCE = "mysql";
//	private static final String SOURCE = "sqlserver1";
	private static final String DESTINATION = "hsql.alone";
//	private static final String DESTINATION = "hsql.mem";
//	private static final String DESTINATION = "sqlserver";
//	private static final String DESTINATION = "mysql";
//	private static final String DESTINATION = "sqlserver";
	private static Class<Entity>[] entities = new Class[] {
		Role.class, User.class,
		RuleEntity.class, Metric.class, Evaluation.class, CustomerGroup.class, PromotionPlan.class,
		PointType.class,
		ReportChart.class, ReportParameter.class, ReportExportOption.class, Report.class,
	};

	public static void main(String[] a) throws Exception {
		Tester t1 = new Tester(SOURCE);
		Tester t2 = new Tester(DESTINATION);
		t2.deleteData(entities);
		
		t2 = new Tester(DESTINATION);
		List<Entity> all = t1.loadData(entities);
		t2.copyData(all);
	}

	private static class Tester extends AbstractEntityManagerTestCase {
		private String type;
		public Tester(String type) { this.type = type; }
		
		private List<Entity> loadData(Class<Entity>[] entities) throws Exception {
			this.setUp();
			try {
				List<Entity> all = new ArrayList<Entity>();
				for (Class<Entity> en : entities) {
					all.addAll(this.getSession().createQuery("from " + en.getName()).list());
				}
				int i = 0;
				for (Entity en : all) {
					initializeHibernateEntity(en);
				}
				return all;
			}
			finally {
				this.tearDown();
			}
		}
		
		private void deleteData(Class<Entity>[] entities) throws Exception {
			this.setUp();
			try {
				for (int i = entities.length - 1; i >= 0; i--) {
					GenericEntityManager em2 =  this.createEntityManager(entities[i]);
					List list = em2.getAll();
					if (list != null && !list.isEmpty()) {
						log.debug("删除" + list.size() + "条记录，表：" + entities[i].getSimpleName());
						em2.getHibernateTemplate().deleteAll(list);
					}
				}
				this.setComplete();
			}
			finally {
				this.tearDown();
			}
		}
		
		private void copyData(List<Entity> all) throws Exception {
				int index = 0, limit = all.size() * 3;
				while (!all.isEmpty()) {
					index++;
					if (index > limit) throw new Exception("可能出现实体的相互依赖：" + Arrays.asList(all));
					Entity en = all.remove(0);
					try {
						saveEntity(en);
					}
					catch (DataIntegrityViolationException e) {
						if (e.getCause() instanceof ConstraintViolationException || e.getCause() instanceof PropertyValueException) {
							log.debug("ConstraintViolation, try again later: " + e.getMessage());
							all.add(en);
						}
						else {
							throw e;
						}
					}
				}
		}
		
		private void saveEntity(Entity en) throws Exception {
			this.setUp();
			try {
				GenericEntityManager em2 =  this.createEntityManager(en.getClass());
				Object before_id = HibernateUtil.getSerializablePrimaryKeyValue(em2.getSessionFactory(), en);
				em2.save(en);
				Object after_id = HibernateUtil.getSerializablePrimaryKeyValue(em2.getSessionFactory(), en);
				log.debug("插入记录,表：" + en.getClass().getSimpleName() + "ID: " + before_id + "->" + after_id + ", [" + en + "]");
				this.setComplete();
			}
			finally {
				this.tearDown();
			}
		}

		@Override
		protected String[] getConfigLocations() {
			return new String[] { 
					"applicationContext-hibernate.xml",
					"applicationContext-" + this.type + ".xml"
			};
		}
		
	}
	
	private static void initializeHibernateEntity(Entity entity) {
		Hibernate.initialize(entity);
		for (Field f : entity.getClass().getDeclaredFields()) {
			if (Collection.class.isAssignableFrom(f.getDeclaringClass()) ||
					Map.class.isAssignableFrom(f.getDeclaringClass())) {
				log.debug("发现集合属性，准备强制初始化:" + entity.getClass() + " -- " + f.getName());
				try {
					f.setAccessible(true);
					Hibernate.initialize(f.get(entity));
				} catch (Exception e) {
					log.error("强制初始化hibernate代理出错", e);
				} 
			}
		}
		for (Method m : entity.getClass().getMethods()) {
			if (m.getName().startsWith("get") &&
					m.getParameterTypes().length == 0 &&
					(Collection.class.isAssignableFrom(m.getReturnType()) ||
							Map.class.isAssignableFrom(m.getReturnType()))) {
				log.debug("发现集合属性，准备强制初始化:" + entity.getClass() + " -- " + m.getName());
				try {
					Hibernate.initialize(m.invoke(entity, new Object[0]));
				} catch (Exception e) {
					log.error("强制初始化hibernate代理出错", e);
				} 
			}
		}
	}
}
