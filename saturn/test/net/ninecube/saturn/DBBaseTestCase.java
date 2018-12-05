package net.ninecube.saturn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.ninecube.saturn.database.DatabaseManager;
import net.ninecube.saturn.database.SchemaGenerator;
import net.ninecube.saturn.database.impl.DatabaseManagerImpl;
import net.ninecube.test.DBTestCase;

public abstract class DBBaseTestCase extends DBTestCase {
	protected DatabaseManager dbm = DatabaseManager.get();
	
	public DBBaseTestCase() {
		super();
		initSetup();
	}

	public DBBaseTestCase(String arg0) {
		super(arg0);
		initSetup();
	}

	protected void initSetup() {
		String sqltype = setup.getJdbcDriver().contains("hsqldb") ? "hsql" : "mysql";
		List sqls = SchemaGenerator.generateAll(sqltype);
		List<String> strsqls = new ArrayList<String>();
		for (Object o : sqls) strsqls.add(o.toString());
		
		super.setup.setTestSql("select * from " + getTransTableName());
		
		super.setup.setCreateSqls(strsqls);
		((DatabaseManagerImpl)dbm).setType(sqltype);
	}
	
	protected String getTransTableName() {
		return DatabaseManager.get().getTableByAlias("交易").get(0).getName();
	}
}
