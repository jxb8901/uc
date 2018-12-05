/**
 * 
 * created on 2007-5-20
 */
package net.ninecube.reports.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.RowSetDynaClass;

/**
 * 
 * @author jxb
 */
public class VenusRowSetDynaClass extends RowSetDynaClass {

	public VenusRowSetDynaClass(ResultSet resultSet, boolean lowerCase, int limit) throws SQLException {
		super(resultSet, lowerCase, limit);
	}

	public VenusRowSetDynaClass(ResultSet resultSet, boolean lowerCase) throws SQLException {
		super(resultSet, lowerCase);
	}

	public VenusRowSetDynaClass(ResultSet resultSet, int limit) throws SQLException {
		super(resultSet, limit);
	}

	public VenusRowSetDynaClass(ResultSet resultSet) throws SQLException {
		super(resultSet);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void copy(ResultSet resultSet) throws SQLException {
		int cnt = 0;
		while (resultSet.next() && (limit < 0 || cnt++ < limit)) {
			DynaBean bean = createDynaBean();
			for (int i = 0; i < properties.length; i++) {
				String name = properties[i].getName();
				// 原始实现是通过name取得字段值，这在hsql数据库中没有任何问题，
				// 但在mysql中却会报错：“Column 'customername' not found.”
				// 这里的name实际是调用metadata.getColumnName()取得的，通过测试
				// 如果将metadata.getColumnLabel()取得的值作为name，则在mysql和hsql
				// 数据库中都没有问题，但jdk api中说使用索引取值可能会比使用名称取值更高效
				// 而且使用索引取值的方式在任何数据库中都是可行的。因此修改为如下的方式：
				// bean.set(name, resultSet.getObject(name));
				bean.set(name, resultSet.getObject(i+1));
			}
			rows.add(bean);
		}
	}

	@Override
	protected DynaProperty createDynaProperty(ResultSetMetaData metadata, int i) throws SQLException {

		String name = null;
		// 原来使用metadata.getColumnLabel方法在Mysql下无法取得真正别名，修改为metadata.getColumnLabel
		if (lowerCase) {
			name = metadata.getColumnLabel(i).toLowerCase();
		} else {
			name = metadata.getColumnLabel(i);
		}
		String className = null;
		try {
			className = metadata.getColumnClassName(i);
		} catch (SQLException e) {
			// this is a patch for HsqlDb to ignore exceptions
			// thrown by its metadata implementation
		}

		// Default to Object type if no class name could be retrieved
		// from the metadata
		Class clazz = Object.class;
		if (className != null) {
			clazz = loadClass(className);
		}
		return new DynaProperty(name, clazz);

	}

}
