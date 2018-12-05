/**
 * 
 * created on 2007-2-7
 */
package net.ninecube.saturn.database.impl;


import java.util.Set;

import jfun.parsec.ParserException;
import net.ninecube.saturn.database.Column;
import net.ninecube.saturn.database.DatabaseException;
import net.ninecube.saturn.database.jrc.JrcUtil;

import org.codehaus.jrc.predicate.Predicate;
import org.codehaus.jrc.relation.Relation;

/**
 * 
 * @author jxb
 * 
 */
public class TableView extends TableImpl {
	private TableImpl table;
	private Predicate where;
	private String tableName;
	private String whereSql;

	@Override
	public TableImpl postProcess(DatabaseManagerImpl dbm) {
		//this.table = dbm.getTableByAlias(this.tableName);
		//if (this.table == null) throw new DatabaseException("invalid table view: '" + this.tableName + "'");
//		try {
//			this.where = JrcUtil.convertColumn(JrcFactory.getParser().parsePredicate(whereSql), this.table);
//		} catch (ParserException e) {
//			throw new DatabaseException("invalid where: '" + this.whereSql + "'", e);
//		}
//		return super.postProcess(dbm);
		return this;
	}

	@Override
	public Predicate getWhere() {
		return this.where;
	}
	
	//~

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public void setWhereSql(String whereSql) {
		this.whereSql = whereSql;
	}
	
	//~ delegated method

	@Override
	public Relation getTableRelation() {
		return this.table.getTableRelation();
	}

	@Override
	public Set<AssociationImpl> getAssociations() {
		return table.getAssociations();
	}

	@Override
	public Column getTableColumnByAlias(String name) {
		return table.getTableColumnByAlias(name);
	}

	@Override
	public Column getDateColumn() {
		return table.getDateColumn();
	}

	@Override
	public String getName() {
		return table.getName();
	}

	@Override
	public Column getPrimaryKey() {
		return table.getPrimaryKey();
	}

	@Override
	public boolean isBase() {
		return table.isBase();
	}

	@Override
	public boolean isCentric() {
		return table.isCentric();
	}

}
