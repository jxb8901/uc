/**
 * 
 * created on 2007-1-16
 */
package net.ninecube.fishman.parser;

import java.io.InputStream;
import java.io.StringReader;

import junit.framework.TestCase;

/**
 * 
 * @author jxb
 * 
 */
public class ParserTest extends TestCase {

	public void testTemplate() throws Exception {
		Parser p = newparser("aaaa<!--    BeginTemplate name='test'   test='test'  -->asdf<!--   EndTemplate -->");
		System.out.println(p.next());
		System.out.println(p.next());
		System.out.println(p.next());
		System.out.println(p.next());
		System.out.println(p.next());
		System.out.println(p.next());
		System.out.println(p.next());
		System.out.println(p.next());
		System.out.println(p.next());
		System.out.println(p.next());
		System.out.println(p.next());
		System.out.println(p.next());
//		Block b = p.next();
//		this.assertTrue(b.isStatic());
//		while (b != null) {
//			printInputStream(b.getOutput());
//			System.err.println(":" + b.isStatic());
//			b = p.next();
//		}
	}

	private void printInputStream(InputStream output) throws Exception {
		int ch;
		while ((ch = output.read()) != -1) {
			System.err.print((char)ch);
		}
	}

	private Parser newparser(String in) {
		Parser p = new Parser(new StringReader(in));
		p.token_source.nameOfChecked = "Template";
		return p;
	}
}
