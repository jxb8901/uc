/**
 * 
 * created on 2007-6-27
 */
package net.ninecube.core.config.impl;

import junit.framework.TestCase;

/**
 * 
 * @author jxb
 */
public class ActionConfImplTest extends TestCase {
	private TransactionConfigFixture fixture;
	
	public void setUp() {
		fixture = new TransactionConfigFixture();
	}

	public void testGetDynamicBeanClassName() {
		this.assertEquals("net.ninecube.user.Add$$Default", fixture.tc1.getAction("").getDynamicBeanClassName());
		this.assertEquals("net.ninecube.user.Query$$Default", fixture.tc3.getAction("").getDynamicBeanClassName());
		this.assertEquals("net.ninecube.customer.Query$$Default", fixture.tc23.getAction("").getDynamicBeanClassName());
	}

	public void testUniquenessOfDynamicBeanClassName() {
		this.assertNotSame(fixture.tc1.getAction("").getDynamicBeanClassName(), 
				fixture.tc23.getAction("").getDynamicBeanClassName());
		this.assertNotSame(fixture.tc3.getAction("").getDynamicBeanClassName(),
				fixture.tc23.getAction("").getDynamicBeanClassName());
		this.assertNotSame(fixture.tc1.getAction("").getDynamicBeanClassName(), 
				fixture.tc3.getAction("").getDynamicBeanClassName());
	}
}
