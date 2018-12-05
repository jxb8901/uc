/**
 * 2005-5-13
 */
package net.ninecube.db;

import java.util.List;
import java.util.Map;

import net.ninecube.lang.TypedMap;
import net.ninecube.test.DBTestCase;

/**
 * 
 * @author JXB
 */
@SuppressWarnings("unchecked")
public class MapDBManagerTest extends DBTestCase {
    
    private MapDBManager db = new MapDBManager(
            "BRANCH",
            new String[]{"branch_desc", "feeAgentIP", "feeAgentPort"},
            new String[]{"branch_id", "feebyselfOpened"}
            );

    public void testCountAndDelete() throws Exception {
        this.assertEquals(3, db.count(null, null));
        TypedMap branch = db.getById(new Object[]{"102", "1"});
        db.delete(branch);
        this.assertEquals(2, db.count(null, null));
    }

    public void testGetById() throws Exception {
        Map branch = db.getById(new Object[]{"102", "1"});
        this.assertNotNull(branch);
        this.assertEquals("102", branch.get("branch_id"));
        this.assertEquals("", branch.get("branch_desc"));
        
        this.assertNull(db.getById(new Object[]{"102", "0"}));
    }

    public void testInsert() throws Exception {
        TypedMap branch = new TypedMap();
        branch.put("branch_id", "110");
        branch.put("feebyselfOpened", "0");
        branch.put("branch_desc", "深圳分行");
        branch.put("feeAgentIP", "10.2.101.102");
        branch.put("feeAgentPort", "80");
        db.insert(branch);
        
        this.assertEquals(4, db.count(null, null));
        this.assertEquals(1, db.count("where branch_id=?", new Object[]{"110"}));
        
        branch = db.getByWhere("where branch_id=?", new Object[]{"110"});
        this.assertNotNull(branch);
        this.assertEquals("110", branch.get("branch_id"));
        this.assertEquals("0", branch.get("feebyselfOpened"));
        this.assertEquals("深圳分行", branch.get("branch_desc"));
        this.assertEquals("10.2.101.102", branch.get("feeAgentIP"));
        this.assertEquals("80", branch.get("feeAgentPort"));
    }

    public void testQuery() throws Exception {
        List list = db.query("where feebyselfOpened=?", new Object[]{"1"}, 1, 2);
        this.assertEquals(2, list.size());
        this.assertTrue(list.get(0) instanceof Map);
    }

    public void testUpdate() throws Exception {
    	TypedMap branch = db.getById(new Object[]{"102", "1"});
        branch.put("branch_desc", "深圳分行");
        db.update(branch);
        
        branch = db.getById(new Object[]{"102", "1"});
        this.assertNotNull(branch);
        this.assertEquals("102", branch.get("branch_id"));
        this.assertEquals("深圳分行", branch.get("branch_desc"));
        
        branch = new TypedMap();
        branch.put("branch_id", "xxx");
        branch.put("feebyselfOpened", "1");
        try {
            db.update(branch);
            this.fail("没有更新任何非主键的列，导致数据库操作失败");
        } catch (DBException e) { }
        
        branch = new TypedMap();
        branch.put("branch_id", "102");
        branch.put("feebyselfOpened", "1");
        branch.put("feeAgentIP", "10.1.1.1");
        db.update(branch);
        branch = db.getById(new Object[]{"102", "1"});
        this.assertNotNull(branch);
        this.assertEquals("102", branch.get("branch_id"));
        this.assertNotNull(branch.get("branch_desc"));
        this.assertEquals("深圳分行", branch.get("branch_desc"));
        this.assertEquals("10.1.1.1", branch.get("feeAgentIP"));
    }

}
