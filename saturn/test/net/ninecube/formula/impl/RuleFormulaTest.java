/**
 * 
 * created on 2007-3-15
 */
package net.ninecube.formula.impl;

import java.util.Arrays;
import java.util.regex.Matcher;

import net.ninecube.db.DBUtil;
import net.ninecube.formula.BaseFormulaTestCase;
import net.ninecube.formula.Config;
import net.ninecube.formula.Context;
import net.ninecube.formula.Result;
import net.ninecube.formula.Target;
import net.ninecube.formula.TestTargetManager;
import net.ninecube.formula.impl.RuleFormula.FormulaValueResult;
import net.ninecube.lang.FrequenceDate;
import net.ninecube.saturn.RuleEngine;
import net.ninecube.saturn.database.DataSet;
import net.ninecube.saturn.database.DatabaseManager;
import net.ninecube.saturn.database.TableImplTestFixture;
import net.ninecube.saturn.database.impl.WhereDataSet;
import net.ninecube.saturn.impl.RuleEngineImpl;

import org.codehaus.dimple.Implementor;
import org.codehaus.jrc.expression.Aggregate;
import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.expression.Expressions;
import org.codehaus.jrc.relation.Relation;
import org.codehaus.jrc.relation.Relations;

/**
 * 
 * @author jxb
 * 
 */
public class RuleFormulaTest extends BaseFormulaTestCase {
	private RuleEngine re;
	protected TableImplTestFixture fixture = new TableImplTestFixture();
	
	protected void setUp() throws Exception {
		super.setUp();
		fixture.setUp();
		this.re = RuleEngine.getInstance();
		((RuleEngineImpl)this.re).setDatabaseManager(fixture.dbm);
	}
	
	public void testPattern() {
		this.assertTrue(RuleFormula.pattern.matcher("count").matches());
		this.assertTrue(RuleFormula.pattern.matcher("count (a.b)").matches());
		this.assertTrue(RuleFormula.pattern.matcher("sum(a.b)").matches());
		this.assertTrue(RuleFormula.pattern.matcher("sum(a.b) a.m == '1'").matches());
		this.assertTrue(RuleFormula.pattern.matcher("count by(a.c) a.m == '1'").matches());
		this.assertTrue(RuleFormula.pattern.matcher("max   (a.b) by   (a.c) a.m == '1'").matches());
		this.assertTrue(RuleFormula.pattern.matcher("sum(a.b) by(a.c) a.m == '1'").matches());
	}
	
	public void testPatternGroup() {
		Matcher m = RuleFormula.pattern.matcher("sum(a.b) a.m == '1'");
		this.assertTrue(m.matches());
		this.assertEquals(4, m.groupCount());
		this.assertEquals("sum", m.group(1));
		this.assertEquals("a.b", m.group(2));
		this.assertEquals(null, m.group(3));
		this.assertEquals("a.m == '1'", m.group(4));
		
		m = RuleFormula.pattern.matcher("sum(a.b) by(a.c) a.m == '1'");
		this.assertTrue(m.matches());
		this.assertEquals("sum", m.group(1));
		this.assertEquals("a.b", m.group(2));
		this.assertEquals("a.c", m.group(3));
		this.assertEquals("a.m == '1'", m.group(4));
		
		// count
		m = RuleFormula.pattern.matcher("count by(a.c) a.m == '1'");
		this.assertTrue(m.matches());
		this.assertEquals(null, m.group(2));
		this.assertEquals("a.c", m.group(3));
		
		// 省略by关键字
		m = RuleFormula.pattern.matcher("sum(a.b) (a.c) a.m == '1'");
		this.assertTrue(m.matches());
		this.assertEquals("a.c", m.group(3));
	}

