/**
 * 
 * created on 2007-3-23
 */
package net.ninecube.saturn.database.jrc;

import net.ninecube.util.ArrayUtil;

import org.codehaus.jrc.ast.BaseAstVisitor;
import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.predicate.Predicate;
import org.codehaus.jrc.relation.OrderElement;
import org.codehaus.jrc.relation.Relation;
import org.codehaus.jrc.relation.Relations;

/**
 * 实现了与Relations.project不同策略的project方法
 * @author jxb
 * 
 */
public class RelationsUtil {
	public static RelationsUtil.MergeStrategy AFTERMERGESTRATEGY = new DefaultMergeStragegy(true);
	public static RelationsUtil.MergeStrategy BEFOREMERGESTRATEGY = new DefaultMergeStragegy(false);

	public static interface MergeStrategy {
		public Expression[] merge(Expression[] inner, Expression[] outter);
	}

	/**
	 * 合并式project，与Relations.project不同的地方是，本方法尽量将本次project操作
	 * 与之前的project操作合并
	 * @see Relations#project(Expression[], Relation)
	 */
	public static Relation mergeProject(Expression expr, Relation relation) {
		return RelationsUtil.mergeProject(new Expression[]{expr}, relation);
	}

	public static class DefaultMergeStragegy implements RelationsUtil.MergeStrategy {
		private boolean after;
		public DefaultMergeStragegy(boolean after) {
			this.after = after;
		}
		public Expression[] merge(Expression[] inner, Expression[] outter) {
			return after? ArrayUtil.merge(inner, outter) : ArrayUtil.merge(outter, inner);
		}
	}
	
	public static Relation mergeProject(final Expression[] exprs, final Relation relation) {
		return mergeProject(AFTERMERGESTRATEGY, exprs, relation);
	}
	
	public static Relation mergeProject(final RelationsUtil.MergeStrategy merger, 
			final Expression[] exprs, final Relation relation) {
		return relation.accept(new BaseAstVisitor<Relation>() {
			@Override
			protected Relation getResult() {
				return Relations.project(exprs, relation);
			}
			
			@Override
			public Relation visitProjection(Expression[] inner_proj, Relation inner) {
				return Relations.project(merger.merge(inner_proj, exprs), inner);
			}

			@Override
			public Relation visitTop(int n, final Relation inner) {
				return Relations.top(n, RelationsUtil.mergeProject(exprs, inner));
			}

			@Override
			public Relation visitNamed(String name, Relation rel) {
				return Relations.alias(name, RelationsUtil.mergeProject(merger, exprs, rel));
			}
			
			@Override
			public Relation visitDistinct(Relation rel) {
				return Relations.distinct(RelationsUtil.mergeProject(merger, exprs, rel));
			}
			
			@Override
			public Relation visitOrderBy(Relation rel, OrderElement[] elements) {
				return Relations.orderBy(RelationsUtil.mergeProject(merger, exprs, rel), elements);
			}
			
			@Override
			public Relation visitWhere(Relation rel, Predicate pred) {
				return Relations.where(RelationsUtil.mergeProject(merger, exprs, rel), pred);
			}
			
			@Override
			public Relation visitGroupBy(Expression[] aggregates, Relation rel, Expression[] by, Predicate having) {
				return Relations.groupBy(merger.merge(aggregates, exprs), rel, by, having);
			}
		});
	}
}
