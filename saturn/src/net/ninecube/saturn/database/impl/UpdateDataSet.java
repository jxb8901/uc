/**
 * 
 * created on 2007-4-11
 */
package net.ninecube.saturn.database.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import net.ninecube.saturn.database.Column;
import net.ninecube.saturn.database.DataSet;
import net.ninecube.saturn.database.DatabaseException;
import net.ninecube.saturn.database.Table;
import net.ninecube.saturn.database.jrc.JrcUtil;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jrc.contribute.Jrc;
import org.codehaus.jrc.contribute.util.GetTablesVisitorUtil;
import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.relation.Relation;

/**
 * 支持的操作：update, where
 * 不支持的操作：insert, alias, select(project, groupby, orderby)
 * @author jxb
 * 
 */
public class UpdateDataSet extends WhereDataSet {
	protected Table targetTable;
	private List<Column> targetColumns = new ArrayList<Column>();
	protected List<Expression> targetValues =  new ArrayList<Expression>();

	public UpdateDataSet(WhereDataSet parent, Column column, Expression value) {
		super(parent.getDatabaseManager());
		this.__update(column, value);
	}
	
	@Override
	protected List<String> getUnsupportedOperations(){
		return Arrays.asList("insert", "alias", "groupby", "project", "orderby");
	}

	protected void checkMerge(DataSet other){
		if (other instanceof UpdateDataSet) {
			UpdateDataSet o = (UpdateDataSet) other;
			if (this.targetTable != null && !this.targetTable.equals(o.targetTable)) {
				throw new IllegalArgumentException();
			}
		}
	}
	
	@Override
	public String getSql() {
		StringBuffer sql = new StringBuffer();
		sql.append("update ").append(this.targetTable.getName()).append(" set ");
		for(int i = 0 ; i < this.targetColumns.size() ; i++){
			sql.append(this.targetColumns.get(i).getName()).append("=");
			sql.append(Jrc.getSqlGenerator().getSql(this.targetValues.get(i))).append(",");
		}
		sql.setLength(sql.length() - 1);
		
		Relation r = this.getRelation();
		if (r != null){ 
			if (false) { // 这种方式的SQL因为MYSQL不支持故不使用：update update_table set a=1 where update_table.key in (select update_table.key from ... where ...)
				Set<String> tablenames = JrcUtil.getTablesInFromClause(r);
				if (tablenames.contains(this.targetTable.getName()))
					throw new DatabaseException("由于Mysql不支持被更新的表["+this.targetTable.getName()+
							"'出现在FROM子句中："+tablenames+",故本SQL语句将不能生成.");
				sql.append(" where " + this.targetTable.getPrimaryKey().getSqlCode() + " in (");
				sql.append(new SelectDataSet(this).project(this.targetTable.getPrimaryKey().getSqlCode()).getSql());
				sql.append(")");
			} 
			else {
				sql.append(" where " + Jrc.getSqlGenerator().getSql(this.getCondition()));
			}
		}
		return sql.toString();
	}

	@Override
	public WhereDataSet update(Column column, Expression value) {
		UpdateDataSet ret = (UpdateDataSet) this.clone(); 
		if (!ret.targetTable.equals(column.getOwner())){
			throw new DatabaseException("不能同时update多个表:"+
					ret.targetTable.getName() + "," + column.getOwner().getName());
		}
		ret.__update(column, value);
		
		return ret;
	}
	
	private void __update(Column column, Expression value) {
		this.targetTable = column.getOwner();
		this.targetColumns.add(column);
		this.targetValues.add(value);
		Set<String> tables = GetTablesVisitorUtil.getImportedTables(value);
		if(tables.size() > 1 || (tables.size()==1 && !tables.contains(this.targetTable.getName()))){
			throw new DatabaseException("当前不支持从其它表更新数据:"+tables+","+value);
		}
	}
	
	@Override
	public String toString() {
		String ret = "update " + this.targetTable + " set (" + 
				StringUtils.join(this.targetColumns.toArray(), ",") + ") = (" +
				StringUtils.join(this.targetValues.toArray(), ",") + ")";
		String where = super.toString();
		if (where.equals("[]")) return ret;
		return ret + " where " + where;
	}
}
