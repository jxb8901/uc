package net.ninecube.saturn.database.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.ninecube.saturn.database.Association;
import net.ninecube.saturn.database.Column;
import net.ninecube.saturn.database.DatabaseException;
import net.ninecube.saturn.database.MutableTable;
import net.ninecube.saturn.database.Table;
import net.ninecube.saturn.database.exception.ColumnNotFoundException;
import net.ninecube.saturn.database.exception.InvalidAssociationException;
import net.ninecube.util.StringUtil;

import org.apache.log4j.Logger;
import org.codehaus.jrc.predicate.Predicate;
import org.codehaus.jrc.predicate.Predicates;
import org.codehaus.jrc.relation.Relation;
import org.codehaus.jrc.relation.Relations;

public class TableImpl implements MutableTable {
	private static final Logger log = Logger.getLogger(TableImpl.class);
	
	private DatabaseImpl owner;
	private String name;
	private List<String> alias = new ArrayList<String>();
	private List<ColumnImpl> columns = new ArrayList<ColumnImpl>();
	protected Set<AssociationImpl> associations = new LinkedHashSet<AssociationImpl>();
	private Column primaryKey;
	private Column dateColumn;
	private boolean centric = false;
	
	//~ implements Table

	public Column getTableColumnByAlias(String name) {
		int index = name.indexOf(".");
		String columnName = name;
		String subName = null;
		if (index != -1) {
			columnName = name.substring(0, index);
			subName = name.substring(index + 1);
		}
		
		for (ColumnImpl c : columns) {
			if (c.hasAlias(columnName)) {
				if (index == -1) return c;
				// 动态字段
				return new DynaDateColumn(c, subName);
			}
		}

		throw new ColumnNotFoundException(this, name);
	}
	
	public boolean hasAlias(String name) {
		return this.name.equals(name) || this.alias.contains(name);
	}

	public Relation getTableRelation() {
		return Relations.table(this.name);
	}

	public Predicate getWhere() {
		return Predicates.always(true);
	}

	public TableImpl postProcess(DatabaseManagerImpl dbm) {
		for (ColumnImpl c : columns) {
			// 如果在字段上配置关联表和关联字段，则生成关联
			if (!StringUtil.isEmpty(c.getAssociateTable())) {
				buildAssociation(dbm, c);
			}
			// TODO: 复合主键？
			if (c.isPrimaryKey()) this.primaryKey = c;
			if (c.isDateColumn()) this.dateColumn = c;
		}
		checkConfig();
		return this;
	}
	
	protected void checkConfig() {
		if (this.getPrimaryKey() == null) throw new DatabaseException("Primary key must be specified in table '" + this.name + "'");
	}
	
	protected void buildAssociation(DatabaseManagerImpl dbm, ColumnImpl c) {
		if (StringUtil.isEmpty(c.getAssociateTable()))
			throw new DatabaseException("must specify a 'asstable' attr in column: '" + this.name + "." + c.getName() + "'");
		for (String ass : c.getAssociateTable()) {
			try {
				Column oc = dbm.getColumnByAlias(ass);
				this.associations.add(new AssociationImpl(c, oc));
			} catch (DatabaseException e) {
				throw new InvalidAssociationException("invalid association: " + c.getAssociateTable());
			}
		}
	}

	/* getter and setter */

	public List<String> getAlias() {
		return alias;
	}

	public void setAlias(List<String> alias) {
		this.alias = alias;
	}

	public Set<AssociationImpl> getAssociations() {
		return associations;
	}
	
	public void setAssociations(Set<AssociationImpl> as) {
		this.associations = as;
	}

	public List<ColumnImpl> getColumns() {
		return Collections.unmodifiableList(columns);
	}

	public void setColumns(List<ColumnImpl> columns) {
		this.columns = columns;
		for (Column col : this.columns) {
			((ColumnImpl) col).setOwner(this);
		}
	}

	public TableImpl addColumn(Column col) {
		columns.add((ColumnImpl)col);
		((ColumnImpl)col).setOwner(this);
		return this;
	}

	public DatabaseImpl getOwner() {
		return owner;
	}

	public void setOwner(DatabaseImpl owner) {
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String tableName) {
		this.name = tableName;
	}

	public Column getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(Column primaryKey) {
		this.primaryKey = primaryKey;
	}

	public Column getDateColumn() {
		return dateColumn;
	}

	public void setDateColumn(Column dateColumn) {
		this.dateColumn = dateColumn;
		
	}

	public boolean isBase() {
		return getDateColumn() != null;
	}
	
	public void setCentric(boolean centric) {
		this.centric = centric;
	}
	
	public boolean isCentric() {
		return this.centric;
	}

	public ColumnImpl addColumn(String name, String[] alias) {
		return new ColumnImpl(this, name, alias);
	}

	public Association addAssociation(Column self, Column other) {
		AssociationImpl as = new AssociationImpl(self, other);
		this.associations.add(as);
		return as;
	}

	public String toString() {
		return getName();
	}
	
	public boolean equals(Object o) {
		if (o instanceof Table) return ((Table)o).getName().equals(this.getName());
		return false;
	}
	
	public int hashCode() {
		return this.name.hashCode();
	}

}
