package net.ninecube.formula.ruleadapter;

import net.ninecube.formula.Formula;
import net.ninecube.formula.FormulaResolver;
import net.ninecube.formula.Target;
import net.ninecube.formula.TargetManager;
import net.ninecube.formula.exception.FormulaNotFoundException;
import net.ninecube.saturn.database.DatabaseManager;
import net.ninecube.saturn.function.FunctionDefinition;
import net.ninecube.saturn.function.FunctionProvider;
import net.ninecube.saturn.function.impl.FunctionDefinitionImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MetricFunctionProviderImpl extends FunctionProvider {
	private Log log = LogFactory.getLog(MetricFunctionProviderImpl.class);
	private DatabaseManager dbm = DatabaseManager.get();
	private FormulaResolver formulaResolver;
	private TargetManager targetManager;
	
	public MetricFunctionProviderImpl(){
	}
	
	public MetricFunctionProviderImpl(FormulaResolver fr, TargetManager tm){
		this.formulaResolver = fr;
		this.targetManager = tm;
	}
	
	@Override
	public FunctionDefinition getByDesc(String desc) {
		return null;
	}

	@Override
	public FunctionDefinition getByMapping(String mapping) {
		return null;
	}

	@Override
	public FunctionDefinition getByName(String name) {
		int dots = name.indexOf(".");
		if(this.formulaResolver == null || dots == -1) return null;
		String targetname = name.substring(0,dots);
		String mname = name.substring(dots+1);
		Formula f = null;
		try {
			f = formulaResolver.resolve(mname);
		} catch (FormulaNotFoundException e) {
		}
		if(f == null) return null;
		Target t = targetManager.getTargetByName(targetname, f.getTargetType());
		if(t == null) return null;
		return new FunctionDefinitionImpl(name,name,new MetricFunction(f,t),null);
	}
	
	public void setDatabaseManager(DatabaseManager dbm){
		this.dbm = dbm;
	}
	
	public DatabaseManager setDatabaseManager(){
		return this.dbm;
	}

	public void setFormulaResolver(FormulaResolver formulaResolver) {
		this.formulaResolver = formulaResolver;
	}

	public void setTargetManager(TargetManager targetManager) {
		this.targetManager = targetManager;
	}

}
