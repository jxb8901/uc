/**
 * 
 */
package net.ninecube.saturn.database.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.ninecube.saturn.database.AliasDataSet;
import net.ninecube.saturn.database.Column;
import net.ninecube.saturn.database.DataSet;
import net.ninecube.saturn.database.DatabaseException;
import net.ninecube.saturn.database.DatabaseManager;
import net.ninecube.saturn.database.Table;
import net.ninecube.saturn.database.jrc.JrcUtil;

import org.apache.log4j.Logger;
import org.codehaus.jrc.contribute.Jrc;
import org.codehaus.jrc.contribute.util.GetTablesVisitorUtil;
import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.predicate.BoolOperator;
import org.codehaus.jrc.predicate.LogicalOperator;
import org.codehaus.jrc.predicate.Predicate;
import org.codehaus.jrc.predicate.Predicates;
import org.codehaus.jrc.relation.Relation;

/**
 * @author Fox
 * 
 */
public class WhereDataSet implements DataSet, Cloneable {
	private static final Logger log = Logger.getLogger(WhereDataSet.class);

	protected DatabaseManager databaseManager;
	protected Set<Table> tables = new LinkedHashSet<Table>();
	protected Date[] dateRange;
	protected Predicate pred = Predicates.always(true) ;	

	public WhereDataSet(DatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}
	
	public WhereDataSet(WhereDataSet other){
		this.copy(false, other);
	}
	
	public WhereDataSet merge(DataSet other) {
		checkMerge(other);
		return this.clone().copy(false, other);
	}

	protected void checkMerge(DataSet other){
	}

	@SuppressWarnings("unchecked")
	protected WhereDataSet copy(boolean isClone, Object other){
		Class base = this.getClass();
		while(!base.isAssignableFrom(other.getClass())) base = base.getSuperclass();
		for(Class c =base ; !c.equals(Object.class) ; c = c.getSuperclass()){
			for(Field fld : c.getDeclaredFields()){
				try{
					if(Modifier.isFinal(fld.getModifiers()) || Modifier.isStatic(fld.getModifiers())) continue;
					fld.setAccessible(true);
					fld.set(this,copy(isClone, fld.get(this),fld.get(other)));
				}catch(Exception exc){log.error(exc.getMessage(), exc);}
			}
		}
		return this;
	}
	
	@SuppressWarnings("unchecked")
	protected Object copy(boolean isClone, Object trgVal, Object srcVal){
		if(trgVal instanceof Collection){
			if(isClone){
				try{
					trgVal= srcVal.getClass().newInstance();
				}catch(Exception exc){throw new IllegalStateException(exc.getMessage());}
			}
			((Collection)trgVal).addAll((Collection)srcVal);
			return trgVal;
		}else if(trgVal instanceof Predicate){
			if(!isClone) return JrcUtil.and((Predicate)trgVal, (Predicate)srcVal);
		}
		return srcVal;
	}
	
	
	/**
	 * 实现深拷贝
	 */
	@Override
	public WhereDataSet clone() {
		 try{
			 return ((WhereDataSet)super.clone()).copy(true, this);
		}catch(Exception exc){
			throw new DatabaseException(exc.getMessage());
		}
	}
	
	protected List<String> getUnsupportedOperations(){
		return Arrays.asList("alias");
	}
	
	private void checkOperation(String operation){
		if(getUnsupportedOperations().contains(operation))
			throw new UnsupportedOperationException(
					"unsupporte " + operation +  " on " + 
					this.getClass().getName() + "!"); 
	}

	public AliasDataSet alias(String alias) {
		throw new UnsupportedOperationException("unsupporte alias on Where !");
	}
	
	public String getSql(String sqltype) {
		String oldType = Jrc.setDefaultSqlGeneratorName(sqltype);
		String ret = this.getSql();
		Jrc.setDefaultSqlGeneratorName(oldType);
		return ret;
	}
	
	public String getSql() {
		return Jrc.getSqlGenerator().getSql(this.getRelation());
	}
	
	public WhereDataSet insert(Column column) {
		checkOperation("insert");
		return new InsertDataSet(this, column);
	}
	public WhereDataSet update(Column column, Expression value) {
		checkOperation("update");
		return new UpdateDataSet(this, column, value).merge(this);
	}
	