	public void testEvalRelation() {
		String r = evaluate("count").selectAll.insertAsDataSet().toString();
		System.out.println(r);
		this.assertEquals("insert into MetricValue(MetricValue.F_Frequence,MetricValue.F_Date," + 
				"MetricValue.F_TargetType,MetricValue.F_TargetId,MetricValue.F_TargetDataID," + 
				"MetricValue.F_MetricId,MetricValue.F_DimensionType,MetricValue.F_Dimension," + 
				"MetricValue.F_QuotaValue) select D,2007-03-20,1,1,*,-1,*,*,count (*) " + 
				"from cust_base where cust_base.sex=女", r);
		
		r = evaluate("count(交易.金额)").selectAll.insertAsDataSet().toString();
		System.out.println(r);
		this.assertEquals("insert into MetricValue(MetricValue.F_Frequence,MetricValue.F_Date," + 
				"MetricValue.F_TargetType,MetricValue.F_TargetId,MetricValue.F_TargetDataID," + 
				"MetricValue.F_MetricId,MetricValue.F_DimensionType,MetricValue.F_Dimension," + 
				"MetricValue.F_QuotaValue) select D,2007-03-20,1,1,*,-1,*,*,count (trans.amount) " + 
				"from cust_base inner join trans on trans.custid=cust_base.id " + 
				"where (cust_base.sex=女 and (trans.transdate>=2007-03-20 and " +
				"trans.transdate<2007-03-21))", r);
		
		r = evaluate("sum(交易.金额) 交易.交易类型 == 'POS'").selectAll.insertAsDataSet().toString();
		System.out.println(r);
		this.assertEquals("insert into MetricValue(MetricValue.F_Frequence,MetricValue.F_Date," + 
				"MetricValue.F_TargetType,MetricValue.F_TargetId,MetricValue.F_TargetDataID," + 
				"MetricValue.F_MetricId,MetricValue.F_DimensionType,MetricValue.F_Dimension," + 
				"MetricValue.F_QuotaValue) select D,2007-03-20,1,1,*,-1,*,*,sum (trans.amount) " + 
				"from cust_base inner join trans on trans.custid=cust_base.id where " + 
				"((cust_base.sex=女 and trans.transtype=POS) and " + 
				"(trans.transdate>=2007-03-20 and trans.transdate<2007-03-21))", r);
		
		r = evaluate("sum(交易.金额) by(交易.交易类型)").groupBy[0].insertAsDataSet().toString();
		System.out.println(r);
		this.assertEquals("insert into MetricValue(MetricValue.F_Frequence,MetricValue.F_Date," + 
				"MetricValue.F_TargetType,MetricValue.F_TargetId,MetricValue.F_TargetDataID," + 
				"MetricValue.F_MetricId,MetricValue.F_DimensionType,MetricValue.F_Dimension," + 
				"MetricValue.F_QuotaValue) select D,2007-03-20,1,1,*,-1,交易.交易类型," + 
				"trans.transtype,sum (trans.amount) from cust_base " + 
				"inner join trans on trans.custid=cust_base.id " + 
				"where (cust_base.sex=女 and (trans.transdate>=2007-03-20 and " + 
				"trans.transdate<2007-03-21)) group by trans.transtype", r);
	}
	
