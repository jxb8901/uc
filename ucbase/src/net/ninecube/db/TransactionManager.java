/**
 * 2005-8-12
 */
package net.ninecube.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author JXB
 *
 */
class TransactionManager {
	static DataSource ds;
	private static final Log log = LogFactory.getLog(TransactionManager.class);
	private static final ThreadLocal<TransactionManager> context = new ThreadLocal<TransactionManager>();
	
	static TransactionManager get() {
		TransactionManager tm = context.get();
		if (tm == null) {
			tm = new TransactionManager();
			context.set(tm);
		}
		return tm;
	}
	
	static void set(TransactionManager tm) {
		context.set(tm);
	}
	
	private Connection con = null;
	private int transactionTimes = 0;
	private String demoDatabase = null;

	void setDemoDatabase(String demoDatabase) {
		this.demoDatabase = demoDatabase;
	}
	
	void begin() {
		if (transactionTimes == 0) { 
			log.debug("dabatase transaction begin.");
		}
		transactionTimes++;
	}
	
	void commit() {
		if (con != null && transactionTimes <= 1) { //只有到最外层的事务时才真正提交或回滚
			try {
				con.commit(); // con不可能为null
			} catch (SQLException e) {
				throw new DBException("提交数据库操作异常", e);
			}
			log.debug("dabatase transaction has commited!");
		}
	}
	
	void rollback() {
		if (con != null && transactionTimes <= 1) { //只有到最外层的事务时才真正提交或回滚
			try {
				con.rollback(); // con不可能为null
			} catch (SQLException e) {
				throw new DBException("回滚数据库操作异常", e);
			}
			log.debug("dabatase transaction has rollbacked!");
		}
	}
	
	void end() {
		transactionTimes--;
		freeConnection(con);
		if (transactionTimes <= 0) {
			log.debug("dabatase transaction end.");
		}
	}
	
	Connection getConnection() {
		if (con == null) {
			try {
				con = ds.getConnection();
			} catch (Exception e1) {
				throw new DBException(e1);
			}
			if (transactionTimes >= 1) { //只有第一次开始事务时才取连接? @ TODO: 没有这个控制时出con != null and sp == null的情况，为什么？
				try {
					con.setAutoCommit(false);
				} catch (SQLException e) {
					throw new DBException("设置数据库事务异常", e);
				}
				log.debug("get dabatase connection.");
			}
		}
		return con;
	}
	
	void freeConnection(Connection con) {
		if (transactionTimes <= 0) {
			try {
				if (con != null) {
					con.setAutoCommit(true);
				}
				con.close(); // 不可能出现：con != null 但 sp == null
			} catch (SQLException e) {
				throw new DBException("关闭数据库事务异常", e);
			} finally {
				reset();
			}
		}
	}
	
	private void reset() {
		con = null;
		transactionTimes = 0;
	}
}
