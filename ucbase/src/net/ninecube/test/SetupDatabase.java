/*
 * Created on 2004-10-15 SetupDatabase.java
 *
 */
package net.ninecube.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import net.ninecube.util.ArrayUtil;
import net.ninecube.util.StringUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author JXB
 * @version 1.0
 */
public class SetupDatabase implements DataSource {
	private static final Log log = LogFactory.getLog(SetupDatabase.class);

	private String jdbcDriver;

	private String jdbcUrl;

	private String jdbcUserName;

	private String jdbcPassword;

	private String dropTableTemplate;

	private String testSql;

	private String createSqlFile;

	private String initSqlFile;

	private String clearSqlFile;

	private List<String> createSqls;

	private List<String> initSqls;

	private List<String> clearSqls;

	public SetupDatabase() {
		this("testdb");
	}

	public SetupDatabase(String propertiesFile) {
		ResourceBundle rb = ResourceBundle.getBundle(propertiesFile);

		this.jdbcDriver = rb.getString("jdbc.driver");
		this.jdbcUrl = rb.getString("jdbc.url");
		this.jdbcUserName = rb.getString("jdbc.username");
		this.jdbcPassword = rb.getString("jdbc.password");

		this.dropTableTemplate = rb.getString("template.droptable");
		this.testSql = rb.getString("test_sql");
		this.createSqlFile = rb.getString("create_sql_file");
		this.initSqlFile = rb.getString("init_sql_file");
		this.clearSqlFile = rb.getString("clear_sql_file");
	}

	public SetupDatabase(String jdbcDriver, String jdbcUrl,
			String jdbcUserName, String jdbcPassword) {
		this.jdbcDriver = jdbcDriver;
		this.jdbcUrl = jdbcUrl;
		this.jdbcUserName = jdbcUserName;
		this.jdbcPassword = jdbcPassword;
	}

	public void setup() throws SQLException, IOException {
		if (createDB()) {
			initDB();
		}
	}

	public void initDB() throws SQLException, IOException {
		log.debug("开始初始化数据库...");
		log.debug("InitSql:\r\n" + getInitSqls());
		executeSql(getInitSqls());
		log.debug("数据库初始化成功");
	}

	public void clearDB() throws SQLException, IOException {
		log.debug("开始清除数据库...");
		log.debug("ClearSql:\r\n" + getClearSqls());
		executeSql(getClearSqls());
		log.debug("数据库清除成功");
	}

	public void dropDB() throws SQLException, IOException {
		log.debug("开始删除数据库表...");
		log.debug("DropSql:\r\n" + getDropTableSqls());
		executeSql(getDropTableSqls(), true);
		log.debug("数据库表删除成功");
	}

	public boolean createDB() throws SQLException, IOException {
		Connection con = getConnection();
		PreparedStatement ps = null;
		try {
			log.debug("测试数据库表是否创建:" + testSql);
			log.debug("TestSql:\r\n" + testSql);
			List<String> sqls = new ArrayList<String>();
			sqls.add(testSql);
			executeSql(sqls);
			log.info("数据库表已经存在,不需重建");
			return false;
		} catch (Exception e) {
			log.debug("开始创建数据库...");
			log.debug("CreateSql:\r\n" + getCreateSqls());
			executeSql(getCreateSqls(), true);
			log.debug("数据库创建成功");
			return true;
		}
	}

	public List<String> getInitSqls() throws IOException {
		if (this.initSqls == null) {
			this.initSqls = getSqls(initSqlFile);
		}
		return this.initSqls;
	}

	public List<String> getClearSqls() throws IOException {
		if (this.clearSqls == null) {
			if (!StringUtil.isEmpty(clearSqlFile)) {
				List<String> ret = getSqls(clearSqlFile);
				if (ret != null && !ret.isEmpty())
					this.clearSqls = ret;
			}
			if (this.clearSqls == null)
				this.clearSqls = getDropOrDeleteTableSqls("delete from $1");
		}
		return this.clearSqls;
	}

	private List<String> getDropTableSqls() throws IOException {
		return getDropOrDeleteTableSqls(dropTableTemplate);
	}

	private List<String> getDropOrDeleteTableSqls(String template)
			throws IOException {
		List<String> cs = getCreateSqls();
		List<String> ret = new ArrayList<String>();
		for (int i = 0; i < cs.size(); i++) {
			String s = cs.get(i);
			Pattern p = Pattern.compile(
					".*\\s*CREATE\\s+TABLE\\s+([^\\s\\(]+).*",
					Pattern.CASE_INSENSITIVE + Pattern.MULTILINE
							+ Pattern.DOTALL);
			Matcher m = p.matcher(s);
			if (m.matches()) {
				ret.add(m.replaceAll(template));
			}
		}
		return ret;
	}

