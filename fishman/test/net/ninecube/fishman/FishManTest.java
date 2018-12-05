/**
 * 
 * created on 2007-1-17
 */
package net.ninecube.fishman;

import java.io.Reader;
import java.io.StringReader;

import junit.framework.TestCase;

/**
 * 
 * @author jxb
 * 
 */
public class FishManTest extends TestCase {
	
	public static String process(String template, String instance) throws Exception {
		String ret = getContent(FishMan.process(new StringReader(template),
				new StringReader(instance)));
		System.out.println("***" + ret); 
		return ret;
	}
	
	public static void assertTrue(String result, String template, String instance) throws Exception {
		assertEquals(result, process(template, instance));
	}

	public void test1() throws Exception {
		assertTrue("x<!-- BeginTemplate    name='content' -->b<!--    EndTemplate -->z", 
				"x<!-- BeginTemplate name='content' -->b<!-- EndTemplate -->z",
				"a<!-- BeginTemplate    name='content' --><!--    EndTemplate -->c");
	}

	public void test2() throws Exception {
		assertTrue("x<!-- BeginTemplate    name='content' -->中<!--    EndTemplate -->z", 
				"x<!-- BeginTemplate name='content' -->b<!-- EndTemplate -->z",
				"a<!-- BeginTemplate    name='content' -->中<!--    EndTemplate -->c");
	}
	
	private static String getContent(Reader input) throws Exception {
		StringBuffer s = new StringBuffer();
		int ch;
		while ((ch = input.read()) != -1) {
			s.append((char)ch);
		}
		return s.toString();
	}
}
