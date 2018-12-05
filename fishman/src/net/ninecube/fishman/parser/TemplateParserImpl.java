/**
 * 
 * created on 2007-1-16
 */
package net.ninecube.fishman.parser;

import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ninecube.fishman.Block;


/**
 * 
 * @author jxb
 * 
 */
public class TemplateParserImpl implements net.ninecube.fishman.IParser.ITemplateParser {
	private Parser parser;
	private boolean valid = false;
	
	public TemplateParserImpl(Reader in) {
		this.parser = new Parser(in);
		//this.parser.token_source.nameOfChecked = "Template";
	}
	
	public boolean isValid() {
		return valid;
	}
	
	public BlockImpl next() throws ParseException {
		BlockImpl ret = parser.next();
		if (ret != null && ret.isHead()) this.valid = true;
		return ret;
	}
	
	public Block nextDynamicEnd() {
		BlockImpl b = next();
		while (b != null) {
			if (b.isTail()) return b;
			b = next();
		}
		return null;
	}
}
