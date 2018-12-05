/**
 * 
 */
package net.ninecube.saturn.database.impl;

import net.ninecube.saturn.database.Association;
import net.ninecube.saturn.database.Column;
import net.ninecube.saturn.database.Table;

import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.predicate.BoolOperator;
import org.codehaus.jrc.predicate.Predicate;
import org.codehaus.jrc.predicate.Predicates;

/**
 * @author Fox
 *
 */
public class AssociationImpl implements Association {
	private Column c1;
	private Column c2;

	public AssociationImpl(Column column1, Column column2) {
		super();
		this.c1 = column1;
		this.c2 = column2;
	}
	
	public Column getOppositeColumn(Column one) {
		if (one.getFullName().equals(c1.getFullName())) return c2;
		if (one.getFullName().equals(c2.getFullName())) return c1;
		return null;
	}
	
	public Predicate getJoinOnPredicate() {
		Expression expr1 = this.c1.getSqlCode();
		Expression expr2 = this.c2.getSqlCode();
		return Predicates.comparison(expr1, BoolOperator.Eq, expr2, false);
	}

	public Table getOppositeTable(Table table) {
		return table.equals(this.c1.getOwner()) ? this.c2.getOwner() : this.c1.getOwner() ;
	}
	
	public Column getColumn(Table table) {
		return table.equals(this.c1.getOwner()) ? this.c1 : this.c2;
	}

	public Column getOppositeColumn(Table table) {
		return table.equals(this.c1.getOwner()) ? this.c2 : this.c1;
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof AssociationImpl)) return false;
		AssociationImpl a = (AssociationImpl) o;
		return (a.c1.equals(this.c1) && a.c2.equals(this.c2) ||
				a.c2.equals(this.c1) && a.c1.equals(this.c2));
	}
	
	public int hashCode() {
		return c1.hashCode() * 31 + c2.hashCode();
	}
	
	public String toString() {
		return getJoinOnPredicate().toString();
	}
}
