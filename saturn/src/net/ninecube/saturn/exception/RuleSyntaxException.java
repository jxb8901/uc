package net.ninecube.saturn.exception;

import net.ninecube.lang.BaseException;

public class RuleSyntaxException extends BaseException{
	private int line = -1;
	private int column = -1;
	private String message;
	
	public RuleSyntaxException(String message){
		this.message = message;
	}
	
	public RuleSyntaxException(String message, Exception cause){
		super(message,cause);
		this.message = message;
	}
	
	public RuleSyntaxException(int line, int column , String message){
		this.line = line;
		this.column = column;
		this.message = message + "(" + line + "行," + column + "列) . ";
	}

	public int getColumn() {
		return column;
	}

	public String getMessage() {
		return message;
	}

	public int getLine() {
		return line;
	}
	
}
