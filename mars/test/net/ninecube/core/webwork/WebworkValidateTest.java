/**
 * 
 * created on 2007-1-15
 */
package net.ninecube.core.webwork;

import java.util.ArrayList;
import java.util.List;

import net.ninecube.core.context.ServiceLocator;
import net.ninecube.core.trans.EntityCreateTransaction;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import com.opensymphony.xwork.ActionProxy;

/**
 * 
 * @author jxb
 * 
 */
public class WebworkValidateTest extends AbstractTransactionalDataSourceSpringContextTests {

	public void testValidate() throws Exception {
		ActionProxy proxy = WebworkTestUtil.getActionProxy("/manage/user", "create");
		EntityCreateTransaction t = (EntityCreateTransaction)proxy.getAction();
		proxy.setExecuteResult(false);
		proxy.execute();
	}
	
	protected void onSetUpBeforeTransaction() throws Exception {
		ServiceLocator.get().setBeanFactory(this.applicationContext);
		WebworkTestUtil.setup(this.applicationContext);
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
	
	public static void main(String[] a) throws Exception {
		List<String> list = new ArrayList<String>();
		list.remove("name");
		System.out.println("*** " + list);
	}
}
