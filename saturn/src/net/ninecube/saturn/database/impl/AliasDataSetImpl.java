/**
 * 
 * created on 2007-4-12
 */
package net.ninecube.saturn.database.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import net.ninecube.saturn.database.Association;
import net.ninecube.saturn.database.Column;
import net.ninecube.saturn.database.Database;
import net.ninecube.saturn.database.MutableTable;
import net.ninecube.saturn.database.AliasDataSet;
import net.ninecube.saturn.database.Table;
import net.ninecube.saturn.database.exception.ColumnNotFoundException;
import net.ninecube.util.StringUtil;

import org.codehaus.jrc.predicate.Predicate;
import org.codehaus.jrc.relation.Relation;

/**
 * 不支持的操作：alias
 * @author jxb
 * 
 */
public class AliasDataSetImpl extends WhereDataSet implements AliasDataSet {

	private SelectDataSet parent;
	private TableImpl delegate;

	public AliasDataSetImpl(SelectDataSet parent, String tableName) {
		super(parent.getDatabaseManager());
		this.parent = parent;
		this.delegate = newTable();
		this.delegate.setName(tableName);
		this.tables.add(this.delegate);
	}
	
	public TableImpl getTable(){
		return this.delegate;
	}

	@Override
	public List<? extends Table> getTableByAlias(String name) {
		if (this.delegate.hasAlias(name)) return Arrays.asList(this.delegate);
		return super.getTableByAlias(name);
	}

	@Override
	public Column getColumnByAlias(String name) {
		try {
			if (this.delegate.hasAlias(StringUtil.getFirstByDot(name)))
				return this.delegate.getTableColumnByAlias(StringUtil.getSecondByDot(name));
			else
				return this.delegate.getTableColumnByAlias(name);
		} catch (ColumnNotFoundException e) {
			return super.getColumnByAlias(name);
		}
	}
	
	/////
	private TableImpl newTable(){
		return new TableImpl(){
			@Override
			public Association addAssociation(Column self, Column other) {
				Association nas = super.addAssociation(self, other);
				// 自动添加所有与other相关的关系
				for (Association as : getDatabaseManager().getAssociations(nas.getOppositeTable(this))) {
					if (as.getColumn(nas.getOppositeTable(this)).equals(nas.getOppositeColumn(this))) {
						super.addAssociation(nas.getColumn(this), as.getOppositeColumn(nas.getOppositeTable(this)));
					}
				}
				return nas;
			}

			@Override
			public Relation getTableRelation() {
				return parent.getRelation().alias(this.getName());
			}
		};
	}
	
}
