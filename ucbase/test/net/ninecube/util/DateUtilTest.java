package net.ninecube.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

/**
 * 
 * @author JXB
 *
 */
public class DateUtilTest extends TestCase {

	public void testGetYYYYMMDD() throws Exception {
		String s = "20050812";
		Date date = new SimpleDateFormat("yyyyMMdd").parse(s);
		this.assertEquals(s, DateUtil.getYYYYMMDD(date));
		System.out.println(DataTypeUtil.str2Date("200512", "yyyyM"));
	}

	public void testGetHHMMSS() throws Exception {
		String s = "123248";
		Date date = new SimpleDateFormat("HHmmss").parse(s);
		this.assertEquals(s, DateUtil.getHHMMSS(date));
	}

	public void testGetLastDateOfMonth() throws Exception {
		this.assertEquals("20050331", DateUtil.getYYYYMMDD(DateUtil.getLastDateOfMonth(2005, 3)));
		this.assertEquals("20000229", DateUtil.getYYYYMMDD(DateUtil.getLastDateOfMonth(2000, 2)));
		this.assertEquals("20000930", DateUtil.getYYYYMMDD(DateUtil.getLastDateOfMonth(2000, 9)));
		this.assertEquals("20061231", DateUtil.getYYYYMMDD(DateUtil.getLastDateOfMonth(2006, 12)));
	}
	
	public void testParseDate() {
		this.assertNull(DateUtil.parseDate("20051232", "yyyyMMdd"));
		this.assertNull(DateUtil.parseDate("200512", "yyyyMMdd"));
		System.out.print(DateUtil.parseDate("2005", "yyyy"));
	}
}
