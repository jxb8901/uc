package net.ninecube.core.exception;

import net.ninecube.lang.BaseException;

public class NoAuthenticationException extends BaseException {

	public NoAuthenticationException(){
	}
	
	public NoAuthenticationException(String message){
		super(message);
	}

}
