package net.ninecube;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Date;

import org.apache.commons.beanutils.DynaProperty;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

/**
 * @author  jxb
 */
public abstract class CreateVenusTables extends AbstractTransactionalDataSourceSpringContextTests {
	protected LocalSessionFactoryBean sessionFactory;

	public void setSessionFactory(LocalSessionFactoryBean sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	protected String[] getConfigLocations() {
		return new String[]{
				"applicationContext-mysql.xml",
				"classpath*:applicationContext-hibernate.xml",
				"classpath*:applicationContext-transaction.xml"
		};
	}
	
	public static void create() throws Exception {
		CreateVenusTables c = new CreateVenusTables(){};
		c.setUp();
		c.sessionFactory.dropDatabaseSchema();
//		c.sessionFactory.createDatabaseSchema();
//		c.sessionFactory.updateDatabaseSchema();
		c.setComplete();
		c.tearDown();
	}

	public static void main(String[] a) throws Exception {
		create();

//		Class.forName("com.mysql.jdbc.Driver").newInstance();
//		Connection con = DriverManager.getConnection("jdbc:mysql://svnserver:3306/saturntest?useUnicode=true&amp;characterEncoding=utf8", "admin", "admin");
////		Class.forName("org.hsqldb.jdbcDriver").newInstance();
////		Connection con = DriverManager.getConnection("jdbc:hsqldb:hsql://127.0.0.1:9001/sample", "sa", "");
//		Statement s = con.createStatement();
//		ResultSet rs = s.executeQuery("select customername as Name from customers");
//		ResultSetMetaData rsmd = rs.getMetaData();
//		int n = rsmd.getColumnCount();
//		rs.next();
//        	for (int i = 1; i <= n; i++) { // JDBC is one-relative!
//        		System.out.println(rsmd.getColumnLabel(i));
//        		System.out.println(rsmd.getCatalogName(i));
//        		System.out.println(rsmd.getColumnDisplaySize(i));
//        		System.out.println(rsmd.getColumnType(i));
//        		System.out.println(rsmd.getColumnTypeName(i));
//        		System.out.println(rsmd.getColumnName(i));
//        		System.out.println(rsmd.getColumnClassName(i));
//        		System.out.println(rs.getObject(rsmd.getColumnLabel(i)));
//        		System.out.println(rs.getObject(rsmd.getColumnName(i)));
//        }
	}
}
