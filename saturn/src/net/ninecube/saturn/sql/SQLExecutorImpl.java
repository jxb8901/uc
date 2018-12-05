package net.ninecube.saturn.sql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.ninecube.db.DBUtil;

import org.apache.log4j.Logger;

public class SQLExecutorImpl implements SQLExecutor {
	private static final Logger log = Logger.getLogger(SQLExecutorImpl.class);
	private boolean autoCommit = true;
	private List<String> sqls = new ArrayList<String>();

	public void execute(String sql){
		sqls.add(sql);
		if (autoCommit)
			commit();
	}

	public void setAutoCommit(Boolean autoCommit) {
		this.autoCommit = autoCommit;
	}

	public void commit() {
		for (Iterator<String> it = sqls.iterator(); it.hasNext();) {
			DBUtil.update(it.next(), null);
			it.remove();
		}
	}

}
