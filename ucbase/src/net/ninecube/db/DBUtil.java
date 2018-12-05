/**
 * 2005-8-14
 */
package net.ninecube.db;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.ninecube.lang.TypedMap;
import net.ninecube.util.DataTypeUtil;
import net.ninecube.util.XmlUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author JXB
 *
 */
public class DBUtil {
	private static final Log log = LogFactory.getLog(DBUtil.class);
	
	public static int setBigData(String tableName, String keyFieldName, String bigDataFieldName, Object key, TypedMap kd) {
		String s = XmlUtil.obj2xml(kd);
		return DBManager.executeUpdate("UPDATE "+tableName+" SET "+bigDataFieldName+"=? WHERE "+keyFieldName+"=?", 
				new Object[]{s, key});
	}
	
	public static TypedMap getBigData(String tableName, String keyFieldName, String bigDataFieldName, Object key) {
		Object[] o = DBManager.executeSelect("SELECT "+bigDataFieldName+" FROM "+tableName+" WHERE "+keyFieldName+"=?", 
				new Object[]{key});
		log.debug("read keyedDataEx from db:"+o[0]);
		return (TypedMap) XmlUtil.xml2obj((String)o[0]);
	}
	
	public static int countBySql(String sql, Object[] params) {
		Object[] values = executeSelect(sql, params);
		return DataTypeUtil.obj2Int(values[0], 0);
	}
	
	public static List<TypedMap<String, Object>> query(String sql, Object[] params, String[] fields) {
		return query(sql, params, fields, 1, Integer.MAX_VALUE);
	}

	public static List<TypedMap<String, Object>> query(String sql, Object[] params, String[] fields, int start, int count) {
		List<TypedMap<String, Object>> ret = new ArrayList<TypedMap<String, Object>>();
		List rs = executeQuery(sql, params, start, count);
		if (rs == null) return ret;
		for (int i = 0; i < rs.size(); i++) {
			ret.add(array2KeyedData((Object[])rs.get(i), fields));
		}
		return ret;
	}

	public static TypedMap<String, Object> select(String sql, Object[] params, String[] fields) {
		Object[] values = executeSelect(sql, params);
		if (values == null) return new TypedMap<String, Object>();
		return array2KeyedData(values, fields);
	}
	
	public static int update(String sql, Object[] params) {
		return executeUpdate(sql, params);
	}
	
	public static List<Object[]> executeQuery(Sql sql) throws DBException{
		return executeQuery(sql.getSql(), sql.getParameters().toArray(), sql.getStartIndex(), sql.getCount());
	}
	
	public static Object[] executeSelect(Sql sql) throws DBException{
		return executeSelect(sql.getSql(), sql.getParameters().toArray());
	}
	
	public static int executeUpdate(Sql sql) throws DBException{
		return executeUpdate(sql.getSql(), sql.getParameters().toArray());
	}
	
	public static int[] executeUpdate(Sql[] sqls) throws DBException{
		int[] ret = new int[sqls.length];
		for (int i = 0; i < sqls.length; i++)
			ret[i] = executeUpdate(sqls[i].getSql(), sqls[i].getParameters().toArray());
		return ret;
	}

