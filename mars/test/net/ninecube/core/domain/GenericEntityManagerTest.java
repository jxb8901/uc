/**
 * 
 * created on 2007-4-5
 */
package net.ninecube.core.domain;

import net.ninecube.core.Bar;

/**
 * 
 * @author jxb
 * 
 */
public class GenericEntityManagerTest extends AbstractEntityManagerTest {
	@Override
	protected void onSetUpBeforeTransaction() throws Exception {
		super.onSetUpBeforeTransaction();
		GenericEntityManager<Bar> bm = new GenericEntityManager<Bar>(sessionFactory, Bar.class);
		this.bm = bm;
	}
}
