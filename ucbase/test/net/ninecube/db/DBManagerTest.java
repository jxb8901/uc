package net.ninecube.db;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import net.ninecube.lang.TypedMap;
import net.ninecube.test.DBTestCase;
import net.ninecube.util.DataTypeUtil;

@SuppressWarnings("unchecked")
public class DBManagerTest extends DBTestCase {

	protected void setUp() throws Exception {
		super.setUp();
		super.setup.clearDB();
	}
	
	public void testDataType() throws Exception {
		DataTypeEntity en = new DataTypeEntity();
		en.charField = false;
		en.varcharField = "10001";
		en.dateField = new Date();
		en.timestampField = new Timestamp(System.currentTimeMillis());
		en.intField = 1234;
		en.amountField = new BigDecimal("1234.56");
		en.keyField = "1";
		
		dataTypeDB.save(en);
		
		DataTypeEntity en1 = (DataTypeEntity) dataTypeDB.getById(en.keyField);
		this.assertEquals(en.charField, en1.charField);
		this.assertEquals(en.varcharField, en1.varcharField);
		this.assertDateEquals(en.dateField, en1.dateField);
		//@ TODO：Timestamp处理，hsql中比较失败，DB2中比较成功
		this.assertDateEquals(en.timestampField, en1.timestampField);
		this.assertEquals(en.intField, en1.intField);
		this.assertEquals(en.amountField.doubleValue(), en1.amountField.doubleValue(), 0.001);
		this.assertEquals(en.keyField, en1.keyField);
		
		en.bigDataField = new TypedMap();
		en.bigDataField.put("TEST", "中文问题");
		en.bigDataField.put("中国", "TEST");
		DBUtil.setBigData("TEST", "keyField", "bigDataField", en.keyField, en.bigDataField);
		
		TypedMap kd = DBUtil.getBigData("TEST", "keyField", "bigDataField", en.keyField);
		this.assertNotNull(kd);
		this.assertEquals(2, kd.size());
		this.assertEquals("中文问题", kd.get("TEST"));
		this.assertEquals("TEST", kd.get("中国"));
	}
	
	public void testTransaction() throws Exception {
		Branch b = createBranch();
		this.assertEquals(1, DBUtil.countBySql("select count(*) from branch", null));
		
		DBManager.beginTransction();
		try {
			db.executeUpdate("delete from branch", null);
			this.assertEquals(0, DBUtil.countBySql("select count(*) from branch", null));
			db.executeUpdate("insert @#test abd", null);
			
			fail();
		} catch (RuntimeException e) {
			DBManager.rollbackTransaction();
			this.assertEquals(1, DBUtil.countBySql("select count(*) from branch", null));
		} finally {
			DBManager.endTransaction();
		}
	}

	/*
	 * Test method for 'com.csii.ebank.db.NewDatabase.insert(Object)'
	 */
	public void testCRUD() throws Exception {
		Branch b = createBranch();
		
		Branch b1 = (Branch)db.getById("119");
		this.assertNotNull(b1);
		this.assertEquals(b.branchId, b1.branchId);
		this.assertEquals(b.branchDesc, b1.branchDesc);
		this.assertEquals(b.feebyselfOpened, b1.feebyselfOpened);
		this.assertDateEquals(b.testDate, b1.testDate);
		
		b1.branchDesc = "长沙商行";
		db.update(b1);
		
		Branch b2 = (Branch)db.getByWhere("where branch_id=?", new Object[]{"119"});
		this.assertNotNull(b2);
		this.assertEquals(b1.branchDesc, b2.branchDesc);
		
		db.delete(b2);
		
		Branch b3 = (Branch)db.getByWhere("where branch_id=?", new Object[]{"119"});
		this.assertNull(b3);
		Branch b4 = (Branch)db.getById("119");
		this.assertNull(b4);
	}
	
	private Branch createBranch() {
		Branch b = new Branch();
		b.branchId = "119";
		b.branchDesc = "深圳分行";
		b.feebyselfOpened = true;
		b.testDate = new Date();
		
		db.insert(b);
		
		return b;
	}
	
	private class Branch extends AbstractEntity{
		private String branchId;
		private String branchDesc;
		private boolean feebyselfOpened;
		private Date testDate;
	}
	
	private DBManager db = new DBManager() {

		protected String getTableName() {
			return "BRANCH";
		}

		protected String[] getFields() {
			return new String[]{"branch_desc", "feebyselfOpened", "test_date"};
		}

		protected Object[] getFieldValues(Entity o) {
			Branch b = (Branch)o;
			return new Object[]{
				b.branchDesc,
				Boolean.valueOf(b.feebyselfOpened),
				b.testDate
			};
		}

		protected String[] getKeyFields() {
			return new String[]{"branch_id"};
		}

		protected Object[] getKeyValues(Entity o) {
			return new Object[]{((Branch)o).branchId};
		}

		protected Entity fromArray(Object[] values) {
			Branch b = new Branch();
			b.branchDesc = (String)values[0];
			b.feebyselfOpened = DataTypeUtil.obj2boolean(values[1]);
			b.testDate = DataTypeUtil.obj2Date(values[2]);
			b.branchId = (String)values[3];
			return b;
		}
	};
	
	private class DataTypeEntity extends AbstractEntity{
		private boolean charField;
		private String varcharField;
		private Date dateField;
		private Timestamp timestampField;
		private int intField;
		private BigDecimal amountField;
		
		private TypedMap bigDataField;
		
		private String keyField;
	}
	
	private DBManager dataTypeDB = new DBManager() {

		protected String getTableName() {
			return "TEST";
		}

		protected String[] getFields() {
			return new String[]{"charField", "varcharField", "dateField", "timestampField", "intField", "amountField"};
		}

		protected Object[] getFieldValues(Entity o) {
			DataTypeEntity b = (DataTypeEntity)o;
			return new Object[]{
				Boolean.valueOf(b.charField),
				b.varcharField,
				b.dateField, //DBManager.date2Str(b.dateField),
				b.timestampField, //DBManager.timestamp2Str(b.timestampField),
				"" + b.intField,
				b.amountField
			};
		}

		protected String[] getKeyFields() {
			return new String[]{"keyField"};
		}

		protected Object[] getKeyValues(Entity o) {
			return new Object[]{((DataTypeEntity)o).keyField};
		}

		protected Entity fromArray(Object[] values) {
			DataTypeEntity b = new DataTypeEntity();
			b.charField = DataTypeUtil.obj2boolean(values[0]);
			b.varcharField = (String)(values[1]);
			b.dateField = DataTypeUtil.obj2Date(values[2]);
			b.timestampField = DataTypeUtil.obj2Timestamp(values[3]);
			b.intField = DataTypeUtil.obj2Int(values[4], -1);
			b.amountField = DataTypeUtil.obj2BigDecimal(values[5]);
			b.keyField = (String)(values[6]);
			return b;
		}
	};
}