	public void testCustomerMetric(){
		Context context = getContext();
		context.setTarget(getTarget("客户.性别 == '女'",Config.TARGET_TYPE_CUSTOMER));
		RuleFormula f = getFormula("sum(交易.金额) 交易.交易类型 == 'POS'");
		context.setFormula(f);
		RuleFormula.FormulaValueResult dsr = f.getDataSetResult(context);
		log.debug("select all : " + dsr.selectAll);
		this.assertEquals("insert into MetricValue(MetricValue.F_Frequence,MetricValue.F_Date,"+
				"MetricValue.F_TargetType,MetricValue.F_TargetId,MetricValue.F_TargetDataID,"+
				"MetricValue.F_MetricId,MetricValue.F_DimensionType,MetricValue.F_Dimension,"+
				"MetricValue.F_QuotaValue) select D,2007-03-20,Customer,1,cust_base.id,-1,*,*,"+
				"sum (trans.amount) from cust_base inner join trans "+
				"on trans.custid=cust_base.id where ((cust_base.sex=女 and trans.transtype=POS) and "+
				"(trans.transdate>=2007-03-20 and trans.transdate<2007-03-21)) group by cust_base.id", 
				dsr.selectAll.insertAsDataSet().toString());
		this.assertNull(dsr.groupBy);
		

		f = getFormula("sum(交易.金额)  by (交易.交易类型)");
		dsr = f.getDataSetResult(context);
		log.debug("select all : " + dsr.selectAll);
		this.assertEquals("insert into MetricValue(MetricValue.F_Frequence,MetricValue.F_Date,"+
				"MetricValue.F_TargetType,MetricValue.F_TargetId,MetricValue.F_TargetDataID,"+
				"MetricValue.F_MetricId,MetricValue.F_DimensionType,MetricValue.F_Dimension,"+
				"MetricValue.F_QuotaValue) select D,2007-03-20,Customer,1,cust_base.id,-1,*,*,"+
				"sum (trans.amount) from cust_base inner join trans on "+
				"trans.custid=cust_base.id where (cust_base.sex=女 and (trans.transdate>=2007-03-20 "+
				"and trans.transdate<2007-03-21)) group by cust_base.id", dsr.selectAll.insertAsDataSet().toString());
		this.assertEquals(1, dsr.groupBy.length);
		log.debug("group by  : " + dsr.groupBy[0].insertAsDataSet());
		this.assertEquals("insert into MetricValue(MetricValue.F_Frequence,MetricValue.F_Date,"+
				"MetricValue.F_TargetType,MetricValue.F_TargetId,MetricValue.F_TargetDataID,"+
				"MetricValue.F_MetricId,MetricValue.F_DimensionType,MetricValue.F_Dimension,"+
				"MetricValue.F_QuotaValue) select D,2007-03-20,Customer,1,cust_base.id,-1,"+
				"交易.交易类型,trans.transtype,sum (trans.amount) "+
				"from cust_base inner join trans on trans.custid=cust_base.id where (cust_base.sex=女 "+
				"and (trans.transdate>=2007-03-20 and trans.transdate<2007-03-21)) group by "+
				"cust_base.id,trans.transtype", dsr.groupBy[0].insertAsDataSet().toString());
	}
	
	public void testCustomerMetricCal(){
		((RuleEngineImpl)this.re).setDatabaseManager(DatabaseManager.get());
		Context context = getContext();
		context.setTargetDate(new FrequenceDate("D20070323"));
		context.setTarget(getTarget("客户.性别 == '女'",Config.TARGET_TYPE_CUSTOMER));
		RuleFormula f1 = getFormula("客户当日交易额","sum(交易.金额) ");
		context.setFormula(f1);
		fr.register(f1);
		DefaultFormula df = getDefaultFormula("test","客户当日交易额(1000000001)");
		context.getVariables().setArgument(0, "1000000001");
		Result val = df.eval(context);
		log.debug("val : " + val.getValue());
		this.assertEquals(400, val.getValue().intValue());
		val = f1.eval(context);
		this.assertEquals(400, val.getValue().intValue());
		
		
		context.setTarget(getTarget("客户.性别 == '男'",Config.TARGET_TYPE_CUSTOMER));
		f1 = getFormula("客户交易额","sum(交易.金额) by (交易.交易类型)");
		f1.setId(2l);
		context.getVariables().setArgument(0, "1000000003");
		val = f1.eval(context);
		log.debug("val : " + val.getValue() );
		this.assertEquals(200, val.getValue().intValue());
		context.getVariables().setArgument(0, "1000000001");
		val = f1.eval(context);
		this.assertEquals(0, val.getValue().intValue());
		
	}
	
