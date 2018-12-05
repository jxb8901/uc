/**
 * 
 * created on 2007-1-16
 */
package net.ninecube.fishman.parser;

import java.io.Reader;
import java.io.StringReader;
import java.util.Map;

import net.ninecube.fishman.Block;


/**
 * 
 * @author jxb
 * 
 */
public class BlockImpl implements Block {
	private Map<String, String> properties;
	private String in;
	private boolean isStatic = true;
	private boolean isHead = false;
	
	static public BlockImpl newDummyEndBlock(String name) {
		BlockImpl ret = new BlockImpl("<!-- EndTemplate dummy='true' name='"+name+"' -->");
		ret.isHead = false;
		ret.isStatic = false;
		return ret;
	}
	
	public BlockImpl(String in) {
		this.in = in;
	}
	
	public BlockImpl(String in, Map<String, String> properties, boolean isHead) {
		this.in = in;
		this.properties = properties;
		this.isHead = isHead;
		this.isStatic = false;
	}
	
	//~ implements Block

	public boolean isStatic() {
		return isStatic;
	}
	
	public boolean isHead() {
		return !isStatic && isHead;
	}
	
	public boolean isTail() {
		return !isStatic && !isHead;
	}

	public String getName() {
		return this.properties.get("name");
	}

	public Reader getInput() {
		return new StringReader(in);
	}
	
	public String toString() {
		return "Block"+this.properties + ":" + this.in;
	}
}
