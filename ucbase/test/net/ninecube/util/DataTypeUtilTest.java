package net.ninecube.util;

import junit.framework.TestCase;

public class DataTypeUtilTest extends TestCase {

	public void testStr2BigDecimal() {
		this.assertNotNull(DataTypeUtil.str2BigDecimal("1,234.00"));
		this.assertNotNull(DataTypeUtil.str2BigDecimal("1,234.00 "));
	}

	public void testStr2Date() {
		this.assertNull(DataTypeUtil.str2Date("20051232", "yyyyMMdd"));
		System.out.println(DataTypeUtil.str2Date("20051213a", "yyyyMMdd"));
		this.assertNull(DataTypeUtil.str2Date("20051213a", "yyyyMMdd"));
		
		this.assertNotNull(DataTypeUtil.str2Date("20051212", "yyyyMMdd"));
		this.assertNotNull(DataTypeUtil.str2Date("20050131", "yyyyMMdd"));
	}

}
