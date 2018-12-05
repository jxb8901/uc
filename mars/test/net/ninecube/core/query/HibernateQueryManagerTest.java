/*
 * Created on 2004-7-17
 *
 */
package net.ninecube.core.query;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import net.ninecube.core.AbstractEntityTestCase;
import net.ninecube.core.Bar;
import net.ninecube.core.Foo;
import net.ninecube.core.Tao;

import org.hibernate.Session;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

/**
 * @author jxb
 */
public class HibernateQueryManagerTest extends AbstractEntityTestCase {
	private QueryManager query;
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	private HibernateQueryManagerFixture fixture;
	
	@Override
	public void runBare() throws Throwable {
		setUp();
		try {
			setupDB();
			fixture = new HibernateQueryManagerFixture(this.query, Foo.class);
			runTest();
		}
		finally {
			tearDown();
		}
	}

	private void setupDB() throws Exception
	{
		Bar bar = new Bar(1L, "bar bar bar");
		getSession().save(bar);
		Bar bar1 = new Bar(2L, "bar");
		getSession().save(bar1);
		
		Foo foo = new Foo(1L, "foo1 abc", dateFormat.parse("2003/02/12"), new BigDecimal("0"));
		foo.setProperty("email", "foo1@onesun.com");
		foo.setProperty("address", "");
		foo.setBar(bar);
		foo.setTao(new Tao('T', "Tom", "David"));
		getSession().save(foo);
		
		foo = new Foo(2L, "foo2 jxb", dateFormat.parse("2004/12/12"), new BigDecimal("1.234"));
		foo.setProperty("email", "foo2@onesun.com");
		foo.setProperty("address", "foo shenzhen");
		foo.setBar(bar1);
		foo.setTao(new Tao('E', "Smith", "Sally"));
		getSession().save(foo);
	}

	public void testFindByOneField()
	{
		//test "="
		Map<String, String> conditionValues = new HashMap<String, String>();
		Page ret = fixture.reset().add("name", "=", "foo1 abc").find();
		assertNotNull(ret);
		assertEquals(1, ret.getRecords().size());

		ret = fixture.reset().add("name", "=", "abcdefg").find();
		assertEquals(0, ret.getRecords().size());
		//test ">"
		ret = fixture.reset().add("name", ">", "foo1").find();
		assertEquals(2, ret.getRecords().size());
		
		ret = fixture.reset().add("name", ">", "foo3").find();
		assertEquals(0, ret.getRecords().size());
		//test "like"
		ret = fixture.reset().add("name", "%", "foo").find();
		assertEquals(2, ret.getRecords().size());
		
		ret = fixture.reset().add("name", "%", "foo3").find();
		assertEquals(0, ret.getRecords().size());
		
		ret = fixture.reset().add("name", "%", "aaaaaa").find();
		assertEquals(0, ret.getRecords().size());
	}

	//test wrong field that is not existed in persistant class
	public void testWrongField() throws ParseException {
		fixture.reset().add("name1", "%", "abcdefg");
		try
		{
			fixture.find(); // 会抛找不字段的异常
			fail();
		} catch (RuntimeException e){e.printStackTrace();}
	}
	
	//test multi fields
	public void testFindByMultiFields() throws ParseException
	{
		fixture.add("name", "like", "foo");
		fixture.add("dateStart", ">=", "date", dateFormat.parse("2004/1/1"));
		fixture.add("dateEnd", "<=", "date", dateFormat.parse("2004/12/31"));
		fixture.add("idStart", ">", "id", 0);
		fixture.add("idEnd", "<", "id", 10);
		fixture.add("amountStart", ">=", "amount", 1.2);
		fixture.add("amountEnd", "<", "amount", 2.0);
		Page ret = fixture.find();
		assertNotNull(ret);
		assertEquals(1, ret.getRecords().size());
	}
	
	//test empty fields
	public void testEmptyConidtion()
	{
		//相当于find all
		Page ret = fixture.find();
		assertNotNull(ret);
		assertEquals(2, ret.getRecords().size());
		
		fixture.conditions = null;
		fixture.parameters = null;
		ret = fixture.find();
		assertNotNull(ret);
		assertEquals(2, ret.getRecords().size());
	}
	
