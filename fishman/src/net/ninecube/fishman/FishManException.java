/**
 * 
 * created on 2007-1-25
 */
package net.ninecube.fishman;

/**
 * 
 * @author jxb
 * 
 */
public class FishManException extends RuntimeException {

	public FishManException() {
	}

	public FishManException(String message) {
		super(message);
	}

	public FishManException(Throwable cause) {
		super(cause);
	}

	public FishManException(String message, Throwable cause) {
		super(message, cause);
	}

}