	public void testSqlSyntax() {
		((RuleEngineImpl)this.re).setDatabaseManager(DatabaseManager.get());
		executeRule("count");
		executeRule("count(交易.金额)");
		executeRule("sum(交易.金额) 交易.交易类型 == 'POS'");
		executeRule("sum(交易.金额) by(交易.交易类型)");
	}

	@Deprecated
	public void testGetSelectRelation() {
//		Context context = getContext();
//		context.setFormula(getFormula("count"));
//		DataSet d = getDataSet("knc_cust", false);
//		Relation r = RuleFormula.getSelectRelation(context, d, false).getRelation();
//		System.out.println(r);
//		this.assertEquals("select count (knc_cust.*),*,*,D,2007-03-20,1,1,-1 from knc_cust", r.toString());
//
//		r = RuleFormula.getSelectRelation(context, d, false).getRelation();
//		System.out.println(r);
//		this.assertEquals("在同一个DataSet上调用多次结果应该相同", 
//				"select count (knc_cust.*),*,*,D,2007-03-20,1,1,-1 from knc_cust", r.toString());
//		
//		d =  getDataSet("knc_cust", true);		
//		r = RuleFormula.getSelectRelation(context, d, true).getRelation();
//		System.out.println(r);
//		this.assertEquals("select count (knc_cust.*),knc_cust.dim,dimensionType,D,2007-03-20,1,1,-1 from knc_cust group by knc_cust.dim", r.toString());
	}
	
	private FormulaValueResult evaluate(String formula) {
		RuleFormula f = getFormula(formula);
		Context context = getContext();
		context.setFormula(f);
		return f.getDataSetResult(context);
	}
	
	private void executeRule(String rule) {
		DBUtil.executeUpdate(evaluate(rule).selectAll.insert());
	}
	
	private DataSet getRelation(String tableName, boolean group) {
		Relation r = Relations.table(tableName);
		Expression count = Expressions.aggregate(Aggregate.Count, Expressions.wildcard(null));
		Expression dimension = Expressions.column(tableName, "dim");
		Expression dimensionType = Expressions.literal("dimensionType");
		if (group)
			r = r.groupBy(new Expression[]{count, dimension, dimensionType}, new Expression[]{dimension});
		else
			r = r.project(new Expression[]{count});
		return Implementor.proxy(DataSet.class, new Object() {
			
		});
	}
	
	private DataSet getDataSet(String tableName, boolean group) {
		DataSet ds = new WhereDataSet(dbm);
		Expression count = Expressions.aggregate(Aggregate.Count, Expressions.wildcard(tableName));
		Expression dimension = Expressions.column(tableName, "dim");
		Expression dimensionType = Expressions.literal("dimensionType");
		if (group)
			ds = ds.project(count).project(dimension).project(dimensionType).groupby(dimension);
		else
			ds = ds.project(count);		
		System.out.println("&&&&& tablename " + tableName);
		return ds;
	}
	
	private Context getContext() {
		Context context  = new Context();
		context.setTarget(getTarget("客户.性别 == '女'"));
		context.setTargetDate(new FrequenceDate("D20070320"));
		return context;
	}

	private RuleFormula getFormula(String formula) {
		return getFormula("test", formula);
	}
	
	private RuleFormula getFormula(String name, String formula) {
		return new RuleFormula(fr, re, -1L, name, formula);
	}
	
	private DefaultFormula getDefaultFormula(String name, String formula){
		return new DefaultFormula(fr, -1L, name, formula);
	}
	
	private Target getTarget(String rule) {
		return getTarget(rule,"1");
	}
	
	private Target getTarget(String rule, String type) {
		return TestTargetManager.getTarget(1L, type, rule, this.re.getFilter(rule));
	}
	
	public static void main(String[] a) throws Exception {
		System.out.println(Arrays.asList("a.b, a.c      ,    a.*   ".split("[, ]+")));
	}
}
