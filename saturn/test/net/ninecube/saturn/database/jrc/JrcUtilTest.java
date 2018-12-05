/**
 * 
 * created on 2007-2-8
 */
package net.ninecube.saturn.database.jrc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.ninecube.saturn.database.AbstractDatabaseTestCase;
import net.ninecube.saturn.database.DatabaseException;
import net.ninecube.saturn.database.Table;
import net.ninecube.saturn.database.impl.AssociationImpl;
import net.ninecube.saturn.database.impl.ColumnImpl;
import net.ninecube.saturn.database.impl.TableImpl;

import org.apache.log4j.Logger;
import org.codehaus.jrc.contribute.Jrc;
import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.expression.Expressions;
import org.codehaus.jrc.predicate.Predicate;
import org.codehaus.jrc.relation.Relation;
import org.codehaus.jrc.relation.Relations;

/**
 * 
 * @author jxb
 * 
 */
public class JrcUtilTest extends AbstractDatabaseTestCase {
	private static final Logger log = Logger.getLogger(JrcUtilTest.class);
	
	public void testConvertColumn() {
		Expression s = Jrc.getParser().parseExpression("substring(col,    1,    1)");
		this.assertEquals("substring(col,1,1)", s.toString());
		TableImpl table = createTable("a");
		table.addColumn(createColumn(table, "col"));
		Expression s1 = JrcUtil.convertColumn(s, table);
		this.assertEquals("substring(a.col,1,1)", s1.toString());
	}
	
	public void testGetTablesInFromClause() {
		Predicate p = Jrc.getParser().parsePredicate("c.id in (select b.id from b inner join a on a.id=b.id where a.t='1')");
		Set<String> tables = JrcUtil.getTablesInFromClause(p);
		this.assertNotNull(tables);
		this.assertEquals(2, tables.size());
		this.assertTrue(tables.contains("a"));
		this.assertTrue(tables.contains("b"));
	}
	
	// 测试单一表关联及重复表关联
	public void testJoinTables() {
		TableImpl a = createTable("a");
		List<TableImpl> ts = new ArrayList<TableImpl>();
		ts.add(a);
		Relation r = JrcUtil.joinTables(ts);
		System.out.println(r);
		this.assertEquals("a", r.toString());
		ts.add(a);
		r = JrcUtil.joinTables(ts);
		System.out.println(r);
		this.assertEquals("a", r.toString());
	}
	
	// 测试多表关联
	public void testJoinTables1() {
		String expect = "a inner join b on a.id=b.id inner join e on e.id=a.id " + 
			"inner join d on e.id=d.id inner join c on c.id=d.id";
		String rst = ""+JrcUtil.joinTables(createTables());
		log.debug("result : " + rst);
		assertEquals(expect, rst);
	}
	
	// 测试双向双表关联
	public void testJoinTables21() {
		TableImpl a = createTable("a");
		TableImpl b = createTable("b");
		a.getAssociations().add(createAssociation(a, b));
		b.getAssociations().add(createAssociation(b, a));
		
		List<TableImpl> ts = new ArrayList<TableImpl>();
		ts.add(a);
		ts.add(b);
		
		Relation r = JrcUtil.joinTables(ts);
		System.out.println(r);
		this.assertEquals("a inner join b on (b.id=a.id and a.id=b.id)", r.toString());
	}
	
	// 测试单向双表关联
	public void testJoinTables2() {
		TableImpl a = createTable("a");
		TableImpl b = createTable("b");
		TableImpl c = createTable("c");
		a.getAssociations().add(createAssociation(a, b));
		b.getAssociations().add(createAssociation(b, c));
		
		List<TableImpl> ts = new ArrayList<TableImpl>();
		ts.add(a);ts.add(a);ts.add(a);
		ts.add(b);
		
		Relation r = JrcUtil.joinTables(ts);
		System.out.println(r);
		this.assertEquals("a inner join b on a.id=b.id", r.toString());
	}
	
	// 测试三表循环关联
	public void testJoinTables3() {
		TableImpl a = createTable("a");
		TableImpl b = createTable("b");
		TableImpl c = createTable("c");
		a.getAssociations().add(createAssociation(a, b));
		b.getAssociations().add(createAssociation(b, c));
		c.getAssociations().add(createAssociation(c, a));
		
		List<TableImpl> ts = new ArrayList<TableImpl>();
		ts.add(a);
		ts.add(b);
		ts.add(c);
		
		Relation r = JrcUtil.joinTables(ts);
		System.out.println(r);
		this.assertEquals("a inner join b on a.id=b.id inner join c " + 
				"on (c.id=a.id and b.id=c.id)", r.toString());
	}
	
	// 测试四表循环关联
	public void testJoinTables4() {
		TableImpl a = createTable("a");
		TableImpl b = createTable("b");
		TableImpl c = createTable("c");
		TableImpl d = createTable("d");
		a.getAssociations().add(createAssociation(a, b));
		b.getAssociations().add(createAssociation(b, c));
		c.getAssociations().add(createAssociation(c, d));
		d.getAssociations().add(createAssociation(d, a));
		d.getAssociations().add(createAssociation(d, b));
		
		List<TableImpl> ts = new ArrayList<TableImpl>();
		ts.add(a);
		ts.add(b);
		ts.add(c);
		ts.add(d);
		
		Relation r = JrcUtil.joinTables(ts);
		System.out.println(r);
		this.assertEquals("a inner join b on a.id=b.id inner join d on " + 
				"(d.id=a.id and d.id=b.id) inner join c on (c.id=d.id and b.id=c.id)", r.toString());
	}
	
