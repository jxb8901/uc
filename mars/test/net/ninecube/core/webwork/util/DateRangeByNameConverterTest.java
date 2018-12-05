/**
 * 
 * created on 2007-4-25
 */
package net.ninecube.core.webwork.util;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import net.ninecube.lang.DateRange;

/**
 * 
 * @author jxb
 */
public class DateRangeByNameConverterTest extends TestCase {

	public void testConvertMapOfStringObject() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("daterange_D_Start", "2007-04-01");
		params.put("daterange_D_End", "2007-04-10");
		DateRangeByNameConverter.convert(params);
		this.assertNotNull(params.get("daterange"));
		this.assertTrue(params.get("daterange") instanceof DateRange);
		System.out.println(params.get("daterange"));
	}

}
