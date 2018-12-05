/**
 * 
 * created on 2007-3-19
 */
package net.ninecube.venus.domain;


/**
 * 
 * @author jxb
 * 
 */
public class EvaluationManagerTest extends AbstractEntityManagerTestCase {
	private EvaluationManager em;
	private MetricManager mm;

	public void testGetMetrics() {
		this.assertEquals(0, em.getAll().size());
		Metric m = new Metric();
		m.setName("m1");
		Evaluation e = new Evaluation();
		e.setName("e1");
		e.getMetrics().add(m);
		em.save(e);
		super.setComplete();
		try {
			super.endTransaction();
			this.fail("metrics中的对象必须要是持久化对象");
		} catch (RuntimeException ignore) { }
		
		mm.save(m);
		e = new Evaluation();
		e.setName("e1");
		e.getMetrics().add(m);
		em.save(e);
		super.setComplete();
		super.endTransaction();
		super.startNewTransaction();
		this.assertEquals(1, em.getAll().size());
		e = em.getAll().get(0);
		this.assertEquals(1, e.getMetrics().size());
		super.endTransaction();
		super.startNewTransaction();
	}

	public void setEvaluationManager(EvaluationManager em) {
		this.em = em;
	}

	public void setMetricManager(MetricManager mm) {
		this.mm = mm;
	}

}
