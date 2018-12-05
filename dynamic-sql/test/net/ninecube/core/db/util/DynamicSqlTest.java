package net.ninecube.core.db.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

public class DynamicSqlTest extends TestCase {
	private Map<String, Object> paras = new HashMap<String, Object>();
	
	protected void setUp() throws Exception {
		super.setUp();
		paras.put("name", "joea");
		paras.put("address", "");
		paras.put("startDate", "20070502");
		paras.put("endDate", "20070820");
		paras.put("roles", new String[]{"manager","admin","operator"});
		paras.put("birthdays", new String[]{"20000229","19830820"});
	}
	
	public void testSqlInject() {
		paras.put("name", "jack'; delete from users");
		assertEquals("where name = 'jack''; delete from users'", DynamicSql.parseSql(
				"where name=:name", paras));
		paras.put("name", "");
		assertEquals("where 1=1", DynamicSql.parseSql(
				"where name=:name", paras));
	}
	
	public void testParse() {
		assertEquals("where flag <> '1' and 'joea' = name", DynamicSql.parseSql(
				"where flag <> '1' and :name=name", paras));
		assertEquals("where flag <> '1' and 1=1", DynamicSql.parseSql(
				"where flag <> '1' and :name=name", null));

		assertEquals("where flag <> '1' and name like '%joea%'", DynamicSql.parseSql(
				"where flag <> '1' and name like :name", paras));
		assertEquals("where flag <> '1' and 1=1", DynamicSql.parseSql(
				"where flag <> '1' and name like :name", null));
	}
	
	public void testParseSql(){
		String sql = "where :name = dbname";
		String r = DynamicSql.parseSql(sql, paras);
		assertEquals("where 'joea' = dbname", r);
		sql = "where dbname=:name";
		r = DynamicSql.parseSql(sql, paras);
		assertEquals("where dbname = 'joea'", r);
		sql = "where dbname=:name1 and :{startDate} <= birthday ";
		r = DynamicSql.parseSql(sql, paras);
		assertEquals("where 1=1 and '2007-05-02' <= birthday", r);
		sql = "where dbname=:name1 and (:{startDate}<=birthday  and :{endDate} >= birthday) ";
		r = DynamicSql.parseSql(sql, paras);
		assertEquals("where 1=1 and ( '2007-05-02' <= birthday and '2007-08-20' >= birthday )", r);
		sql = "where\r\n dbname=:name1 and (:{startDate1}<=birthday  and :{endDate} >= birthday) ";
		r = DynamicSql.parseSql(sql, paras);
		assertEquals("where 1=1 and ( 1=1 and '2007-08-20' >= birthday )", r);
		sql = "where dbname=:name1 and (:{startDate1}<=birthday  ) and role IN (:roles)";
		r = DynamicSql.parseSql(sql, paras);
		assertEquals("where 1=1 and ( 1=1 ) and role IN ( 'manager' , 'admin' , 'operator' )", r);
		sql = "where dbname=:name1 and (:{startDate1}<=birthday  ) and birthday in (:{birthdays})";
		r = DynamicSql.parseSql(sql, paras);
		assertEquals("where 1=1 and ( 1=1 ) and birthday in ( '2000-02-29' , '1983-08-20' )", r);
		sql = "where dbname=:name1 and (:{startDate1}<=birthday  ) and name In (:name)";
		r = DynamicSql.parseSql(sql, paras);
		assertEquals("where 1=1 and ( 1=1 ) and name In ( 'joea' )", r);
		sql = "where dbname=:name1 and (:{startDate1}<=birthday  ) and name in (:named)";
		r = DynamicSql.parseSql(sql, paras);
		assertEquals("where 1=1 and ( 1=1 ) and 1=1", r);
		sql = "where dbname=:name1 and (:{startDate1}<=birthday  ) and (:named)";
		try{
			r = DynamicSql.parseSql(sql, paras);
			fail();
		}catch(IllegalArgumentException e){}
		sql = "where dbname=:name1 and (:{startDate1}<=birthday  ) and address = :address";
		r = DynamicSql.parseSql(sql, paras);
		assertEquals("where 1=1 and ( 1=1 ) and 1=1", r);
	}
	
	public void testParameterNames(){
		String sql = "where dbname=:name1 and ename = :name1 and (:{startDate1}<=birthday  ) and address = :address";
		List<String> names = DynamicSql.getDyanamicSql(sql).getParameterNames();
		assertNotNull(names);
		assertEquals(3, names.size());
		assertTrue(names.contains("startDate1"));
	}
	
	public void testPattern(){
		String pattern = "(>=)|(<=)|(>)|(=)|(<)|(\\()|(\\))";
		String sql ="a>b";
		String s = sql.replaceAll(pattern, " $0 ");
		assertEquals("a > b", s);
		sql = "(";
		s = sql.replaceAll(pattern, " $0 ");
		assertEquals(" ( ", s);
		
		String[] b = new String[]{};
		assertTrue(b instanceof Object[]);
	}

}
