/**
 * 
 * created on 2007-1-17
 */
package net.ninecube.fishman.sitemesh;

import java.io.IOException;
import java.io.Writer;

import com.opensymphony.module.sitemesh.parser.AbstractPage;

/**
 * 
 * @author jxb
 * 
 */
public class FishManPage extends AbstractPage {
	
	/**
	 * 超类未考虑双字节字符，直接返回char[]的长度，
	 * 会导致含有中文的网页输出到客户端时被截断
	 * 修改为取byte[]的长度
	 */
	@Override
	public int getContentLength() {
		return new String(super.pageData).getBytes().length;
	}

	public FishManPage(char[] data) {
		super.pageData = data;
	}
	
	public char[] getData() {
		return super.pageData;
	}
	
	@Override
	public void writeBody(Writer arg0) throws IOException {
		throw new UnsupportedOperationException();
	}

}
