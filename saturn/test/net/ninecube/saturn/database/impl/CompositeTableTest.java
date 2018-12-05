/**
 * 
 * created on 2007-2-7
 */
package net.ninecube.saturn.database.impl;

import net.ninecube.saturn.database.AbstractDatabaseTestCase;

/**
 * 
 * @author jxb
 * 
 */
public class CompositeTableTest extends AbstractDatabaseTestCase {

	public void testGetColumnByAlias() {
		this.assertNotNull(fixture.dbm.getColumnByAlias("客户.客户号"));
	}
}
