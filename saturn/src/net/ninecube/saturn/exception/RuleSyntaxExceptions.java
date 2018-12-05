package net.ninecube.saturn.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import net.ninecube.lang.BaseException;

public class RuleSyntaxExceptions extends BaseException {
	private List<Exception> exceptions = new ArrayList<Exception>();

	public RuleSyntaxExceptions(){
	}
	
	public RuleSyntaxExceptions(List<Exception> exceptions){
		this.exceptions = exceptions;
	}
	
	public boolean isEmpty(){
		return exceptions.isEmpty();
	}
	
	public RuleSyntaxExceptions addException(Exception exc){
		exceptions.add(exc);
		return this;
	}
	
	public RuleSyntaxExceptions addException(List<Exception> excs){
		exceptions.addAll(excs);
		return this;
	}
	
	public List<Exception> getExceptions(){
		return exceptions;
	}	
	
	public String getMessage(){
		StringBuffer buf = new StringBuffer();
		buf.append("发现如下 " + exceptions.size() + "处错误：");
		for(int i = 0 ; i < exceptions.size() ; i++){
			buf.append("\r\n" + (i +1) + "." + exceptions.get(i).getMessage());
		}
		
		return buf.toString();
	}
		
	public String getFullMessage(){
		StringBuffer buf = new StringBuffer();
		buf.append("found " + exceptions.size() + " exceptions . following is detail . ");
		for(int i = 0 ; i < exceptions.size() ; i++){
			Exception exc = exceptions.get(i);
			buf.append("\r\n" + (i +1) + "." + exc);
			StringWriter strw = new StringWriter();
			exc.printStackTrace(new PrintWriter(strw));
			buf.append(strw.toString());
		}
		
		return buf.toString();
	}

	
}
