/**
 * 
 * created on 2007-1-17
 */
package net.ninecube.fishman.sitemesh;

import java.io.IOException;

import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.PageParser;

/**
 * 
 * @author jxb
 * 
 */
public class FishManPageParser implements PageParser {

	public Page parse(char[] data) throws IOException {
		return new FishManPage(data);
	}

}
