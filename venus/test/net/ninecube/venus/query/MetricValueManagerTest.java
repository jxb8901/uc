/**
 * 
 * created on 2007-4-19
 */
package net.ninecube.venus.query;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import net.ninecube.lang.Frequence;
import net.ninecube.util.DateUtil;
import net.ninecube.venus.domain.AbstractEntityManagerTestCase;
import net.ninecube.venus.domain.CustomerGroup;
import net.ninecube.venus.domain.CustomerGroupManager;
import net.ninecube.venus.domain.Evaluation;
import net.ninecube.venus.domain.EvaluationManager;
import junit.framework.TestCase;

/**
 * 
 * @author jxb
 */
public class MetricValueManagerTest extends AbstractEntityManagerTestCase {
	private CustomerGroupManager cgm;
	private EvaluationManager em;
	private MetricValueManager mvm;

	public void testHibernateQuery() {
		Session s = super.sessionFactory.getCurrentSession();
		Query q = s.createQuery("from " + MetricValue.class.getName());
		List list = q.list();
		System.out.println(list.size());
		System.out.println(list);
		
		q = s.createQuery("from " + MetricValue.class.getName() + " where target.class=? and target.id=?");
		q.setParameter(0, CustomerGroup.class.getName()); // 这里不能使用cg.getClass().getName()，因为cg是被hibernate增强过的
		q.setParameter(1, 2L);
		list = q.list();
		System.out.println(list.size());
		System.out.println(list);
		
		q = s.getNamedQuery("getMetricValues");
		q.setParameter("targetId", 2L);
		q.setParameter("targetType", CustomerGroup.class.getName());
		q.setParameter("frequence", Frequence.DAY);
		q.setParameter("targetDateStart", DateUtil.fromYYYYMMDD("20070323"));
		q.setParameter("targetDateEnd", DateUtil.fromYYYYMMDD("20070323"));
		q.setParameter("evaluationId", 3L);
		list = q.list();
		System.out.println(list.size());
		System.out.println(list);
		
//		q = s.createQuery("select m.id from " + Evaluation.class.getName() + " e join e.metrics ms, " +MetricValue.class.getName() + " m join ms");
//		list = q.list();
//		System.out.println(list.size());
//		System.out.println(list);
	}
	
	public void testEvaluate() {
		List<EvaluationValues> vs = mvm.evaluate(cgm.create(), em.create());
		this.assertNotNull(vs);
	}

	public void setEvaluationManager(EvaluationManager em) {
		this.em = em;
	}

	public void setCustomergroupManager(CustomerGroupManager cgm) {
		this.cgm = cgm;
	}

	public void setMetricvalueManager(MetricValueManager mvm) {
		this.mvm = mvm;
	}

	public static void main(String[] a) {
		System.out.println(MetricValue.class.getSimpleName());
	}
}
