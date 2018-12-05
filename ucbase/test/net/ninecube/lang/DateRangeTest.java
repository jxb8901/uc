package net.ninecube.lang;

import java.util.Iterator;

import junit.framework.TestCase;
import net.ninecube.util.DateUtil;

public class DateRangeTest extends TestCase {

	public void testSize() {
		this.assertDateRangeSize(1, new DateRange(new FrequenceDate("D"+DateUtil.getYYYYMMDD()), new FrequenceDate("D"+DateUtil.getYYYYMMDD())));
		this.assertDateRangeSize(3, new DateRange(new FrequenceDate("D"+"20060410"), new FrequenceDate("D"+"20060412")));
		this.assertDateRangeSize(4, new DateRange(new FrequenceDate("D"+"20060330"), new FrequenceDate("D"+"20060402")));
		this.assertDateRangeSize(5, new DateRange(new FrequenceDate("D"+"20051230"), new FrequenceDate("D"+"20060103")));

		this.assertDateRangeSize(1, new DateRange(new FrequenceDate("M"+"200703"), new FrequenceDate("M"+"200703")));
		this.assertDateRangeSize(3, new DateRange(new FrequenceDate("M"+"200703"), new FrequenceDate("M"+"200705")));
		this.assertDateRangeSize(12, new DateRange(new FrequenceDate("M"+"200601"), new FrequenceDate("M"+"200612")));
		this.assertDateRangeSize(13, new DateRange(new FrequenceDate("M"+"200601"), new FrequenceDate("M"+"200701")));

		this.assertDateRangeSize(1, new DateRange(new FrequenceDate("Q"+"200703"), new FrequenceDate("Q"+"200703")));
		this.assertDateRangeSize(3, new DateRange(new FrequenceDate("Q"+"200701"), new FrequenceDate("Q"+"200703")));
		this.assertDateRangeSize(2, new DateRange(new FrequenceDate("Q"+"200604"), new FrequenceDate("Q"+"200701")));

		this.assertDateRangeSize(1, new DateRange(new FrequenceDate("Y"+"2007"), new FrequenceDate("Y"+"2007")));
		this.assertDateRangeSize(3, new DateRange(new FrequenceDate("Y"+"2005"), new FrequenceDate("Y"+"2007")));
	}
	
	public void testSizeOfFrequence() {
		this.assertDateRangeSizeOfFrequence(1, "D", new DateRange(new FrequenceDate("D"+DateUtil.getYYYYMMDD()), new FrequenceDate("D"+DateUtil.getYYYYMMDD())));
		this.assertDateRangeSizeOfFrequence(3, "D", new DateRange(new FrequenceDate("D"+"20060410"), new FrequenceDate("D"+"20060412")));
		this.assertDateRangeSizeOfFrequence(4, "D", new DateRange(new FrequenceDate("D"+"20060330"), new FrequenceDate("D"+"20060402")));
		this.assertDateRangeSizeOfFrequence(5, "D", new DateRange(new FrequenceDate("D"+"20051230"), new FrequenceDate("D"+"20060103")));
	}

	public void assertDateRangeSize(int size1, DateRange daterange) {
		int size = 0;
		
		Iterator it = daterange.iterator();
		for (;it.hasNext();) {size++; it.next();}
		
		this.assertEquals(size1, size);
		this.assertEquals(size, daterange.size());
	}

	public void assertDateRangeSizeOfFrequence(int size1, String fre, DateRange daterange) {
		int size = 0;
		size = size1;
//		Iterator it = daterange.iterate();
//		for (;it.hasNext();) {size++; it.next();}
		
		this.assertEquals(size1, size);
		this.assertEquals(size, daterange.size(Frequence.get(fre)));
	}
	
	public void testIterate() {
		this.assertEquals(new FrequenceDate("Q200601"), new DateRange(new FrequenceDate("Q200601"), new FrequenceDate("Q200601")).iterator().next());
	}
	
	public void testGetIndex(){
		this.assertEquals(0, new DateRange(new FrequenceDate("D20061025"), new FrequenceDate("D20061030")).getIndex((new FrequenceDate("D20061025")).getStartDate()));
		this.assertEquals(3, new DateRange(new FrequenceDate("D20061025"), new FrequenceDate("D20061030")).getIndex((new FrequenceDate("D20061028")).getStartDate()));
		try{
			this.assertEquals(3, new DateRange(new FrequenceDate("D20061025"), new FrequenceDate("D20061030")).getIndex((new FrequenceDate("D20061128")).getStartDate()));
			this.fail();
		}catch(Throwable exc){};
	}

	public void testOrder() {
		FrequenceDate f1 = new FrequenceDate("D20061025");
		FrequenceDate f2 = new FrequenceDate("D20061030");
		DateRange d1 = new DateRange(f1, f2);
		DateRange d2 = new DateRange(f2, f1);
		this.assertEquals(d1.size(), d2.size());
	}
}
