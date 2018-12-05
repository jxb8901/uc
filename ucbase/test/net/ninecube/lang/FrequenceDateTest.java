package net.ninecube.lang;

import java.util.List;

import net.ninecube.util.DateUtil;
import junit.framework.TestCase;

public class FrequenceDateTest extends TestCase {

	public void testStartEndDate() {
		this.assertEquals("2006-12-31", new FrequenceDate("D20061231").getStartDate().toString());
		this.assertEquals("2007-01-01", new FrequenceDate("D20061231").getEndDate().toString());

		this.assertEquals("2006-12-01", new FrequenceDate("M200612").getStartDate().toString());
		this.assertEquals("2007-01-01", new FrequenceDate("M200612").getEndDate().toString());
		
		this.assertEquals("2006-10-01", new FrequenceDate("Q200604").getStartDate().toString());
		this.assertEquals("2007-01-01", new FrequenceDate("Q200604").getEndDate().toString());
		
		this.assertEquals("2006-01-01", new FrequenceDate("Y2006").getStartDate().toString());
		this.assertEquals("2007-01-01", new FrequenceDate("Y2006").getEndDate().toString());
	}
	
	public void testStartEndDateType(){
		FrequenceDate f = new FrequenceDate("D20061231");
		this.assertEquals(java.sql.Date.class, f.getStartDate().getClass());
		this.assertEquals(java.sql.Date.class, f.getEndDate().getClass());
		this.assertEquals(java.sql.Date.class, f.getEndDate(true).getClass());
	}
	
	public void testStartEndInclusiveDate() {
		this.assertEquals("2006-12-31", new FrequenceDate("D20061231").getStartDate().toString());
		this.assertEquals("2006-12-31", new FrequenceDate("D20061231").getEndDate(true).toString());

		this.assertEquals("2006-12-01", new FrequenceDate("M200612").getStartDate().toString());
		this.assertEquals("2006-12-31", new FrequenceDate("M200612").getEndDate(true).toString());
		
		this.assertEquals("2006-10-01", new FrequenceDate("Q200604").getStartDate().toString());
		this.assertEquals("2006-12-31", new FrequenceDate("Q200604").getEndDate(true).toString());
		
		this.assertEquals("2006-01-01", new FrequenceDate("Y2006").getStartDate().toString());
		this.assertEquals("2006-12-31", new FrequenceDate("Y2006").getEndDate(true).toString());
	}
	
	public void testCast() {
		this.assertEquals("D20061231", new FrequenceDate("D20061231").cast(Frequence.DAY).toString());
		this.assertEquals("M200612", new FrequenceDate("D20061231").cast(Frequence.MONTH).toString());
		this.assertEquals("Q200604", new FrequenceDate("D20061231").cast(Frequence.QUARTER).toString());
		this.assertEquals("Y2006", new FrequenceDate("D20061231").cast(Frequence.YEAR).toString());

		this.assertEquals("M200610", new FrequenceDate("M200610").cast(Frequence.MONTH).toString());
		this.assertEquals("Q200604", new FrequenceDate("M200610").cast(Frequence.QUARTER).toString());
		this.assertEquals("Y2006", new FrequenceDate("M200610").cast(Frequence.YEAR).toString());
	}
	
	public void testFrequenceComparable() {
		this.assertTrue(Frequence.DAY.compareTo(Frequence.YEAR) < 0);
		this.assertTrue(Frequence.DAY.compareTo(Frequence.DAY) == 0);
		this.assertTrue(Frequence.QUARTER.compareTo(Frequence.MONTH) > 0);
	}
	
	public void testAdd() {
		this.assertEquals("D20070101", new FrequenceDate("D20061231").add(1).toString());
		this.assertEquals("M200701", new FrequenceDate("M200612").add(1).toString());
		this.assertEquals("Q200701", new FrequenceDate("Q200604").add(1).toString());
		this.assertEquals("Y2007", new FrequenceDate("Y2006").add(1).toString());
		
		this.assertEquals("D20070102", new FrequenceDate("D20061231").add(2).toString());
		this.assertEquals("M200702", new FrequenceDate("M200612").add(2).toString());
		this.assertEquals("Q200702", new FrequenceDate("Q200604").add(2).toString());
		this.assertEquals("Y2008", new FrequenceDate("Y2006").add(2).toString());
	}
	
	public void testSub() {
		this.assertEquals(1, new FrequenceDate("D20070101").sub(new FrequenceDate("D20061231")));
		this.assertEquals(1, new FrequenceDate("M200701").sub(new FrequenceDate("M200612")));
		this.assertEquals(1, new FrequenceDate("Q200701").sub(new FrequenceDate("Q200604")));
		this.assertEquals(1, new FrequenceDate("Y2007").sub(new FrequenceDate("Y2006")));
		
		this.assertEquals(2, new FrequenceDate("D20070102").sub(new FrequenceDate("D20061231")));
		this.assertEquals(2, new FrequenceDate("M200702").sub(new FrequenceDate("M200612")));
		this.assertEquals(2, new FrequenceDate("Q200702").sub(new FrequenceDate("Q200604")));
		this.assertEquals(2, new FrequenceDate("Y2008").sub(new FrequenceDate("Y2006")));
	}
	
	public void testComparable() {
		this.assertTrue(new FrequenceDate("D20050721").compareTo(new FrequenceDate("D20050720")) > 0);
		this.assertTrue(new FrequenceDate("M200701").compareTo(new FrequenceDate("M200601")) > 0);
		this.assertTrue(new FrequenceDate("Q200704").compareTo(new FrequenceDate("Q200701")) > 0);
		this.assertTrue(new FrequenceDate("Y2009").compareTo(new FrequenceDate("Y2007")) > 0);
		this.assertTrue(new FrequenceDate("Y2005").compareTo(new FrequenceDate("D20071231")) > 0);
	}
	
	public void testGetAllEndFrequenceDate(){
		List<FrequenceDate> freqDates = FrequenceDate.getAllEndFrequenceDate(DateUtil.fromYYYYMMDD("20070522"));
		this.assertEquals(1, freqDates.size());
		freqDates = FrequenceDate.getAllEndFrequenceDate(DateUtil.fromYYYYMMDD("20070531"));
		this.assertEquals(2, freqDates.size());
		System.out.println("freqDates : " + freqDates);
		this.assertTrue(freqDates.contains(new FrequenceDate("M200705")));
		
		
	}
	
	public void testCompareTo() {
		this.assertEquals(new FrequenceDate("M200705"), new FrequenceDate("M200705"));
		this.assertEquals(new FrequenceDate(DateUtil.fromYYYYMMDD("20070501"), Frequence.MONTH), 
				new FrequenceDate("M200705"));
		this.assertEquals(new FrequenceDate(DateUtil.fromYYYYMMDD("20070502"), Frequence.MONTH), 
				new FrequenceDate("M200705"));
		this.assertEquals(new FrequenceDate(DateUtil.fromYYYYMMDD("20070531"), Frequence.MONTH), 
				new FrequenceDate("M200705"));
	}
	
	public void testConstruct(){
		FrequenceDate freqd = new FrequenceDate(DateUtil.fromYYYYMMDD("20070522"),Frequence.QUARTER);
		this.assertEquals(DateUtil.parseDate("200704", "yyyyMM"),  freqd.getStartDate());
		
	}
	
}
