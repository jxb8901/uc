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
import net.ninecube.saturn.database.AliasDataSet;

import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.relation.OrderElement;
import org.codehaus.jrc.relation.Relation;

/**
 * 支持的操作：insert, where, alias, select(project, groupby, orderby)
 * 不支持的操作：update
 * @author jxb
 * 
 */
public class SelectDataSet extends WhereDataSet {
	protected List<Expression> projects = new ArrayList<Expression>();
	protected List<Expression> groupby =  new ArrayList<Expression>();
	protected Expression orderby;
	protected boolean asc;
	protected boolean distinct = false;
	protected int top;

	public SelectDataSet(WhereDataSet parent) {
		super(parent.getDatabaseManager());
		this.copy(false, parent);
	}
	
	@Override
	protected List<String> getUnsupportedOperations(){
		return Arrays.asList("update");
	}
	
	@Override 
	public AliasDataSet alias(String alias){
		return new AliasDataSetImpl(this, alias);
	}

	
	@Override
	public SelectDataSet project(Expression expr) {
		SelectDataSet ret = (SelectDataSet) this.clone(); 
		ret.importTables(expr);
		ret.projects.add(expr);
		return ret;
	}
	
	@Override
	public SelectDataSet groupby(Expression expr) {
		SelectDataSet ret = (SelectDataSet) this.clone(); 
		ret.importTables(expr);
		ret.groupby.add(expr);
		return ret;
	}
	
	@Override
	public DataSet orderby(Expression orderby, boolean asc, int top) {
		SelectDataSet ret = (SelectDataSet) this.clone(); 
		ret.importTables(orderby);
		ret.orderby = orderby;
		ret.asc = asc;
		ret.top = top;
		return ret;
	}
	
	@Override
	public Relation getRelation() {
		Relation rel = super.getRelation();
		if (rel == null) return null; // if(rel == null) throw new IllegalStateException("Tables must not be empty ! ");
		if (!this.groupby.isEmpty() && !this.projects.isEmpty())
			rel = rel.groupBy(this.projects.toArray(new Expression[0]), this.groupby.toArray(new Expression[0]));
		else if (!this.projects.isEmpty())
			rel = rel.project(this.projects.toArray(new Expression[0]));
		if (this.orderby != null) {
			rel = rel.orderBy(new OrderElement(orderby, this.asc));
		}
		if (this.top != 0) rel = rel.top(top);
		if (this.distinct) rel = rel.distinct();
		return rel;
	}
	
	public SelectDataSet top(int top){
		SelectDataSet ds =  (SelectDataSet)this.clone();
		ds.top = top;
		return ds;
	}
	
	public SelectDataSet distinct(boolean distinct) {
		if(this.distinct == distinct) return this;
		SelectDataSet ds = (SelectDataSet)this.clone();
		ds.distinct = distinct;
		return ds;
	}
}
