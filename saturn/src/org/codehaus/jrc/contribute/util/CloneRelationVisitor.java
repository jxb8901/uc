/**
 * 
 * created on 2007-2-7
 */
package org.codehaus.jrc.contribute.util;

import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.predicate.Predicate;
import org.codehaus.jrc.relation.JoinType;
import org.codehaus.jrc.relation.OrderElement;
import org.codehaus.jrc.relation.Relation;
import org.codehaus.jrc.relation.RelationVisitor;

/**
 * 
 * @author jxb
 * 
 */
public class CloneRelationVisitor extends CloneVisitor implements RelationVisitor<Relation> {
	protected Relation relation;

	public CloneRelationVisitor clone(Relation relation) {
		CloneRelationVisitor ret = (CloneRelationVisitor) super.clone();
		ret.relation = relation;
		return ret;
	}

	public Relation visitDistinct(Relation rel) {
		return cvu.factory.distinct(cvu.vr(rel));
	}

	public Relation visitGroupBy(Expression[] aggregates, Relation rel, Expression[] by, Predicate having) {
		return cvu.factory.groupBy(cvu.ve(aggregates), cvu.vr(rel), cvu.ve(by), cvu.vp(having));
	}

	public Relation visitJoin(Relation rel1, JoinType type, Relation rel2, Predicate on) {
		return cvu.factory.join(cvu.vr(rel1), type, cvu.vr(rel2), cvu.vp(on));
	}

	public Relation visitNamed(String name, Relation rel) {
		return cvu.factory.alias(name, cvu.vr(rel));
	}

	public Relation visitNative(String nativeRelation) {
		return relation;
	}

	public Relation visitOrderBy(Relation rel, OrderElement[] elements) {
		return cvu.factory.orderBy(cvu.vr(rel), vo(elements));
	}

	public Relation visitProjection(Expression[] exprs, Relation rel) {
		return cvu.factory.project(cvu.ve(exprs), cvu.vr(rel));
	}

	public Relation visitTable(String table) {
		return cvu.factory.table(table);
	}

	public Relation visitTop(int n, Relation rel) {
		return cvu.factory.limit(n, cvu.vr(rel));
	}

	public Relation visitUnion(Relation rel1, Relation rel2, boolean all) {
		return cvu.factory.union(cvu.vr(rel1), cvu.vr(rel2), all);
	}

	public Relation visitWhere(Relation rel, Predicate pred) {
		return cvu.factory.where(cvu.vr(rel), cvu.vp(pred));
	}

	public Relation visitCustom(Object custom) {
		return relation;
	}

}
