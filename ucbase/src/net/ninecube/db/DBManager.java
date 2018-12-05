/*
 * Created on 2004-12-21
 */
package net.ninecube.db;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import net.ninecube.util.DataTypeUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;




/**
 * 
 * @author JXB
 * 主要用于支持复合主键
 * 
 * 支持数据库事务，多层嵌套的数据库事务将被合并为一个事务，但使用方法：
 * <pre>
 * DBManager.beginTransction();
 * try {
 *    ...
 *    数据库操作(只有DBManager相关类的数据库操作才在事务中)
 *    ...
 *    DBManager.commitTransction();
 * } catch (RuntimeException e) { //@ TODO: 事务控制：如果抛出其它异常，是否会导致连接不会被释放？
 *    DBManager.rollbackTransaction();
 *    throw e;
 * } finally {
 *    DBManager.endTransaction();
 * }
 * </pre>
 * 注意：只有DBManager及其相关类的数据库操作才在事务中
 * 
 * @ TODO: 数据缓存？
 */
public abstract class DBManager<E extends Entity> extends DBUtil {	
	private static final Log log = LogFactory.getLog(DBManager.class);
	
	//~ transaction 
	
	public static void setDemoDatabase(String demoDatabase) {
		TransactionManager.get().setDemoDatabase(demoDatabase);
	}
	
	public static void setDataSource(DataSource ds) {
		TransactionManager.ds = ds;
	}
	
	public static void beginTransction() {
		TransactionManager.get().begin();
	}
	
	public static void commitTransction() {
		TransactionManager.get().commit();
	}
	
	public static void rollbackTransaction() {
		TransactionManager.get().rollback();
	}
	
	public static void endTransaction() {
		TransactionManager.get().end();
	}

    //~ business method
    
    public boolean exist(E o) throws DBException {
        return getById(getKeyValues(o)) != null;
    }

    public void insert(E o) throws DBException {
        checkPrimaryKey(o);
        if (executeUpdate(getInsertSql(o), getAllParams(o)) != 1)
            throw new DBException();
    }

    public void update(E o) throws DBException {
        checkPrimaryKey(o);
        int ret = executeUpdate(getUpdateSql(o), getAllParams(o)); 
        if (ret != 1)
            throw new DBException("影响记录条数："+ret);
    }

    public void save(E o) throws DBException {
        if (o.isNew()) {
            assignId(o);
            insert(o); 
        }
        else {
            update(o);
        }
        o.setNew(false);
    }
    
    public void delete(E e) throws DBException {
        if (executeUpdate(getDeleteSql(), getKeyValues(e)) != 1)
            throw new DBException();
        e.setNew(true);
    }

    public E getById(Object id) throws DBException {
        return getByWhere(" where "+getKeyWhere(), getParamsFromKey(id));
    }

    public E getByWhere(String where, Object[] params) throws DBException {
        String sql = getSelectSql() + " " + where;
        Object[] values = executeSelect(sql, params);
        if (values == null) return null;
        E e = fromArray(values);
        e.setNew(false);
        return e;
    }
    
    public int count(String where, Object[] param) throws DBException {
        String sql = "select count(*) from " + getTableName();
        if (where != null)
            sql += " " + where;
        return DataTypeUtil.obj2Int(executeSelect(sql, param)[0],0);
    }

    public List<E> query(String where, Object[] param, int start, int count) throws DBException {
        List ret = new ArrayList();
        String sql = getSelectSql();
        if (where != null)
            sql += " " + where;
        return queryBySql(sql, param, start, count);
    }

    public List<E> queryBySql(String sql, Object[] param, int start, int count) throws DBException {
        List<E> ret = new ArrayList<E>();
        List<Object[]> list = executeQuery(sql, param, start, count);
        for (int i = 0; i < list.size(); i++) {
            Object[] values = list.get(i);
            E e = fromArray(values);
            e.setNew(false);
            ret.add(e);
        }

        return ret;
    }

    //~
    
    protected String getDeleteSql() {
        StringBuffer sb = new StringBuffer();

        sb.append("delete from ").append(getTableName());
        sb.append(" where ").append(getKeyWhere());

        return sb.toString();
    }

