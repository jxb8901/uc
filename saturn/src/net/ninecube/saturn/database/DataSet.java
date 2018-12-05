package net.ninecube.saturn.database;

import java.util.Date;
import java.util.List;

import net.ninecube.saturn.database.exception.ColumnNotFoundException;

import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.predicate.Predicate;
import org.codehaus.jrc.relation.Relation;

public interface DataSet {
	
	public DataSet insert(Column column);
	/**
	 * 本方法不支持根据一个表的数据更新另一个表数据的SQL
	 */
	public DataSet update(Column column, Expression value);
	public DataSet project(Expression expr);
	public DataSet groupby(Expression expr);
	public DataSet orderby(Expression expr, boolean asc, int top);
	public DataSet where(Predicate pred);	
	public AliasDataSet alias(String alias);
	
	public DataSet[] split(Predicate pred);
	
	/**
	 * 将本DataSet与other合并成一个新的DataSet并返回
	 * 合并操作不影响本身状态
	 */
	public DataSet merge(DataSet other);

	/**
	 * 中心表一定不会为null
	 */
	public Table getCentricTable();
	
	public Table getBaseTable();

	public Column getColumnByAlias(String name) throws ColumnNotFoundException;

	// public void addDataSet(String name);

	public String getSql();
	public String getSql(String sqltype);
	
	public Relation getRelation();

	public void setDateRange(Date[] dateRng);

	public Date[] getDateRange();

	public DataSet clone();

	/**
	 * @return 如果getRelation()为空则返回真，否则返回假
	 */
	public boolean isEmpty();

}
