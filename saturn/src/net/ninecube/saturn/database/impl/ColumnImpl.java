/**
 * 
 */
package net.ninecube.saturn.database.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import net.ninecube.saturn.database.Column;
import net.ninecube.saturn.database.MutableTable;
import net.ninecube.saturn.database.Table;
import net.ninecube.saturn.database.jrc.JrcUtil;
import net.ninecube.util.DateUtil;
import net.ninecube.util.EnumerManager;
import net.ninecube.util.StringUtil;

import org.codehaus.jrc.contribute.Jrc;
import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.expression.Expressions;

/**
 * @author Fox
 * 
 */
public class ColumnImpl implements Column {

	public static enum Type {
		general, enumer, formula, dyna, formulaenumer/*TODO: 重构*/;
	}

	public enum SqlType {
		varchar, date, datetime, time, integer, bigint, autoint, decimal;
	}

	private MutableTable owner;
	private String name;
	private SqlType sqlType;
	private Type type;
	private String pattern="yyyy-MM-dd";
	private String value;
	private String formula;
	private String enumer;
	private int len = -1;
	private List<String> alias;
	private boolean isDateColumn = false;
	private boolean isPrimaryKey = false;
	private List<String> associateTable = new ArrayList<String>();

	public ColumnImpl() {
	}

	public ColumnImpl(MutableTable owner, String name, String[] alias) {
		this(owner, name, Arrays.asList(alias));
	}

	public ColumnImpl(MutableTable owner, String name, List<String> alias) {
		this.owner = owner;
		this.name = name;
		this.alias = new ArrayList<String>(alias);
		owner.addColumn(this);
	}

	// ~

	public Expression getSqlCode() {
		if (!StringUtil.isEmpty(this.formula)) {
			return JrcUtil.convertColumn(Jrc.getParser().parseExpression(this.formula), this.owner);
		}
		return Expressions.column(owner.getName(), getName());
	}

	public Expression getSqlValue(Object val) {
		if (val instanceof Expression) {
			return (Expression) val;
		}else if(val instanceof Date){
			if (this.sqlType == SqlType.date) {
				return Expressions.literal(DateUtil.format((Date)val, "yyyy-MM-dd"));
			}
			return Expressions.literal(DateUtil.format((Date)val, "yyyy-MM-dd"));
		}
		if (!StringUtil.isEmpty(this.enumer)) {
			return Expressions.literal(EnumerManager.get().getValue(this.enumer,
					(String) val));
		}
		return Expressions.literal(val);
	}

	public boolean hasAlias(String name) {
		return this.name.equals(name) || this.alias.contains(name);
	}

	public String getFullName() {
		return owner.getName() + "." + getName();
	}

	// ~ getter and setter

	public String getName() {
		return name;
	}

	public Type getType() {
		return type;
	}

	public List<String> getAlias() {
		return alias;
	}

	public void setAlias(List<String> alias) {
		this.alias = alias;
	}

	public Table getOwner() {
		return owner;
	}

	public void setOwner(MutableTable owner) {
		this.owner = owner;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String toString() {
		return getOwner().getName() + "." + getName();
	}

	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}

	public void setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}

	public SqlType getSqlType() {
		return sqlType;
	}

	public void setSqlType(SqlType sqltype) {
		this.sqlType = sqltype;
	}

	public List<String> getAssociateTable() {
		return associateTable;
	}

	public void setAssociateTable(List<String> associateTable) {
		this.associateTable = associateTable;
	}

	public boolean isDateColumn() {
		return isDateColumn;
	}

	public void setDateColumn(boolean isDateColumn) {
		this.isDateColumn = isDateColumn;
	}

	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}

	public String getEnumer() {
		return enumer;
	}

	public void setEnumer(String enumer) {
		this.enumer = enumer;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean equals(Object o) {
		if (!(o instanceof ColumnImpl)) return false;
		ColumnImpl c = (ColumnImpl) o;
		return c.getOwner().equals(this.owner) && c.getName().equals(this.name);
	}
	
	public int hashCode() {
		return this.owner.hashCode() * 31 + this.name.hashCode();
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

}