	//test field type (Date, BigDecimal, Long, Collection?)
	public void testFieldType() throws ParseException
	{
		//test Date
		Page ret = fixture.reset().add("date", "=", dateFormat.parse("2004/12/12")).find();
		assertNotNull(ret);
		assertEquals(1, ret.getRecords().size());
		
		fixture.reset().add("date", "%", dateFormat.parse("2004/12/12"));
		try
		{
			fixture.find();
			System.err.println("========"+ret.getRecords().size());
			//fail();
		} 
		catch (RuntimeException success){}
		//test Long
		ret = fixture.reset().add("id", "=", 1).find();
		assertNotNull(ret);
		assertEquals(1, ret.getRecords().size());
		//test BigDecimal
		ret = fixture.reset().add("amount", "=", new BigDecimal("1.234")).find();
		assertNotNull(ret);
		assertEquals(1, ret.getRecords().size());
		
		ret = fixture.reset().add("amount", "=", new BigDecimal("1.2345")).find();
		assertNotNull(ret);
		assertEquals(0, ret.getRecords().size());
	}
	
	//test type convert
	public void testTypeConvert()
	{
		//test String[]->String
		Page ret = fixture.reset().add("name", "=", new String[]{"foo1 abc"}).find();
		assertNotNull(ret);
		assertEquals(1, ret.getRecords().size());
		//test String[]->Date
		ret = fixture.reset().add("date", "=", new String[]{"20041212"}).find();
		assertNotNull(ret);
		assertEquals(1, ret.getRecords().size());
		//test String[]->Long
		ret = fixture.reset().add("id", "=", new String[]{"1"}).find();
		assertNotNull(ret);
		assertEquals(1, ret.getRecords().size());
		//test BigDecimal->BigDecimal
		ret = fixture.reset().add("amount", "=", new BigDecimal("1.234")).find();
		assertNotNull(ret);
		assertEquals(1, ret.getRecords().size());
		//test String->BigDecimal
		ret = fixture.reset().add("amount", "=", "1.234").find();
		assertNotNull(ret);
		assertEquals(1, ret.getRecords().size());
		//test String[]->BigDecimal
		ret = fixture.reset().add("amount", "=", new String[]{"1.234"}).find();
		assertNotNull(ret);
		assertEquals(1, ret.getRecords().size());
	}
	
	//test record count
	public void testCount()
	{
		//相当于find all
		Page ret = fixture.reset().find();
		assertNotNull(ret);
		assertEquals(2, ret.getRecords().size());
		assertEquals(2, ret.getRecordCount());
		
		ret = fixture.reset().find(1, 1);
		assertNotNull(ret);
		assertEquals(1, ret.getRecords().size());
		assertEquals(2, ret.getRecordCount());
		
		ret = fixture.reset().find(2, 1);
		assertNotNull(ret);
		assertEquals(1, ret.getRecords().size());
		assertEquals(2, ret.getRecordCount());
	}
	
	//test order by
	public void testOrder()
	{
		Page ret = fixture.reset().add("name", "%", "foo").add("name", "asc").find();
		assertNotNull(ret);
		assertEquals(2, ret.getRecords().size());
		assertEquals("foo1 abc", ((Foo)ret.getRecords().get(0)).getName());
		//test desc order 
		ret = fixture.reset().add("name", "%", "foo").add("name", "desc").find();
		assertNotNull(ret);
		assertEquals(2, ret.getRecords().size());
		assertEquals("foo1 abc", ((Foo)ret.getRecords().get(1)).getName());
	}
	
	//test Map field
	public void testMapField()
	{
		Page ret = fixture.reset().add("properties['email']", "%", "foo1")
			.add("properties['address']", "%", "").find();
		assertNotNull(ret);
		assertEquals(1, ret.getRecords().size());
		
		ret = fixture.reset().add("properties['email']", "%", "foo1")
			.add("properties['not found']", "%", "xxx").find();
		assertNotNull(ret);
		assertEquals(0, ret.getRecords().size());
	}
	
	//test property that like: bar.id, bar.name, ...
	public void testRelatedClassesProperty()
	{
		Page ret = fixture.reset().add("id", "=", "1").find();
		assertNotNull(ret);
		assertEquals(1, ret.getRecords().size());
		
		ret = fixture.reset().add("bar.id", "=", "1").find();
		assertNotNull(ret);
		assertEquals(1, ret.getRecords().size());
		
		ret = fixture.reset().add("bar.name", "%", "bar bar ").find();
		assertNotNull(ret);
		assertEquals(1, ret.getRecords().size());
		
		ret = fixture.reset().add("tao.first", "%", "mit").find();
		assertNotNull(ret);
		assertEquals(1, ret.getRecords().size());
	}
	
	public void setQueryManager(QueryManager queryManager) {
		this.query = queryManager;
	}
	
	private Session getSession() {
		return SessionFactoryUtils.getSession(sessionFactory, false);
	}
}
