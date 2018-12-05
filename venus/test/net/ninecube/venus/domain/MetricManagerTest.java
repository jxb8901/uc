/**
 * 
 * created on 2007-3-19
 */
package net.ninecube.venus.domain;

import java.util.List;

import net.ninecube.formula.Formula;
import net.ninecube.lang.Frequence;
import net.ninecube.venus.domain.impl.MetricManagerImpl;
import junit.framework.TestCase;

/**
 * 
 * @author jxb
 * 
 */
public class MetricManagerTest extends AbstractEntityManagerTestCase {
	private MetricManager mm;

	@Override
	protected void prepareTestInstance() throws Exception {
		super.setDependencyCheck(false);
		super.prepareTestInstance();
	}

	public void testGetByName() {
		this.assertNull(mm.getByName("name"));
		Metric m = newMetric();
		mm.save(m);
		
		Metric m1 = mm.getByName("name");
		this.assertNotNull(m1);
		this.assertEquals(m.getIdno(), m1.getIdno());
		this.assertEquals(m.getName(), m1.getName());
		this.assertEquals(m.getFormula(), m1.getFormula());
	}
	
	public void testGetByTypeAndFrequence() {
		this.assertEquals(0, mm.getByTypeAndFreqence("1", Frequence.MONTH).size());
		Metric m = newMetric();
		mm.save(m);
		
		List<Metric> m1 = mm.getByTypeAndFreqence("1", Frequence.MONTH);
		this.assertEquals(1, m1.size());
		m = newMetric();
		mm.save(m);
		m = newMetric();
		mm.save(m);
		m1 = mm.getByTypeAndFreqence("1", Frequence.MONTH);
		this.assertEquals(3, m1.size());
	}
	
	private Metric newMetric() {
		Metric m = new Metric();
		m.setIdno(2L);
		m.setName("name");
		m.setFormula("formula");
		m.setFrequence(Frequence.MONTH);
		m.setType("1");
		return m;
	}

	public void setMetricManager(MetricManager mm) {
		this.mm = mm;
	}

}
