/**
 * 
 * created on 2007-1-18
 */
package net.ninecube.fishman;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * 
 * @author jxb
 * 
 */
public class AllTests {

	public static Test suite() throws Exception {
		TestSuite suite = new TestSuite("Test for net.ninecube.fishman");
		//$JUnit-BEGIN$
		suite.addTestSuite(FishManTest.class);
		for (int i = 1; i <= 2; i++) {
			suite.addTestSuite(Class.forName("net.ninecube.fishman.Test" + i));
		}
		//$JUnit-END$
		return suite;
	}

}
