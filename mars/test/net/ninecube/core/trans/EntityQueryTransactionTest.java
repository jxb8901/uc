/**
 * 
 * created on 2007-1-15
 */
package net.ninecube.core.trans;

import net.ninecube.core.webwork.WebworkTestUtil;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import com.opensymphony.xwork.ActionProxy;

/**
 * 
 * @author jxb
 * 
 */
public class EntityQueryTransactionTest extends AbstractTransactionalDataSourceSpringContextTests {

	public void testExecute() throws Exception {
		ActionProxy proxy = WebworkTestUtil.getActionProxy("/manage/user", "edit");
		proxy.setExecuteResult(false);
		proxy.execute();
	}
	
	protected void onSetUpBeforeTransaction() throws Exception {
		WebworkTestUtil.setup(super.applicationContext);
	}
	
	@Override
	protected String[] getConfigLocations() {
		return new String[]{
				"classpath*:applicationContext-test.xml",
				"classpath*:applicationContext-hibernate.xml",
				"classpath*:applicationContext-transaction.xml",
				"classpath*:applicationContext-testbeans.xml",
		};
	}
}
