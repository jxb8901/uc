/**
 * 
 * created on 2007-1-15
 */
package net.ninecube.core.config.impl;

import java.util.List;

import junit.framework.TestCase;
import net.ninecube.core.config.FieldConf;
import net.ninecube.core.config.field.QueryFieldsConfImpl;
import net.ninecube.core.query.Operator;

/**
 * 
 * @author jxb
 * 
 */
public class QueryFieldConfImplTest extends TestCase {
	
	private ModelConfImpl model;
	
	protected void setUp() {
		model = new ModelConfImpl(null);
		FieldConfImpl f = new FieldConfImpl();
		f.setName("creationDate");
		model.addField(f);
		
		f = new FieldConfImpl();
		f.setName("name");
		model.addField(f);
		
		f = new FieldConfImpl();
		f.setName("creator");
		model.addField(f);
	}

	public void testResolveBetween() {
		QueryFieldsConfImpl q = new QueryFieldsConfImpl();
		q.setName("creationDate");
		q.setOperator(Operator.between);
		q.setReq(true);
		
		List<FieldConf> m = q.resolve(model); 
		this.assertEquals(2, m.size());
		this.assertEquals(Operator.ge, m.get(0).getOperator());
		this.assertEquals(Operator.le, m.get(1).getOperator());
		this.assertEquals("creationDateStart", m.get(0).getName());
		this.assertEquals("creationDateEnd", m.get(1).getName());
		this.assertEquals("creationDate", m.get(0).getEntityName());
		this.assertEquals("creationDate", m.get(1).getEntityName());
	}
	
	public void testResolve() {
		QueryFieldsConfImpl q = new QueryFieldsConfImpl();
		q.setName("name");
		q.setOperator(Operator.like);
		q.setReq(true);

		List<FieldConf> m = q.resolve(model); 
		this.assertEquals(1, m.size());
		this.assertEquals(Operator.like, m.get(0).getOperator());
		this.assertEquals("nameQ", m.get(0).getName());
		this.assertEquals("name", m.get(0).getEntityName());
	}

}
