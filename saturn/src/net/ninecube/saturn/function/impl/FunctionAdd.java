package net.ninecube.saturn.function.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import net.ninecube.saturn.Context;
import net.ninecube.saturn.function.Function;

import org.apache.log4j.Logger;

public class FunctionAdd implements Function {
	private static Logger log = Logger.getLogger(FunctionAdd.class);
	public Object execute(Context context, List indexArgs, Map namedArgs){
		BigDecimal result = null;
		log.debug("invode with params : " + indexArgs);
		
		return result;
	}

}
