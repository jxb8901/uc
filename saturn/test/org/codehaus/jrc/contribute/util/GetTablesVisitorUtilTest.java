/**
 * 
 * created on 2007-4-10
 */
package org.codehaus.jrc.contribute.util;

import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.codehaus.jrc.contribute.Jrc;
import org.codehaus.jrc.contribute.util.GetTablesVisitorUtil;
import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.predicate.Predicate;
import org.codehaus.jrc.relation.Relation;
import org.codehaus.jrc.relation.Relations;

/**
 * 
 * @author jxb
 * 
 */
public class GetTablesVisitorUtilTest extends TestCase {


	public void testGetImportedTables() {
		Predicate p = Jrc.getParser().parsePredicate("a.b=1");
		Set<String> list = GetTablesVisitorUtil.getImportedTables(p);
		this.assertEquals(1, list.size());
		this.assertTrue(list.contains("a"));
		
		Relation r = Relations.table("d");
		list = GetTablesVisitorUtil.getImportedTables(r);
		System.out.println(list);
		this.assertEquals(1, list.size());
		this.assertTrue(list.contains("d"));
		
		r = Jrc.getParser().parseRelation("select c.* from b where a.b=1 and a.b=2");
		list = GetTablesVisitorUtil.getImportedTables(r);
		System.out.println(list);
		this.assertEquals(3, list.size());
		this.assertTrue(list.contains("a"));
		this.assertTrue(list.contains("b"));
		this.assertTrue(list.contains("c"));
		
		p = Jrc.getParser().parsePredicate("substring(a.b, 1, 1) = 1");
		list = GetTablesVisitorUtil.getImportedTables(p);
		this.assertEquals(1, list.size());
		this.assertTrue(list.contains("a"));
	}

	public void testGetImportedTablesFromExpression() {
		Expression e = Jrc.getParser().parseExpression("t1.b/3+t2.b*5");
		Set<String> tbs = GetTablesVisitorUtil.getImportedTables(e);
		this.assertEquals(2, tbs.size());
		
		e = Jrc.getParser().parseExpression("((kns_tran.tranam/100)*5)");
		tbs = GetTablesVisitorUtil.getImportedTables(e);
		this.assertEquals(1, tbs.size());
	}
	
	public void testGetImportedTablesFromCustomExpression() {
		Expression e = Jrc.getParser().parseExpression("substring(a.b, 1, 1)");
		Set<String> list = GetTablesVisitorUtil.getImportedTables(e);
		this.assertEquals(1, list.size());
		this.assertTrue(list.contains("a"));
	}
	
	
}
