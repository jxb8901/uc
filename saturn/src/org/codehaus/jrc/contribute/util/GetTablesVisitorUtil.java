/**
 * 
 * created on 2007-4-10
 */
package org.codehaus.jrc.contribute.util;

import java.util.HashSet;
import java.util.Set;

import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.predicate.Predicate;
import org.codehaus.jrc.relation.Relation;

/**
 * 
 * @author jxb
 * 
 */
public class GetTablesVisitorUtil extends JrcVisitorSupport<Set<String>> {
	private Set<String> tables = new HashSet<String>();
	
	public static Set<String> getImportedTables(Relation rel){
		GetTablesVisitorUtil visitor = new GetTablesVisitorUtil();
		new ReflectTreeWalker(visitor).visit(rel);
		return visitor.getResult();
	}
	
	public static Set<String> getImportedTables(Expression expr){
		GetTablesVisitorUtil visitor = new GetTablesVisitorUtil();
		new ReflectTreeWalker(visitor).visit(expr);
		return visitor.getResult();
	}
	
	public static Set<String> getImportedTables(Predicate pred){
		GetTablesVisitorUtil visitor = new GetTablesVisitorUtil();
		new ReflectTreeWalker(visitor).visit(pred);
		return visitor.getResult();
	}

	@Override
	public Set<String> visitColumn(String table, String col) {
		tables.add(table);
		return this.getResult();
	}

	@Override
	public Set<String> visitWildcard(String table) {
		tables.add(table);
		return this.getResult();
	}

	@Override
	public Set<String> visitTable(String table) {
		tables.add(table);
		return this.getResult();
	}

	@Override
	public Set<String> getResult() {
		return this.tables;
	}
}
