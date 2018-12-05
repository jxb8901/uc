/**
 * 
 * created on 2007-2-8
 */
package net.ninecube.saturn.database.jrc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.ninecube.saturn.database.Association;
import net.ninecube.saturn.database.Column;
import net.ninecube.saturn.database.DataSetUtil;
import net.ninecube.saturn.database.DatabaseException;
import net.ninecube.saturn.database.Table;

import org.apache.log4j.Logger;
import org.codehaus.jrc.ast.BaseAstVisitor;
import org.codehaus.jrc.contribute.util.CloneExpressionVisitor;
import org.codehaus.jrc.contribute.util.ClonePredicateVisitor;
import org.codehaus.jrc.contribute.util.CloneVisitorUtil;
import org.codehaus.jrc.contribute.util.JrcVisitorSupport;
import org.codehaus.jrc.contribute.util.ReflectTreeWalker;
import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.predicate.BoolOperator;
import org.codehaus.jrc.predicate.Predicate;
import org.codehaus.jrc.predicate.Predicates;
import org.codehaus.jrc.relation.JoinType;
import org.codehaus.jrc.relation.Relation;
import org.codehaus.jrc.relation.Relations;

/**
 * 
 * @author jxb
 * 
 */
public class JrcUtil {

	private static Logger log = Logger.getLogger(JrcUtil.class);
	
	public static Relation where(Relation r, Predicate p) {
		if (p == null || Predicates.always(true).equals(p)) return r;
		return Relations.where(r, p);
	}
	
	public static Predicate and(Predicate p1, Predicate p2) {
		if ((p1 == null || Predicates.always(true).equals(p1)) && 
				(p2 == null || Predicates.always(true).equals(p2))) 
			return Predicates.always(true);
		if (p1 == null || Predicates.always(true).equals(p1)) return p2;
		if (p2 == null || Predicates.always(true).equals(p2)) return p1;
		if(p1.equals(p2)) return p1;
		return Predicates.and(p1, p2);
	}
	
	public static Predicate or(Predicate p1, Predicate p2){
		if(p1 == null) p1 = Predicates.always(true);
		if(p2 == null) p1 = Predicates.always(true);
		if(Predicates.always(true).equals(p1)) return p2;
		if(Predicates.always(true).equals(p2)) return p1;
		return Predicates.or(p1, p2);
	}
	
	public static Predicate andWhere(Collection<? extends Table> tables) {
		Predicate ret = Predicates.always(true);
		for (Table t : tables) {
			ret = and(ret, t.getWhere());
		}
		return ret;
	}
	
	public static Relation joinTables(Collection<? extends Table> tables) {
		if (tables.isEmpty()) return null;
		Relation rel = null;
		rel = joinTables(rel, new ArrayList<Table>(), tables);
		return rel;
	}

	/**
	 * 将给定的Relation及给定的一组Table关联生成一个新的JoinRelation
	 * @param prerel 初始Relation，生成后的JoinRelation基于该Relation而生成
	 * @param preTables 初始Relation中涉及的Table，避免同一个Table再次关联
	 * @param tables 需要关联的Table
	 * @return 返回关联Relaion，不包括各Table的where从句
	 */
	private static Relation joinTables(Relation prerel, Collection<? extends Table> preTables, 
			Collection<? extends Table> tables) {
		if (prerel == null && preTables.size() != 0) {
			throw new IllegalArgumentException("The preTables must be empty if the prrelation is null !");
		}
		log.debug("%%%%" + preTables);
		for (Table t : preTables)
			log.debug("%%%%" + t.getAssociations());
		log.debug("####" + tables);
		for (Table t : tables)
			log.debug("====" + t.getAssociations());
		List<Table> caltbs = new ArrayList<Table>();
		caltbs.addAll(preTables);
		List<Table> remtbs = new ArrayList<Table>();
		remtbs.addAll(tables);

		if (remtbs.size() == 0) return prerel;
		if (caltbs.size() == 0) { // prerel must be null
			Table tb = remtbs.remove(0);
			prerel = tb.getTableRelation();
			caltbs.add(tb);
		}

		OUTERLOOP: for (int i = 0; i < caltbs.size(); i++) {
			Table tb = caltbs.get(i);
			log.debug("current caltb : " + tb);
			for (Association as : DataSetUtil.getAssociations(tb, remtbs)) {
				Table antb = as.getOppositeTable(tb);
				log.debug("associated tb : " + antb + " ; remained tbs : " + remtbs + " ; build :  " + remtbs.contains(antb));
				if (remtbs.remove(antb)) {
					if (caltbs.contains(antb)) continue;
					caltbs.add(antb);
					Predicate pred = null;
					for (Association as1 : DataSetUtil.getAssociations(antb, caltbs)) {
						pred = and(pred, as1.getJoinOnPredicate());
					}
					prerel = Relations.join(prerel, JoinType.Inner, antb.getTableRelation(), pred);
					if (remtbs.size() == 0) break OUTERLOOP;
				}
			}
		}
		for (int i = remtbs.size() - 1; i >= 0; i--) {
			if (caltbs.contains(remtbs.get(i))) remtbs.remove(i);
		}
		if (remtbs.size() > 0) {
			throw new DatabaseException("Can't build relation for tables " + remtbs + " with the tables " + caltbs + " ! ");
		}
		return prerel;
	}
	
	public static Set<String> getTablesInFromClause(Object obj) {
		JrcVisitorSupport<Set<String>> v = new JrcVisitorSupport<Set<String>>() {
			private Set<String> tables = new HashSet<String>();
			@Override
			public Set<String> visitTable(String table) {
				this.tables.add(table);
				return super.visitTable(table);
			}
			@Override
			public Set<String> getResult() {
				return this.tables;
			}
		};
		new ReflectTreeWalker(v).visit(obj);
		return v.getResult();
	}

	public static Predicate convertColumn(Predicate p, Table table) {
		return getVistiorUtil(table).vp(p);
	}

	public static Expression convertColumn(Expression e, Table table) {
		return getVistiorUtil(table).ve(e);
	}

	private static CloneVisitorUtil getVistiorUtil(Table table) {
		CloneVisitorUtil ret = CloneVisitorUtil.get();
		ret.setExpressionVisitorPrototype(new ColumnExpressionVisitor(table));
		return ret;
	}

	public static class ColumnExpressionVisitor extends CloneExpressionVisitor {
		private Table table;

		public ColumnExpressionVisitor(Table table) {
			this.table = table;
		}

		public Expression visitColumn(String t, String col) {
			Column c = table.getTableColumnByAlias(col);
			if (c == null)
				throw new DatabaseException("Invalid column name: '" + col
						+ "' in table : '" + table.getName() + "'");
			return c.getSqlCode();
		}
	}

}
