/**
 * 2005-12-3
 */
package net.ninecube.test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.WeakHashMap;

import net.ninecube.db.DBException;
import net.ninecube.util.StringUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @deprecated 参数配置的方式需要确定
 * @author JXB
 *
 */
public class HsqlDemoDB {
	private static final Log log = LogFactory.getLog(HsqlDemoDB.class);
	private static final Map<String, HsqlDemoDB> instances = Collections.synchronizedMap(new WeakHashMap<String, HsqlDemoDB>());
	private static final String jdbcDriver = "org.hsqldb.jdbcDriver";

	private String jdbcUrl;
	private Properties config;
	
	public static HsqlDemoDB get(String database) {
		if (StringUtil.isEmpty(database)) database = ".";
		HsqlDemoDB ret = (HsqlDemoDB)instances.get(database);
		if (ret == null) {
			ret = new HsqlDemoDB(database);
			instances.put(database, ret);
		}
		return ret;
	}

	private HsqlDemoDB(String database) {
		log.info("create hsql database:"+database+",count="+instances.size());
		jdbcUrl = getJdbcUrl(database);
		initialize();
	}
	
	private String getJdbcUrl(String database) {
		return "jdbc:hsqldb:file:" + config.getProperty("upLoadFileDir") + 
				File.separator + database;
	}

	public void freeConnection(Connection con) {
		try {
			con.close();
		} catch (SQLException e) {
			log.error("close connection error:"+e.getMessage(), e);
		}
	}

	public Connection getConnection() {
		try {
			return DriverManager.getConnection(jdbcUrl, "sa", "");
		} catch (SQLException e) {
			throw new DBException("get connection error:"+e.getMessage());
		}
	}

	private void initialize() {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
		} catch (ClassNotFoundException e) {
			throw new DBException("can't load hsql driver class.", e);
		}
	}
	
	public static void main(String[] a) throws Exception {
		HsqlDemoDB db1 = HsqlDemoDB.get("test1");
		HsqlDemoDB db2 = HsqlDemoDB.get("test2");
		
		Connection con1 = db1.getConnection();
		int i = con1.createStatement().executeUpdate("insert into BRANCH(branch_id, feebyselfOpened) values('xxx',1)");
		System.out.println(""+i);
		test(con1);
		con1.close();

		Connection con2 = db2.getConnection();
		i = con2.createStatement().executeUpdate("insert into BRANCH(branch_id, feebyselfOpened) values('ttt',1)");
		System.out.println(""+i);
		test(con2);
		con2.close();
	}
	
	private static void test(Connection con) throws Exception {
		ResultSet rs = con.createStatement().executeQuery("select branch_id from branch");
		while (rs.next()) {
			System.out.println("branch_id="+rs.getString(1));
		}
	}
}