    protected String getInsertSql(E o) {
        StringBuffer sb = new StringBuffer();

        sb.append("insert into ").append(getTableName()).append("(");
        Object[] values = getFieldValues(o); 
        for (int i = 0; i < getFields().length; i++) {
        	if (values[i] != null || updateNull()) sb.append(getFields()[i]).append(",");
        }
        for (int i = 0; i < getKeyFields().length; i++) {
            sb.append(getKeyFields()[i]).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(") values(");
        
        for (int i = 0; i < getFields().length; i++) {
        	if (values[i] != null) sb.append("?,");
        	else if (updateNull()) sb.append("null,");
        }
        for (int i = 0; i < getKeyFields().length; i++) {
        	sb.append("?,");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");

        return sb.toString();
    }

    protected String getUpdateSql(E o) {
        StringBuffer sb = new StringBuffer();
        sb.append("update ").append(getTableName()).append(" set ");
        
        Object[] values = getFieldValues(o); 
        for (int i = 0; i < getFields().length; i++) {
        	if (values[i] != null) sb.append(getFields()[i]).append("=?,");
        	else if (updateNull()) sb.append(getFields()[i]).append("=null,");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" where ").append(getKeyWhere());
        return sb.toString();
    }
    
    protected boolean updateNull() {
    	return true;
    }
    protected String getSelectSql() {
        StringBuffer sb = new StringBuffer();
        sb.append(getSelectClause());
        sb.append(" from ").append(getTableName());
        return sb.toString();
    }
    
    protected String getSelectClause() {
        StringBuffer sb = new StringBuffer();
        sb.append("select ");
        for (int i = 0; i < getFields().length; i++) {
            sb.append(getFields()[i]).append(",");
        }
        sb.append(getKeyFields()[0]);
        for (int i = 1; i < getKeyFields().length; i++) {
            sb.append(",").append(getKeyFields()[i]);
        }
        return sb.toString();
    }
    
    protected String getKeyWhere() {
        StringBuffer sb = new StringBuffer();
        sb.append(getKeyFields()[0]).append("=?");
        for (int i = 1; i < getKeyFields().length; i++) {
            sb.append(" and ").append(getKeyFields()[i]).append("=?");
        }
        return sb.toString();
    }
    
    protected Object[] getParamsFromKey(Object id) {
        if (this.getKeyFields().length == 1) return new Object[]{id};
        return (Object[])id;
    }
    
    protected Object[] getAllParams(E o) {
        List<Object> ret = new ArrayList<Object>();

        Object[] values = getFieldValues(o); 
        for (int i = 0; i < values.length; i++) {
        	if (values[i] != null) ret.add(values[i]);
        	else if (updateNull()) ; // 参数值在生成时已写进SQL语句中了
        }
        for (int i = 0; i < getKeyValues(o).length; i++) {
            ret.add(getKeyValues(o)[i]);
        }
        return ret.toArray(new Object[ret.size()]);
    }
    
    protected void checkPrimaryKey(E o) throws DBException {
        for (int i = 0; i < getKeyValues(o).length; i++) {
            if (getKeyValues(o)[i] == null) {
                throw new DBException("primary key is null:"+getKeyFields()[i]);
            }
        }
    }
    
    //~ 抽象方法
    
    protected abstract String getTableName();

    //返回字段名列表,不包含主键字段
    protected abstract String[] getFields();

    protected abstract Object[] getFieldValues(E o);
    
    protected abstract String[] getKeyFields();

    protected abstract Object[] getKeyValues(E o);
    
    protected abstract E fromArray(Object[] values);

    /**
     * 如果是插入数据库，在此方法中为新对象分配主键
     */
    protected void assignId(E o) {
    	if (idIsNull(o))
    		throw new UnsupportedOperationException("对象主键为空");
    }
    
    private boolean idIsNull(E o) {
    	Object[] id = getKeyValues(o);
    	for (int i = 0; i < id.length; i++)
    		if (id[i] == null) return true;
    	return false;
    }
}
