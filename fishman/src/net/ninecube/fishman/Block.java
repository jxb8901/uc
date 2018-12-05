/**
 * 
 * created on 2007-1-16
 */
package net.ninecube.fishman;

import java.io.Reader;

/**
 * 
 * @author jxb
 * 
 */
public interface Block {
	
	public boolean isStatic();
	
	public boolean isHead();
	
	public boolean isTail();
	
	public String getName();

	public Reader getInput();

}
