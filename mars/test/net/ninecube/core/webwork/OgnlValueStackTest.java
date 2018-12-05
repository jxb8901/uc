/**
 * 
 * created on 2007-6-20
 */
package net.ninecube.core.webwork;

import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import junit.framework.TestCase;
import net.ninecube.core.webwork.util.VenusObjectTypeDeterminer;
import net.ninecube.test.domain.TestUser;
import net.ninecube.test.trans.manage.user.Edit;

import com.opensymphony.xwork.util.DefaultObjectTypeDeterminer;
import com.opensymphony.xwork.util.OgnlContextState;
import com.opensymphony.xwork.util.OgnlValueStack;
import com.opensymphony.xwork.util.XWorkConverter;

/**
 * 
 * @author jxb
 */
public class OgnlValueStackTest extends TestCase {
	private static Log log = LogFactory.getLog(OgnlValueStackTest.class);
	private User user;
	private OgnlValueStack stack;

	protected void setUp() throws Exception {
		Logger.getLogger("com.opensymphony").setLevel(Level.ALL);
		XWorkConverter.getInstance().setObjectTypeDeterminer(new DefaultObjectTypeDeterminer(){
			@Override public Class getElementClass(Class parentClass, String property, Object key) {
				log.debug("***** getElementClass: '" + parentClass+ "', '" + property + "', '" + key);
				return User.class;
			}
		});
		stack = new OgnlValueStack();
		OgnlContextState.setCreatingNullObjects(stack.getContext(), true);
		OgnlContextState.setDenyMethodExecution(stack.getContext(), true);
		OgnlContextState.setReportingConversionErrors(stack.getContext(), true);
		user = new User();
		stack.push(user);
	}
	
	public void test1() throws Exception {		
		stack.setValue("friendList", new String[]{"1", "2"});
		this.assertNotNull(user.getFriendList());
		this.assertEquals(2, user.getFriendList().size());
		try {
			this.assertEquals(User.class, user.getFriendList().iterator().next().getClass());
			this.fail("WW无法执行从字符串到User的类型转换，便直接将字符串放进集合中");
		} catch (ClassCastException e) { }
	}
		
	public void test2() throws Exception {
		stack.setValue("friendList.name", new String[]{"1", "2"});
		this.assertNotNull(user.getFriendList());
		this.assertEquals(2, user.getFriendList().size());
		this.assertEquals(User.class, user.getFriendList().iterator().next().getClass());
	}
		
	public void test3() throws Exception {
		stack.setValue("friendList.name", new String[]{"1"});
		this.assertNotNull(user.getFriendList());
		this.assertEquals(1, user.getFriendList().size());
		this.assertEquals(User.class, user.getFriendList().iterator().next().getClass());
	}
		
	public void test4() throws Exception {
		stack.setValue("friendList.name", "1");
		this.assertNotNull(user.getFriendList());
		this.assertEquals("WW的XworkListPropertyAccessor" + 
				"在处理非数组的值时，不会执行创建空对象然后设置" + 
				"值的方式，故集合中没有任何对象", 
				0, user.getFriendList().size());
	}
	
	public static class User {
		private String name;
		private List<User> friendList;
		private Set<User> friendSet;
		public List<User> getFriendList() {
			return friendList;
		}
		public void setFriendList(List<User> friendList) {
			this.friendList = friendList;
		}
		public Set<User> getFriendSet() {
			return friendSet;
		}
		public void setFriendSet(Set<User> friendSet) {
			this.friendSet = friendSet;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
}
