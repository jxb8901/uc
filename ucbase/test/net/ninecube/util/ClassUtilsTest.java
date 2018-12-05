/**
 * 
 * created on 2006-12-30
 */
package net.ninecube.util;

import junit.framework.TestCase;

/**
 * 
 * @author jxb
 * 
 */
public class ClassUtilsTest extends TestCase {

	public void testGetShortClassName() {
		this.assertEquals("String", ClassUtils.getShortClassName(String.class));
	}

}
