package net.ninecube.saturn.function;

import java.util.ArrayList;
import java.util.List;

import net.ninecube.formula.ruleadapter.MetricFunctionProviderImpl;
import net.ninecube.saturn.function.impl.FunctionProviderImpl;

public abstract class FunctionProvider {

	public abstract FunctionDefinition getByName(String name);

	public abstract FunctionDefinition getByDesc(String desc);

	/**
	 * 
	 * @param mapping
	 * @return
	 */
	public abstract FunctionDefinition getByMapping(String mapping);

	public static FunctionProvider getInstance() {
		return funProvider;
	}
	
	public static void setInstance(FunctionProvider fp){
		if(fp != null) funProvider = fp;
	}
	
	private static FunctionProvider funProvider = new FunctionProviderImpl();
	
	public static class ChainedFunctionProviderImpl extends FunctionProvider{
		private List<FunctionProvider> providers = new ArrayList<FunctionProvider>();
		
		public void add(FunctionProvider fp){
			providers.add(fp);
		}
		
		public void setProviders(List<FunctionProvider> fps){
			this.providers = fps;
		}
		
		public FunctionDefinition getByName(String name){
			for(FunctionProvider fp : providers){
				if(fp.getByName(name) != null) return fp.getByName(name);
			}
			return null;
		}
		
		public FunctionDefinition getByDesc(String desc){
			for(FunctionProvider fp : providers){
				if(fp.getByDesc(desc) != null) return fp.getByDesc(desc);
			}
			return null;
		}
		
		public FunctionDefinition getByMapping(String mapping){
			for(FunctionProvider fp : providers){
				if(fp.getByMapping(mapping) != null) return fp.getByMapping(mapping);
			}
			return null;
		}
	}

}
