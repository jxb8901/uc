package net.ninecube.saturn;

import junit.framework.TestCase;

public class DateDataTest extends TestCase {

	public void testGetSql() {
		assertSql("1983年", "1983", DateData.Type.Y);
		assertSql("3季度", "03", DateData.Type.Q);
		assertSql("12月", "12", DateData.Type.M);
		assertSql("3日", "03", DateData.Type.D);
		
		assertSql("1983年3季度", "198303", DateData.Type.YQ);
		assertSql("1983年12月3日", "19831203", DateData.Type.YMD);
	}
	
	public void testParse() {
		this.assertEquals(DateData.Type.Y, DateData.parse("1983年").getType());
		
		try {
			DateData.parse("1983年12月35日");
			this.fail();
		} catch (IllegalArgumentException e) {
		}
	}
	
	private void assertSql(String literal, String sql, DateData.Type type) {
		DateData dt = DateData.parse(literal);
		this.assertEquals(type, dt.getType());
		this.assertEquals(sql, dt.getSql());
		this.assertEquals(literal, dt.toString());
	}

}
