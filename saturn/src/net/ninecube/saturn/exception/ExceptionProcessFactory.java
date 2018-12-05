package net.ninecube.saturn.exception;

import java.util.HashMap;
import java.util.Map;

public class ExceptionProcessFactory {
	public static enum ProcessType{
		RETHROW,COLLECT
	}
	
	private static Map<ProcessType, ExceptionProcess> processors = new HashMap<ProcessType, ExceptionProcess>();
	
	static{
		processors.put(ProcessType.RETHROW, new SimpleExceptionProcess(ProcessType.RETHROW));
		processors.put(ProcessType.COLLECT, new SimpleExceptionProcess(ProcessType.COLLECT));
		
	}
	
	public static ExceptionProcess getExceptionProcess(ProcessType type){
		if(processors.containsKey(type))
			return processors.get(type);
		else
			throw new IllegalArgumentException("Unsupported processType : " + type);
	}
}
