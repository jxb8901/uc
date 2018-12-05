/**
 * 
 * created on 2007-1-14
 */
package net.ninecube.core.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author jxb
 * 
 */
public class HibernateQueryManagerFixture {
	private QueryManager query;
	private Class entityClass;
	public List<QueryCondition> conditions = new ArrayList<QueryCondition>();
	public Map<String, Object> parameters = new HashMap<String, Object>();
	
	public HibernateQueryManagerFixture(QueryManager query, Class entityClass) {
		this.query = query;
		this.entityClass = entityClass;
	}
	
	public HibernateQueryManagerFixture reset() {
		conditions = new ArrayList<QueryCondition>();
		parameters = new HashMap<String, Object>();
		return this;
	}
	
	public Page find() {
		return find(this.entityClass, 1, 10);
	}
	
	public Page find(Class entityClass) {
		return find(entityClass, 1, 10);
	}
	
	public Page find(int pageNum, int pageSize) {
		return find(this.entityClass, pageNum, pageSize);
	}
	
	public Page find(Class entityClass, int pageNum, int pageSize) {
		return query.getQueryObject(entityClass, conditions).find(parameters, pageNum, pageSize);
	}
	
	public HibernateQueryManagerFixture add(String name, String op) {
		this.conditions.add(new TestQueryCondition(name, op, name));
		return this;
	}
	
	public HibernateQueryManagerFixture add(String name, String op, Object value) {
		return this.add(name, op, name, value);
	}
	
	public HibernateQueryManagerFixture add(String name, String op, String entityname, Object value) {
		this.conditions.add(new TestQueryCondition(name, op, entityname));
		this.parameters.put(name, value);
		return this;
	}
	
	private static class TestQueryCondition implements QueryCondition {
		private String name;
		private Operator op;
		private String entityname;
		private TestQueryCondition(String name, String op, String entityname) {
			this.name = name;
			this.op = Operator.from(op);
			this.entityname = entityname;
		}
		public String getEntityName() {
			return entityname;
		}
		public String getName() {
			return name;
		}
		public Operator getOperator() {
			return op;
		}
		public String toString() {
			return entityname + ":" + op.toSql() +":" + name;
		}
	}
}
