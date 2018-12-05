package net.ninecube.saturn.exception;

import java.util.ArrayList;
import java.util.List;

import net.ninecube.lang.BaseException;

public class SimpleExceptionProcess implements ExceptionProcess {
	
	private ExceptionProcessFactory.ProcessType processType = ExceptionProcessFactory.ProcessType.RETHROW;
	private List<Exception> exceptions = new ArrayList<Exception>();
	
	public SimpleExceptionProcess(){
		
	}
	
	public SimpleExceptionProcess(ExceptionProcessFactory.ProcessType processType){
		this.processType = processType;
	}
	
	public void process(Exception exception) {
		switch(processType){
		case RETHROW :
			if(exception instanceof BaseException)
				throw (BaseException) exception;
			else throw new BaseException(exception.getMessage(), exception);
		case COLLECT:
			exceptions.add(exception);
			break;
		default:
			throw new UnsupportedOperationException("unsupported process type : " + processType);
		}

	}

	public void setProcessType(ExceptionProcessFactory.ProcessType processType) {
		this.processType = processType;
	}

	public List<Exception> getAllExceptions(){
		return exceptions;
	}

}