    /**
     * @param startIndex 开始记录号,从1开始计数
     */
    public static List<Object[]> executeQuery(String sql, Object[] params, int startIndex,
            int count) throws DBException{
        log.debug("--executeQuery--sql:"+sql);
        log.debug("--executeQuery--params:start="+startIndex+",count="+count+","+(params == null? "[]" : Arrays.asList(params).toString()));
        
        List<Object[]> result = new ArrayList<Object[]>();
        if (count <= 0)
            return result;
        if (startIndex <= 0)
            startIndex = 1;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = TransactionManager.get().getConnection();
            ps = con.prepareStatement(sql);//,
                                           // ResultSet.TYPE_SCROLL_INSENSITIVE,
                                           // ResultSet.CONCUR_READ_ONLY);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                	setParameter(ps, i + 1, params[i]);
                }
            }
            rs = ps.executeQuery();
            if (absolute(rs, startIndex)) {
                int cc = rs.getMetaData().getColumnCount();
                do {
                    Object[] row = new Object[cc];
                    for (int i = 0; i < row.length; i++) {
                        row[i] = rs.getObject(i + 1);
                    }
                    result.add(row);
                } while (rs.next() && --count > 0);
            }
        } catch (SQLException e) {
            throw new DBException(e);
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (Exception exception1) {
                }
            if (ps != null)
                try {
                    ps.close();
                } catch (Exception exception1) {
                }
            if (con != null)
                TransactionManager.get().freeConnection(con);
        }
        return result;
    }
    
    public static Object[] executeSelect(String sql, Object[] params) throws DBException{
    	log.debug("--executeSelect--sql:"+sql);
    	log.debug("--executeSelect--params:"+(params == null? "[]" : Arrays.asList(params).toString()));

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = TransactionManager.get().getConnection();
            ps = con.prepareStatement(sql);//,
                                           // ResultSet.TYPE_SCROLL_INSENSITIVE,
                                           // ResultSet.CONCUR_READ_ONLY);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                	setParameter(ps, i + 1, params[i]);
                }
            }
            rs = ps.executeQuery();
            if (rs.next()) {
                int cc = rs.getMetaData().getColumnCount();
                Object[] row = new Object[cc];
                for (int i = 0; i < row.length; i++) {
                    row[i] = rs.getObject(i + 1);
                }
                return row;
            }
        } catch (SQLException e) {
            throw new DBException(e);
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (Exception exception1) {
                }
            if (ps != null)
                try {
                    ps.close();
                } catch (Exception exception1) {
                }
            if (con != null)
                TransactionManager.get().freeConnection(con);
        }
        return null;
    }
    
    public static int executeUpdate(String sql, Object[] params) throws DBException{
    	log.debug("--executeUpdate--sql:"+sql);
    	log.debug("--executeUpdate--params:"+(params == null? "[]" : Arrays.asList(params).toString()));

        Connection con = null;
        PreparedStatement ps = null;
        try {
			con = TransactionManager.get().getConnection();
            ps = con.prepareStatement(sql);
            if (params != null) {
                for (int i = 0,j = 1; i < params.length; i++) {
                    setParameter(ps, j++, params[i]);
                }
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(e);
        } finally {
            if (ps != null)
                try {
                    ps.close();
                } catch (Exception exception1) {
                }
            if (con != null)
                TransactionManager.get().freeConnection(con);
        }
    }
    
    private static void setParameter(PreparedStatement ps, int i, Object o) throws SQLException {
    	if (o == null) {
    		ps.setNull(i, Types.NULL); //这里可能有问题??
    	}
    	else if (o instanceof String) {
    		ps.setString(i, (String)o);
    	}
    	else if (o instanceof Boolean) {
    		ps.setString(i, ((Boolean)o).booleanValue()? "1" : "0");
    	}
    	else if (o instanceof Date) {
    		ps.setDate(i, (Date)o);
    	}
    	else if (o instanceof Timestamp) {
    		ps.setTimestamp(i, (Timestamp)o);
    	}
    	else if (o instanceof java.util.Date) {
    		ps.setDate(i, new Date(((java.util.Date)o).getTime()));
    	}
    	else if (o instanceof Integer) {
    		ps.setInt(i, ((Integer)o).intValue());
    	}
    	else if (o instanceof BigDecimal) {
    		ps.setDouble(i, ((BigDecimal)o).doubleValue());
    	}
    	else {
    		ps.setObject(i, o);
    	}
    }

    private static boolean absolute(ResultSet rs, int p) throws SQLException {
        while (rs.next() && --p > 0)
            ;
        return p == 0;
    }
	
	private static TypedMap<String, Object> array2KeyedData(Object[] values, String[] fields) {
		TypedMap<String, Object> kd = new TypedMap<String, Object>();
		for (int j = 0; j < fields.length; j++) {
			kd.put(fields[j], values[j]);
		}
		return kd;
	}
}
