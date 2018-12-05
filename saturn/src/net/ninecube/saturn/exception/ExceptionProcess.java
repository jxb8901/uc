package net.ninecube.saturn.exception;

import java.util.List;

public interface ExceptionProcess {

	public void process(Exception exception);
	public List<Exception> getAllExceptions();
}
