/**
 * 
 * created on 2007-4-11
 */
package net.ninecube.saturn.database.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.ninecube.saturn.database.Column;
import net.ninecube.saturn.database.DataSet;
import net.ninecube.saturn.database.DatabaseException;
import net.ninecube.saturn.database.Table;
import net.ninecube.saturn.database.jrc.JrcUtil;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jrc.contribute.Jrc;
import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.relation.Relation;

/**
 * 支持的操作：insert, where, select(project, groupby, orderby) 
 * 不支持的操作：update, alias
 * @author jxb
 * 
 */
public class InsertDataSet extends SelectDataSet {
	protected Table targetTable;
	protected List<Column> targetColumns = new ArrayList<Column>();

	public InsertDataSet(WhereDataSet parent, Column column) {
		super(parent);
		this.__insert(column);
	}
	
	@Override
	protected List<String> getUnsupportedOperations(){
		return Arrays.asList("update", "alias");
	}
	
	@Override
	protected void checkMerge(DataSet other){
		if (other instanceof InsertDataSet) {
			InsertDataSet o = (InsertDataSet) other;
			if (this.targetTable != null && !this.targetTable.equals(o.targetTable)) {
				throw new IllegalArgumentException();
			}
		}
	}

	
	@Override
	public String getSql() {
		if (super.projects.size() != this.targetColumns.size())
			throw new DatabaseException("columns' size must be equals to projects' size.");
		StringBuffer sql = new StringBuffer();
		sql.append("insert into ").append(this.targetTable.getName()).append("(");
		for (Column des : targetColumns) 
			sql.append(des.getName()).append(",");
		sql.setLength(sql.length() - 1);
		sql.append(") ");
		if (this.isEmpty()) {
			sql.append("values(");
			for (Expression expr : super.projects) 
				sql.append(Jrc.getSqlGenerator().getSql(expr)).append(",");
			sql.setLength(sql.length() - 1);
			sql.append(")");
		}
		else {
			sql.append(super.getSql());
		}
		return sql.toString();
	}

	@Override
	public WhereDataSet insert(Column column) {
		InsertDataSet ret = (InsertDataSet) this.clone(); 
		if (!ret.targetTable.equals(column.getOwner())){
			throw new DatabaseException("不能同时insert多个表:"+
					ret.targetTable.getName() + "," + column.getOwner().getName());
		}
		ret.__insert(column);
		return ret;
	}
	
	private void __insert(Column column) {
		this.targetTable = column.getOwner();
		this.targetColumns.add(column);
	}
	
	@Override
	public String toString() {
		String ret = "insert into " + this.targetTable + "(" + 
				StringUtils.join(this.targetColumns.toArray(), ",") + ")";
		String relation = super.toString();
		if (relation.equals("[]")) 
			return ret + " values(" + StringUtils.join(super.projects.toArray(), ",") + ")";
		else 
			return ret + " " + relation;
	}
}
