/**
 * 
 * created on 2006-12-25
 */
package net.ninecube.core.config.impl;

import junit.framework.TestCase;
import net.ninecube.core.config.TransactionConf;

/**
 * 
 * @author jxb
 * 
 */
public class PackageConfTest extends TestCase {
	private TransactionConfigFixture fixture;
	
	public void setUp() {
		fixture = new TransactionConfigFixture();
	}

	public void testGetTransactionByClass() {
		this.assertEquals(fixture.tc1, fixture.config.getTransactionByClass("net.ninecube.user.Add"));
		this.assertNull(fixture.pc.getTransactionByClass(""));
		this.assertNull(fixture.pc.getTransactionByClass("net.ninecube.package.xxx.Add"));
	}

}
