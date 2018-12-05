/**
 * 
 * created on 2007-5-23
 */
package net.ninecube.venus.query.impl;

import net.ninecube.lang.DateRange;
import net.ninecube.lang.Frequence;
import net.ninecube.util.DateUtil;
import junit.framework.TestCase;

/**
 * 
 * @author jxb
 */
public class MetricValueManagerImplTest extends TestCase {

	public void testAjustDateRange() {
		DateRange d1 = new DateRange(Frequence.DAY, 
				DateUtil.fromYYYYMMDD("20070101"), DateUtil.fromYYYYMMDD("20071231"));
		this.assertTrue(d1.size() > 100);
		DateRange d2 = MetricValueManagerImpl.ajustDateRange(d1, 100);
		System.out.println(d2);
		this.assertEquals(100, d2.size());
	}

}
