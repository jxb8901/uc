/**
 * 
 * created on 2007-2-7
 */
package net.ninecube.saturn.database;

import junit.framework.TestCase;

import org.codehaus.jrc.contribute.Jrc;
import org.codehaus.jrc.predicate.Predicate;

/**
 * 
 * @author jxb
 * 
 */
public abstract class AbstractDatabaseTestCase extends TestCase {
	
	protected TableImplTestFixture fixture;
	
	protected void setUp() {
		fixture = new TableImplTestFixture();
		fixture.setUp();
	}
	
	public static Predicate getPredicate(String p) {
		return Jrc.getParser().parsePredicate(p);
	}

}
