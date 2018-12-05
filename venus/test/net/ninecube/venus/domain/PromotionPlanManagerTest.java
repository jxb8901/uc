package net.ninecube.venus.domain;

import java.util.Date;

import net.ninecube.util.FileUtil;

import org.hibernate.Session;

/**
 * @author  Fox
 */
public class PromotionPlanManagerTest extends AbstractEntityManagerTestCase {
	private PromotionPlanManager m;
	private CustomerGroupManager cgm;
	private RuleEntityManager rem;

	public void testCRUD() {
		CustomerGroup c = new CustomerGroup();
		c.setIdno(98L);
		cgm.save(c);
		RuleEntity r = new RuleEntity();
		rem.save(r);
		
		PromotionPlan cg = new PromotionPlan();
		cg.setIdno(99L);
		cg.setName("test group");
		cg.setCreationDate(new Date());
		cg.setTarget(c);
		cg.setRule(r);
		m.save(cg);
		this.assertNotSame(new Integer(99), cg.getIdno());

		this.assertEquals(1, m.getAll().size());
		this.assertEquals(1, super.countRowsInTable("TPromotionPlan"));
		
		cg = m.getById(cg.getIdno());
		this.assertNotNull(cg);
		this.assertEquals("test group", cg.getName());
		
		m.delete(cg);
		this.assertEquals(0, m.getAll().size());
		this.assertEquals(0, super.countRowsInTable("TPromotionPlan"));
	}
	
	public void xtestBlob() {
		CustomerGroup c1 = new CustomerGroup();
		c1.setIdno(98L);
		cgm.save(c1);
		
		PromotionPlan cg = new PromotionPlan();
		cg.setIdno(99L);
		cg.setTarget(c1);
		//cg.setRule("test");
		m.save(cg);
		super.sessionFactory.getCurrentSession().flush();
		this.setComplete();
		System.out.println("*****1****");
		super.sessionFactory.evict(cg.getClass());
		System.out.println("*****2****");
		
		cg = m.getById(cg.getIdno());
		super.sessionFactory.getCurrentSession().evict(cg);
		System.out.println("*****3****");
		cg = m.getById(cg.getIdno());
		System.out.println("*****4****");
		cg.getRule();
	}
	
	public void xtest() throws Exception {
		CustomerGroup c1 = new CustomerGroup();
		cgm.save(c1);
		
		PromotionPlan cg = new PromotionPlan();
		cg.setIdno(99L);
		cg.setTarget(c1);
		//cg.setRule(FileUtil.getFileContent("rule4.rl"));
		
		m.save(cg);
	}
	
	public void setCustomergroupManager(CustomerGroupManager cgm) {
		this.cgm = cgm;
	}
	
	public void setRuleentityManager(RuleEntityManager rem) {
		this.rem = rem;
	}
	
	public void setPromotionplanManager(PromotionPlanManager cgm) {
		this.m = cgm;
	}
}