	public List<String> getCreateSqls() throws IOException {
		if (this.createSqls == null)
			this.createSqls = getSqls(createSqlFile);
		return this.createSqls;
	}

	public static List<String> split2Sqls(String sql) throws IOException {
		return split2Sqls(new BufferedReader(new StringReader(sql)));
	}
	
	private static List<String> getSqls(String sqlFileName) throws IOException {
		InputStream is = SetupDatabase.class.getResourceAsStream("/"
				+ sqlFileName);
		if (is == null) {
			log.warn("找不到文件:" + sqlFileName);
			return Collections.emptyList();
		}
		return split2Sqls(new BufferedReader(new InputStreamReader(is)));
	}
	
	public static List<String> split2Sqls(BufferedReader reader) throws IOException {
		List<String> sqls = new ArrayList<String>();
		StringBuffer sb = new StringBuffer();
		String line;
		while ((line = reader.readLine()) != null) {
			line = removeSqlComment(line.trim());
			if (line.length() == 0)
				continue;
			sb.append(" ").append(line);
			if (line.endsWith(";")) {
				sb.delete(sb.length() - 1, sb.length());
				sqls.add(sb.toString().trim());
				sb.delete(0, sb.length());
			}
		}
		if (sb.toString().trim().length() > 0)
			sqls.add(sb.toString().trim());
		return sqls;
	}

	private static String removeSqlComment(String line) {
		int index = line.indexOf("--");
		if (index == -1)
			index = line.indexOf("//");
		if (index >= 0) {
			return line.substring(0, index);
		}
		return line;
	}

	public Connection getConnection() throws SQLException {
		try {
			Class.forName(jdbcDriver);
		} catch (ClassNotFoundException e) {
			throw new SQLException("cannot load jdbc driver class.");
		}
		log.debug("connection:" + jdbcUrl + "," + jdbcUserName + ","
				+ jdbcPassword);
		return DriverManager.getConnection(jdbcUrl, jdbcUserName, jdbcPassword);
	}

	public static void executeSql(Connection con, List<String> sqls)
			throws SQLException {
		executeSql(con, sqls, false);
	}

	public static void executeSql(Connection con, List<String> sqls,
			boolean ignoreException) throws SQLException {
		for (int i = 0; i < sqls.size(); i++) {
			try {
				executeSql(con, sqls.get(i));
			} catch (SQLException e) {
				if (!ignoreException)
					throw e;
				else
					log.debug("error to execute sql: '" + sqls.get(i) + "'", e);
			}
		}
	}

	public static void executeSql(Connection con, String sql)
			throws SQLException {
		PreparedStatement ps = null;
		try {
			con.setAutoCommit(false);
			ps = con.prepareStatement(sql);
			ps.execute();
			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw e;
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public void executeSql(List<String> sqls) throws SQLException {
		executeSql(sqls, false);
	}

	public void executeSql(List<String> sqls, boolean ignoreException)
			throws SQLException {
		Connection con = getConnection();
		try {
			executeSql(con, sqls, ignoreException);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	// ~ getter and setter

	public String getClearSqlFile() {
		return clearSqlFile;
	}

	public String getCreateSqlFile() {
		return createSqlFile;
	}

	public String getInitSqlFile() {
		return initSqlFile;
	}

	public String getJdbcDriver() {
		return jdbcDriver;
	}

	public String getJdbcPassword() {
		return jdbcPassword;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public String getJdbcUserName() {
		return jdbcUserName;
	}

	public String getTestSql() {
		return testSql;
	}

	public void setClearSqlFile(String clearSqlFile) {
		this.clearSqlFile = clearSqlFile;
	}

	public void setCreateSqlFile(String createSqlFile) {
		this.createSqlFile = createSqlFile;
	}

	public void setInitSqlFile(String initSqlFile) {
		this.initSqlFile = initSqlFile;
	}

	public void setJdbcDriver(String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
	}

	public void setJdbcPassword(String jdbcPassword) {
		this.jdbcPassword = jdbcPassword;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public void setJdbcUserName(String jdbcUserName) {
		this.jdbcUserName = jdbcUserName;
	}

	public void setTestSql(String testSql) {
		this.testSql = testSql;
	}

	public void setClearSqls(List<String> clearSqls) {
		this.clearSqls = clearSqls;
	}

	public void setCreateSqls(List<String> createSqls) {
		this.createSqls = createSqls;
	}

	public void setInitSqls(List<String> initSqls) {
		this.initSqls = initSqls;
	}

	// ~ implements DataSource

	public Connection getConnection(String username, String password)
			throws SQLException {
		return getConnection();
	}

	public PrintWriter getLogWriter() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
	}

	public void setLoginTimeout(int seconds) throws SQLException {
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}
}
