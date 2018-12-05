package net.ninecube.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

public class StringUtilTest extends TestCase {
	
	public void testGetFirstOrLastByDot() {
		this.assertEquals("1", StringUtil.getFirstByDot("1.2"));
		this.assertEquals("2", StringUtil.getSecondByDot("1.2"));

		this.assertEquals("", StringUtil.getFirstByDot("12"));
		this.assertEquals("12", StringUtil.getSecondByDot("12"));
	}
	
	public void testIsEmpty() {
		this.assertTrue(StringUtil.isEmpty(null));
		this.assertTrue(StringUtil.isEmpty(""));
		this.assertTrue(StringUtil.isEmpty(new Object[0]));
		this.assertTrue(StringUtil.isEmpty(new StringUtilTest[0]));
		this.assertTrue(StringUtil.isEmpty(new String[]{"", ""}));
		this.assertTrue(StringUtil.isEmpty(Collections.emptyList()));
		
		this.assertFalse(StringUtil.isEmpty("   "));
	}

	public void testMultiPadding() {
		this.assertEquals("123456aa", StringUtil.multiPadding("123456", 8, 'a'));
		this.assertEquals("12345678", StringUtil.multiPadding("12345678", 8, 'a'));
	}

	public void testLpadding() {
		this.assertEquals("00001234", StringUtil.lpadding("1234", 8, "0"));
	}
	
	public void testRtrim() {
		this.assertEquals("123456", StringUtil.rtrim("123456abcd", "abcd"));
		this.assertEquals("123456", StringUtil.rtrim("123456", ""));
		this.assertEquals("12345", StringUtil.rtrim("123456", "6"));
		this.assertEquals("", StringUtil.rtrim("123456", "123456"));
	}
	public void testSplit() {
		List<String> expected = new ArrayList<String>();
		expected.add("class");
		expected.add("validator");
		ArrayUtilTest.assertEquals(expected, StringUtil.split("   class,  validator "));
	}
}
