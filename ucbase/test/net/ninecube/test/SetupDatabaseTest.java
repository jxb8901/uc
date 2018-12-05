/**
 * 
 * created on 2007-3-22
 */
package net.ninecube.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * 
 * @author jxb
 * 
 */
public class SetupDatabaseTest extends TestCase {

	public void testGetClearSqls() throws Exception {
		SetupDatabase setup = new SetupDatabase();
		List<String> createSqls = new ArrayList<String>();
		createSqls.add("create table Test1(id int)");
		createSqls.add("asdf  create  table Test2  (id int)");
		createSqls.add("drop table Test3 if exist; \r\ncreate table Test3  (id int)");
		createSqls.add("drop table  if exists  Test4;\r\n" + 
								"CREATE TABLE Test4 (\r\n" + 
								"custno varchar(255), \r\n" + 
								"brchno varchar(255), \r\n" + 
								"custna varchar(255),\r\n" + 
								"primary key (custno)\r\n" + 
								");\r\n");
		setup.setCreateSqls(createSqls);
		
		List<String> clearSqls = setup.getClearSqls();
		this.assertNotNull(clearSqls);
		System.out.println(clearSqls);
		this.assertEquals(4, clearSqls.size());
		this.assertEquals("delete from Test1", clearSqls.get(0));
		this.assertEquals("delete from Test2", clearSqls.get(1));
		this.assertEquals("delete from Test3", clearSqls.get(2));
		this.assertEquals("delete from Test4", clearSqls.get(3));
	}

}
