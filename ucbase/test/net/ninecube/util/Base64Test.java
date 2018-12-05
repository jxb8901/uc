package net.ninecube.util;

import junit.framework.TestCase;

public class Base64Test extends TestCase {

	public void testEncode() {
		String str = "1234";
		System.out.println(new String(Base64.encode(str.getBytes())));
		System.out.println(StringUtil.padding("1234", ("1234".length() / 8 + 1) * 8, '\0'));
		System.out.println(StringUtil.unPadding(StringUtil.padding("1234", 8, '\0'), '\0'));
	}

}