	// 测试无法全部关联
	public void testJoinTables5() {
		TableImpl a = createTable("a");
		TableImpl b = createTable("b");
		TableImpl c = createTable("c");
		TableImpl d = createTable("d");
		a.getAssociations().add(createAssociation(a, b));
		c.getAssociations().add(createAssociation(c, d));
		
		List<TableImpl> ts = new ArrayList<TableImpl>();
		ts.add(a);
		ts.add(b);
		ts.add(c);
		ts.add(d);
		
		Relation r;
		try {
			r = JrcUtil.joinTables(ts);
			this.fail("应该失败");
		} catch (DatabaseException ignore) {
		}
	}
	
	public void testProject() {
		Relation r = Relations.table("a");
		r = r.project("a.b, a.c");
		this.assertEquals("select a.b,a.c from a", r.toString());
		
		// project
		Expression e = Expressions.literal("3");
		r = RelationsUtil.mergeProject(e, r);
		this.assertEquals("select a.b,a.c,3 from a", r.toString());

		// top
		r = r.top(10);
		this.assertEquals("select a.b,a.c,3 from a limit 10", r.toString());
		e = Expressions.literal("10");
		Expression[] exprs = new Expression[]{Expressions.column("a", "d"), e};
		r = RelationsUtil.mergeProject(exprs, r);
		System.out.println(r);
		this.assertEquals("select a.b,a.c,3,a.d,10 from a limit 10", r.toString());
		
		// alias
		r = Relations.table("a");
		r = r.project("a.b, a.c");
		r = r.alias("alias");
		System.out.println(r);
		this.assertEquals("select a.b,a.c from a AS alias", r.toString());
		r = RelationsUtil.mergeProject(exprs, r);
		System.out.println(r);
		this.assertEquals("select a.b,a.c,a.d,10 from a AS alias", r.toString());
		
		// distinct
		r = Relations.table("a");
		r = r.project("a.b, a.c");
		r = r.distinct();
		System.out.println(r);
		this.assertEquals("distinct select a.b,a.c from a", r.toString());
		r = RelationsUtil.mergeProject(exprs, r);
		System.out.println(r);
		this.assertEquals("distinct select a.b,a.c,a.d,10 from a", r.toString());
		
		// group by
		r = Relations.table("a");
		r = r.groupBy("a.c, count(*)", "a.c");
		System.out.println(r);
		this.assertEquals("select a.c,count (*) from a group by a.c", r.toString());
		r = RelationsUtil.mergeProject(exprs, r);
		System.out.println(r);
		this.assertEquals("select a.c,count (*),a.d,10 from a group by a.c", r.toString());
	}

	public void test() {
		Table table = fixture.trans;
		Expression e1 = Jrc.getParser().parseExpression("substring(transdate, 4,2)");
		Expression e2 = JrcUtil.convertColumn(e1, table);
		assertEquals("substring(trans.transdate,4,2)", e2.toString());
	}
	
	private List<Table> createTables() {
		TableImpl a = createTable("a");
		TableImpl b = createTable("b");
		TableImpl c = createTable("c");
		TableImpl d = createTable("d");
		TableImpl e = createTable("e");
		
		a.getAssociations().add(createAssociation(a, b));
		c.getAssociations().add(createAssociation(c, d));
		e.getAssociations().add(createAssociation(e, a));
		e.getAssociations().add(createAssociation(e, d));
		
		List<Table> ret = new ArrayList<Table>();
		ret.add(a);
		ret.add(b);
		ret.add(c);
		ret.add(d);
		ret.add(e);
		return ret;
	}
	
	private List<Table> createTables2() {
		TableImpl a = createTable("a");
		TableImpl b = createTable("b");
		TableImpl c = createTable("c");
		TableImpl d = createTable("d");
		TableImpl e = createTable("e");
		
		a.getAssociations().add(createAssociation(a, b));
		b.getAssociations().add(createAssociation(b, a));
		c.getAssociations().add(createAssociation(c, d));
		d.getAssociations().add(createAssociation(d, c));
		e.getAssociations().add(createAssociation(e, a));
		a.getAssociations().add(createAssociation(a, e));
		e.getAssociations().add(createAssociation(e, d));
		d.getAssociations().add(createAssociation(d, e));
		e.getAssociations().add(createAssociation(e, c));
		c.getAssociations().add(createAssociation(c, e));
		
		List<Table> ret = new ArrayList<Table>();
		ret.add(a);
		ret.add(b);
		ret.add(c);
		ret.add(d);
		ret.add(e);
		return ret;
	}
	
	private AssociationImpl createAssociation(TableImpl a, TableImpl b) {
		return new AssociationImpl(createColumn(a, "id"), createColumn(b, "id"));
	}
	
	private ColumnImpl createColumn(TableImpl t, String name) {
		ColumnImpl ret = new ColumnImpl();
		ret.setName(name);
		ret.setOwner(t);
		return ret;
	}
	
	private TableImpl createTable(String name) {
		TableImpl ret = new TableImpl();
		ret.setName(name);
		return ret;
	}
}
