/**
 * 2007-6-10
 */
package net.ninecube.core.db.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JXB
 * 
 */
public class SqlBuilder {

	private List<Object> params = new ArrayList<Object>();
	private StringBuffer where = new StringBuffer();

	public String getWhere() {
		if (this.where.length() > 0)
			return "where " + this.where;
		else
			return "";
	}

	public Object[] getParams() {
		return this.params.toArray();
	}

	public SqlBuilder and(String condition, Object param) {
		if (!Util.isEmpty(param)) {
			if (this.where.length() != 0)
				this.where.append(" and ");
			this.where.append(condition);
			this.params.add(param);
		}
		return this;
	}

	public SqlBuilder and_like(String condition, String param) {
		if (!Util.isEmpty(param)) {
			return and(condition, "%" + param + "%");
		}
		return this;
	}

}
