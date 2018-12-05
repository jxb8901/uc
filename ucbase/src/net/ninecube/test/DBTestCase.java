/*
 * Created on 2004-9-17 BaseTestCase.java
 *
 */
package net.ninecube.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;
import net.ninecube.db.DBManager;

 
/**
 * 
 * @author JXB
 * @version 1.0
 */
public abstract class DBTestCase extends TestCase {
	
	protected SetupDatabase setup = new SetupDatabase();
    
    public DBTestCase() {
		super();
	}

	public DBTestCase(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
    	super.setUp();
    	DBManager.setDataSource(setup);
    	dropDB();
        createDB();
        clearDB();
        initDB();
    }

	protected void dropDB() throws Exception { setup.dropDB(); }
	protected void createDB() throws Exception { setup.createDB(); }
	protected void clearDB() throws Exception { setup.clearDB(); }
	protected void initDB() throws Exception { setup.initDB(); }
    
    protected void assertDateEquals(Date d1, Date d2) {
    	if (d1 != null && d2 != null) {
	    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    	this.assertEquals(df.format(d1), df.format(d2));
    	}
    	else if (d1 != null || d2 != null) {
    		this.fail("d1="+d1+",d2="+d2);
    	}
    }
    protected void assertTimeEquals(Date d1, Date d2) {
    	if (d1 != null && d2 != null) {
	    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	this.assertEquals(df.format(d1), df.format(d2));
    	}
    	else if (d1 != null || d2 != null) {
    		this.fail("d1="+d1+",d2="+d2);
    	}
    }
}
