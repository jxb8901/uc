package net.ninecube.formula.ruleadapter;


import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.ninecube.formula.Config;
import net.ninecube.formula.FormulaResolver;
import net.ninecube.formula.TargetManager;
import net.ninecube.formula.TestTargetManager;
import net.ninecube.formula.impl.DefaultTarget;
import net.ninecube.formula.impl.MapFormulaResolver;
import net.ninecube.formula.ruleadapter.MetricFunction;
import net.ninecube.lang.FrequenceDate;
import net.ninecube.saturn.Context;
import net.ninecube.saturn.RuleEngine;
import net.ninecube.saturn.database.Column;
import net.ninecube.saturn.database.DataSet;
import net.ninecube.saturn.database.DataSetUtilTestBase;
import net.ninecube.saturn.database.DatabaseManager;
import net.ninecube.saturn.database.Table;
import net.ninecube.saturn.function.Function;
import junit.framework.TestCase;

public class MetricFunctionTest extends DataSetUtilTestBase {
	private Log log = LogFactory.getLog(MetricFunctionTest.class);
	private DatabaseManager dbm = DatabaseManager.get();
	private MapFormulaResolver resolver ;
	private TestTargetManager targetMng; 
	private MetricFunctionProviderImpl funProvider;
	
	public void setUp(){
		super.setUp();
		resolver = new MapFormulaResolver();
		resolver.setRuleEngine(RuleEngine.getInstance());
		resolver.register(1l,"POS交易笔数",Config.TARGET_TYPE_CUSTOMER,"count(交易.交易金额)");
		targetMng = new TestTargetManager();
		targetMng.addTarget(new DefaultTarget(1l,"个人客户",Config.TARGET_TYPE_CUSTOMER,"",null));
		funProvider = new MetricFunctionProviderImpl(resolver, targetMng);
	}
	
	public void testExecute(){
		
		Function mf = funProvider.getByName("个人客户.POS交易笔数").getFunction() ;//new MetricFunction(dbm, "1" , "个人客户" , "POS交易笔数");
		Context context = createContext();
		Column col = (Column)mf.execute(context, new ArrayList(), new HashMap());
		this.assertEquals("table1.formula_value", col.getFullName());
		log .debug("sql : " + context.getDataSet());
		this.assertEquals("select target_data_id=MetricValue.F_TargetDataID,formula_value="+
				"MetricValue.F_QuotaValue from MetricValue where ((((((MetricValue.F_Frequence=D and "+
				"MetricValue.F_Date=2007-02-08) and MetricValue.F_TargetType=Customer) and "+
				"MetricValue.F_TargetId=1) and MetricValue.F_DimensionType=*) and "+
				"MetricValue.F_Dimension=*) and MetricValue.F_MetricId=1) AS table1",context.getDataSet().toString());
		
		//
		RuleEngine re = RuleEngine.getInstance();
		DataSet ds = re.getFilter("客户.性别 == '女'");
		ds = ds.merge(context.getDataSet());
		log.debug("ds : " + ds);
		this.assertEquals("knc_prsn inner join select target_data_id=MetricValue.F_TargetDataID,formula_value"+
				"=MetricValue.F_QuotaValue from MetricValue where ((((((MetricValue.F_Frequence=D and "+
				"MetricValue.F_Date=2007-02-08) and MetricValue.F_TargetType=Customer) and "+
				"MetricValue.F_TargetId=1) and MetricValue.F_DimensionType=*) and MetricValue.F_Dimension"+
				"=*) and MetricValue.F_MetricId=1) AS table1 on table1.target_data_id=knc_prsn.custno where "+
				"knc_prsn.sextp=1", ds.toString());
	}
	
	private Context createContext(){
		Context context = new Context(dbm);
		context.setCurrentFrequence(new FrequenceDate("D20070208"));	
		return context;
	}
}
