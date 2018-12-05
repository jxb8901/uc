/**
 * 
 * created on 2007-4-12
 */
package net.ninecube.saturn.database;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.ninecube.saturn.database.exception.ColumnNotFoundException;
import net.ninecube.saturn.database.impl.WhereDataSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jrc.ast.BaseAstVisitor;
import org.codehaus.jrc.expression.Aggregate;
import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.expression.Expressions;

/**
 * 
 * @author jxb
 * 
 */
public class DataSetUtil {
	private static Log log = LogFactory.getLog(DataSetUtil.class);
	private static String KEY_RECORD_NUM = "recordNum";
	private static String KEY_AGG_COL = "aggCol";
	protected static int aliasRefCout = 0;
	
	public static final String EXPOSED_FIELD_RECORD_NUM = "笔数";

	private static String getAlias1() {
		return "table" + ++aliasRefCout;
	}
	
	/**
	 * 为指定WehereDataSet生成一个AliasDataSet
	 * 注: 该AliasDataSet会自动与CentricTable建一个关联.
	 * @param wds
	 * @param colNames 生成的AliasDataSet暴露的字段，第一个字段为主键。
	 * @param alias 生成的AliasDataSet暴露字段的别名,
	 * @return
	 */
	public static AliasDataSet alias(DataSet wds, Expression[] colNames, String[][] alias){
		DataSet ds = wds;
		for(int i = 0 ; i < colNames.length ; i++) ds = ds.project(Expressions.alias(alias[i][0], colNames[i]));
		AliasDataSet ads = ds.alias(getAlias1());
		for(String[] an : alias) ads.getTable().addColumn(an[0], an);
		Column primaryKey = ads.getTable().getTableColumnByAlias(alias[0][0]);
		ads.getTable().addAssociation(primaryKey, ds.getCentricTable().getPrimaryKey());
		log.debug("**ass : " + ads.getTable().getAssociations());
		
		return ads;
	}
	
	public static AliasDataSet alias(DataSet wds , String[] innerCols, String[] alias){
		Expression[] cols = new Expression[alias.length];
		String[][] fa = new String[alias.length][1];
		for(int i = 0 ; i < alias.length ; i++){
			cols[i] = wds.getColumnByAlias(innerCols[i]).getSqlCode();
			fa[i][0] = alias[i];
		}
		return alias(wds, cols, fa);
	}
	
	/**
	 * 实现SQL的聚焦函数(Sum,Count)，如果参数aggcol为空则仅生成Count，
	 * 否则生成Sum和Count
	 * 在返回的DataSet中创建了虚拟表，aggRefName是该表中的一个字段
	 */
	public static AliasDataSet aggregate(DataSet parent, Expression aggcol, String aggRefName) {
		Column primaryKey = parent.getCentricTable().getPrimaryKey();
		Expression primaryExpr = primaryKey.getSqlCode();
		parent = parent.project(Expressions.alias(primaryKey.getName(), primaryExpr));
		parent = parent.project(Expressions.alias(KEY_RECORD_NUM, 
				Expressions.aggregate(Aggregate.Count, primaryExpr)));
		if (aggcol != null) {
			parent = parent.project(Expressions.alias(KEY_AGG_COL, 
					Expressions.aggregate(Aggregate.Sum, aggcol)));
		}
		parent = parent.groupby(primaryExpr);
		
		AliasDataSet ret = parent.alias(getAlias1());
		ret.getTable().addColumn(KEY_RECORD_NUM, new String[]{KEY_RECORD_NUM, EXPOSED_FIELD_RECORD_NUM});
		if (aggcol != null)
			ret.getTable().addColumn(KEY_AGG_COL, new String[]{KEY_AGG_COL, aggRefName});
		ret.getTable().setPrimaryKey(ret.getTable().addColumn(primaryKey.getName(), new String[]{}));
		ret.getTable().addAssociation(ret.getTable().getPrimaryKey(), primaryKey);
		
		return ret;
	}
	
	public static DataSet orderby(DataSet parent, Expression orderby, boolean asc, int top) {
		Column primaryKey = getColumn(parent, orderby).getOwner().getPrimaryKey();
		parent = parent.project(Expressions.alias(primaryKey.getName(), primaryKey.getSqlCode()));
		parent = parent.orderby(orderby, asc, top);
		
		AliasDataSet ret = parent.alias(getAlias1());
		ret.getTable().setPrimaryKey(ret.getTable().addColumn(primaryKey.getName(), new String[]{}));
		ret.getTable().addAssociation(ret.getTable().getPrimaryKey(), primaryKey);
		
		return ret;
	}

	public static Column getColumn(final DataSet ds, Expression expr){
		List<Column> cols = getColumns(ds, expr);
		if(cols.size()==0) throw new ColumnNotFoundException("" + expr);
		return cols.get(0);
	}

	public static List<Column> getColumns(final DataSet ds, Expression expr){
		final Set<Column> cols = new LinkedHashSet<Column>();
		expr.accept(new BaseAstVisitor<Column>() {
			protected Column getResult() {
				 return null;
			}

			public Column visitColumn(String table, String col) {
				Column column = ds.getColumnByAlias(table+"."+col);
				cols.add(column);
				return column;
			}

		});
		return new ArrayList<Column>(cols);
	}

	public static List<Association> getAssociations(Table tb, List<? extends Table> trgtbs) {
		List<Association> ass = new ArrayList<Association>();
		for (Association as : tb.getAssociations()) {
			if (trgtbs.contains(as.getOppositeTable(tb)) && !ass.contains(as))
				ass.add(as);
		}
		for (Table tb1 : trgtbs) {
			for (Association as : tb1.getAssociations()) {
				if (tb.equals((as.getOppositeTable(tb1))) && !ass.contains(tb1))
					ass.add(as);
			}
		}
		return ass;
	}
	
	public static Column getColumnOfAssociaion(Column one) {
		for (Association as : one.getOwner().getAssociations()) {
			Column ret = as.getOppositeColumn(one);
			if (ret != null) return ret;
		}
		return null;
	}
}