	public SelectDataSet project(Expression expr) {
		checkOperation("project");
		return (SelectDataSet)new SelectDataSet(this).project(expr);
	}
	public SelectDataSet groupby(Expression expr) {
		checkOperation("groupby");
		return (SelectDataSet)new SelectDataSet(this).groupby(expr);
	}
	public DataSet orderby(Expression orderby, boolean isAsc, int top) {
		checkOperation("orderby");
		return new SelectDataSet(this).orderby(orderby, isAsc, top);
	}
	public WhereDataSet where(Predicate pred) {
		WhereDataSet ret = this.clone();
		ret.checkOperation("where");
		ret.importTables(pred);
		ret.pred = JrcUtil.and(ret.pred, pred);
		return ret;
	}

	public Set<Table> getTables() {
		return tables;
	}

	protected List<? extends Table> getTableByAlias(String name) {
		return databaseManager.getTableByAlias(name);
	}

	public Column getColumnByAlias(String name) {
		return databaseManager.getColumnByAlias(name);
	}

	public DataSet[] split(Predicate pred) {
		DataSet[] result = new DataSet[2];
		log.debug("pred '" + pred + "'");

		result[0] = this.clone().where(pred);
		result[1] = this.clone().where(Predicates.buildNot(pred));

		return result;
	}
	
	protected void importTables(Object jrcRel){
		// TODO: 这里引用表时不应该包括子查询中的表
		Set<String> tableNames = null;
		if(jrcRel instanceof Relation){
			 tableNames = GetTablesVisitorUtil.getImportedTables((Relation)jrcRel);
		}else if (jrcRel instanceof Predicate){
			 tableNames = GetTablesVisitorUtil.getImportedTables((Predicate)jrcRel);
		}else {
			 tableNames = GetTablesVisitorUtil.getImportedTables((Expression)jrcRel);
		}
		for (String name : tableNames) this.tables.addAll(this.getTableByAlias(name));
	}
	
	public boolean isEmpty() {
		return this.tables.isEmpty();
	}

	public Relation getRelation() {
		Relation rel = JrcUtil.joinTables(tables); 
		if (rel == null) return null;
		rel = JrcUtil.where(rel, getCondition());
		return rel;
	}
	
	protected Predicate getCondition(){
		Predicate p = JrcUtil.and(pred, JrcUtil.andWhere(tables));
		return JrcUtil.and(p, getDatePredite());
	}
	
	protected Predicate getDatePredite() {
		Predicate prd = Predicates.always(true);
		if (dateRange == null) return prd;
		Table tb = getBaseTable();
		if(tb == null ) return prd;
		Column datecol = tb.getDateColumn();
		log.debug("base : " + tb +" ; datecolumn : " + datecol);
		Expression exp;
		exp = datecol.getSqlCode();
		log.debug(" date range : " + dateRange[0] +" , " + dateRange[1]);
		prd = Predicates.comparison(exp, BoolOperator.Ge, datecol.getSqlValue(dateRange[0]),false);
		Predicate prd2 = Predicates.comparison(exp, BoolOperator.Lt, datecol.getSqlValue(dateRange[1]),false);
		prd = Predicates.binary(prd, LogicalOperator.And, prd2);

		return prd;
	}

	public String toString() {
		if (!this.tables.isEmpty()) {
			Relation r = this.getRelation();
			if (r != null) return r.toString();
		}
		return "[]";
	}

	public Date[] getDateRange() {
		return dateRange;
	}

	public void setDateRange(Date[] dateRng) {
		if (dateRng == null)
			return;
		// assert dateRng != null : "Attemp to set null date range to dateset
		// !";
		this.dateRange = new Date[2];
		this.dateRange[0] = new java.sql.Date(dateRng[0].getTime());
		this.dateRange[1] = new java.sql.Date(dateRng[1].getTime());

	}
	
	public Table getBaseTable() {
		for(Table t : tables){
			if(t.isBase()) return t;
		}
		return null;
	}

	public Table getCentricTable() {
		for(Table tb : tables){
			if(tb.isCentric()){
				return tb;
			}
		}
		return this.databaseManager.getCentricTable();
	}

	protected DatabaseManager getDatabaseManager() {
		return databaseManager;
	}
}
