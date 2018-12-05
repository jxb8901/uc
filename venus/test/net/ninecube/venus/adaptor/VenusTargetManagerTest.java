/**
 * 
 * created on 2007-3-19
 */
package net.ninecube.venus.adaptor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.ninecube.core.domain.Entity;
import net.ninecube.core.domain.EntityManager;
import net.ninecube.core.query.Page;
import net.ninecube.core.query.QueryCondition;
import net.ninecube.formula.impl.TargetManagerSupport.TargetEntity;
import net.ninecube.venus.adaptor.VenusTargetManager;
import junit.framework.TestCase;

/**
 * 
 * @author jxb
 * 
 */
public class VenusTargetManagerTest extends TestCase {
	private TestEntityManager tem;
	private VenusTargetManager vtm;
	
	protected void setUp() throws Exception {
		this.tem = new TestEntityManager();
		this.tem.list = new ArrayList<TestEntity>();
		this.tem.entity = new TestEntity();
		this.tem.entity.setIdno(2L);
		this.tem.entity.setName("test name");
		this.tem.entity.setRulestring("rule rule");
		
		this.vtm = new VenusTargetManager();
		Properties p = new Properties();
		p.put("id", "idno");
		p.put("name", "name");
		p.put("rule", "rulestring");
		vtm.setMappingFieldNames(p);
		vtm.setEntityManager(tem);
		vtm.setType("customer");
	}
	
	public void testGetTargets() {
		this.tem.list.add(this.tem.entity);
		List<TargetEntity> list = this.vtm.getTargets();
		this.assertEquals(1, list.size());

		this.tem.list.add(this.tem.entity);
		this.tem.list.add(this.tem.entity);
		list = this.vtm.getTargets();
		this.assertEquals(3, list.size());
	}
	
	public void testGetTargetEntity() {		
		TestEntity e = this.tem.entity;
		TargetEntity t = vtm.getTargetEntity(e);
		this.assertNotNull(t);
		this.assertEntityEquals(e, t);
	}
	
	private void assertEntityEquals(TestEntity e, TargetEntity t) {
		this.assertEquals(e.getIdno(), t.getId());
		this.assertEquals(e.getName(), t.getName());
		this.assertEquals(e.getRulestring(), t.getRule());
	}
	
	public class TestEntityManager implements EntityManager<TestEntity> {
		private TestEntity entity;
		private List<TestEntity> list;

		public TestEntity getById(Serializable id) {
			return entity;
		}

		public List<TestEntity> getAll() {
			return list;
		}
		
		public TestEntity create() {
			throw new UnsupportedOperationException();
		}

		public void delete(Serializable id) {
			throw new UnsupportedOperationException();
		}

		public void delete(TestEntity cg) {
			throw new UnsupportedOperationException();
		}

		public TestEntity load(Serializable id) {
			throw new UnsupportedOperationException();
		}

		public TestEntity load(TestEntity entity, Serializable id) {
			throw new UnsupportedOperationException();
		}

		public void save(TestEntity cg) {
			throw new UnsupportedOperationException();
		}

		public Page find(int pageNum, int pageSize) {
			throw new UnsupportedOperationException();
		}

		public Page find(List<? extends QueryCondition> conditions, Map<String, Object> parameters, int pageNum, int pageSize) {
			throw new UnsupportedOperationException();
		}

		public Serializable getId(TestEntity entity) {
			return entity.getIdno();
		}
		
	}

	public class TestEntity implements Entity {
		private Long idno;
		private String name;
		private String rulestring;
		public Long getIdno() {
			return idno;
		}
		public void setIdno(Long idno) {
			this.idno = idno;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getRulestring() {
			return rulestring;
		}
		public void setRulestring(String rulestring) {
			this.rulestring = rulestring;
		}
	}
}
