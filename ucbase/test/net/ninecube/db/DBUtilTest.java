package net.ninecube.db;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import net.ninecube.lang.TypedMap;
import net.ninecube.test.DBTestCase;

@SuppressWarnings("unchecked")
public class DBUtilTest extends DBTestCase {

	protected void setUp() throws Exception {
		super.setUp();
		DBManager.executeUpdate("insert into test(charField, varcharField, dateField, timestampField, amountField, intField, keyField) values(?,?,?,?,?,?,?)", 
				new Object[]{"1", "TEST DATE", new java.util.Date(), new Timestamp(System.currentTimeMillis()), "1234.5678", "9889", "1"});
	}

	public void testDataType() {
		List id = DBUtil.query("select * from TEST", null, 
				new String[]{"CHAR", "VARCHAR", "DATE", "TIMESTAMP", "AMOUNT", "INT"}, 1, Integer.MAX_VALUE);
		TypedMap kd = (TypedMap)id.get(0);
		
		this.assertTrue(kd.get("CHAR") instanceof String);
		this.assertTrue(kd.get("VARCHAR") instanceof String);
		this.assertTrue(kd.get("DATE") instanceof Date);
		this.assertTrue(kd.get("TIMESTAMP") instanceof Timestamp);
		this.assertTrue(kd.get("AMOUNT") instanceof BigDecimal);
		this.assertTrue(kd.get("INT") instanceof Integer);
	}

	public void testCount() {
		this.assertEquals(1, DBUtil.countBySql("select count(*) from TEST", null));
	}
	
	public void testBigData() {
		TypedMap kd = new TypedMap();
		kd.put("operate", "江小兵");
		kd.put("null", null);
		DBUtil.setBigData("TEST", "keyField", "bigDataField", "1", kd);
		kd = DBUtil.getBigData("TEST", "keyField", "bigDataField", "1");
		this.assertNotNull(kd);
		this.assertEquals(2, kd.size());
		this.assertEquals("江小兵", kd.getString("operate"));
		this.assertNull("", kd.getString("null"));
	}
	
	public static void main(String[] a) throws Exception {
		TypedMap kd = DBUtil.getBigData("IBS_ETAB_AUTHQUEUE", "AUTH_SEQNO", "AUTH_DATA", "120050913000000003");
		System.out.println(kd);
	}
}
