package net.ninecube.saturn.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;
import net.ninecube.saturn.database.impl.DatabaseManagerImpl;

public class DatabaseManagerTest extends TestCase {

	private DatabaseManagerImpl dm;
	
	protected void setUp() throws Exception {
		dm = new DatabaseManagerImpl("database.xml");
		dm.reload();
	}
	
	public void testCompositeTable() {
		this.assertNotNull(dm.getTableByAlias("客户"));
	}
	
	public void testView() {
		this.assertNotNull(dm.getTableByAlias("POS交易"));
	}
	
	public void testGetSQLCode(){
		
		Table t = dm.getTableByAlias("交易").get(0);
		Column col = t.getTableColumnByAlias("客户号");
		this.assertEquals("SUBSTRING(kns_tran.acctno,1,10)", col.getSqlCode().toString());
		this.assertEquals("kns_tran.custno", col.getSqlValue(col).toString());
		
		Column c = dm.getColumnByAlias("客户.客户类型");
		this.assertNotNull(c);
		this.assertEquals("SUBSTRING(knc_cust.custno,1,1)", c.getSqlCode().toString());
		this.assertEquals("1", c.getSqlValue("个人").toString());
	}
	
	public void testGetTableByAlias() {
		Table t = dm.getTableByAlias("kns_tran").get(0);
		this.assertNotNull(t);
		t = dm.getTableByAlias("客户").get(0);
		this.assertNotNull(t);
		Column col = t.getTableColumnByAlias("客户号");
		this.assertNotNull(col);
		this.assertEquals("custno", col.getName());
		this.assertEquals("knc_cust", col.getOwner().getName());
	}
	
	public void testAssociation() {
		Table t = dm.getTableByAlias("交易").get(0);
		this.assertEquals("kns_tran", t.getName());
		Set<? extends Association> as = t.getAssociations();
		this.assertNotNull(as);
		this.assertEquals(2, as.size()); // 自己设置一个关联，积分流水和计算表设置了两个关联
	}
	
	public void testGetNamedSql() {
		dm = new DatabaseManagerImpl() {
			private Map<String, String> globle = new HashMap<String, String>();
			private Map<String, String> mysql = new HashMap<String, String>();
			{
				globle.put("select1", "top 1");
				globle.put("selectall", "*");
				mysql.put("select1", "limit 1");
			}
			protected String getProperty(String file, String name) {
				if (file.endsWith("mysql")) return mysql.get(name);
				return globle.get(name);
			}
		};
		dm.setType("test");
		this.assertNull(dm.getNamedSql("xx"));
		this.assertEquals("top 1", dm.getNamedSql("select1"));
		this.assertEquals("*", dm.getNamedSql("selectall"));
		dm.setType("mysql");
		this.assertEquals("limit 1", dm.getNamedSql("select1"));
		this.assertEquals("*", dm.getNamedSql("selectall"));
	}
}
