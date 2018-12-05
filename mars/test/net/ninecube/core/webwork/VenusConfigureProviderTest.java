package net.ninecube.core.webwork;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import net.ninecube.core.trans.PageTransaction;
import net.ninecube.test.domain.TestUser;
import net.ninecube.test.trans.manage.user.Edit;
import net.ninecube.util.DynamicBean;

import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.ActionProxy;
import com.opensymphony.xwork.ActionProxyFactory;
import com.opensymphony.xwork.config.ConfigurationManager;
import com.opensymphony.xwork.config.entities.ActionConfig;
import com.opensymphony.xwork.config.entities.PackageConfig;
import com.opensymphony.xwork.util.OgnlValueStack;

/**
 * 测试基本的action配置
 * 
 * @author jxb
 *
 */
public class VenusConfigureProviderTest extends TestCase {
	
	private Map<String, String> params;
	private HashMap<Object, Object> extraContext;
	
	@Override
	public void setUp() throws Exception {
		WebworkTestUtil.setup();
		extraContext = (HashMap<Object, Object>) ServletActionContext.getContext().getContextMap();
		params = (HashMap<String, String>)ServletActionContext.getContext().getParameters();
	}

	public void testAction() throws Exception{
		params.put("userid", "1234");
		params.put("Password", "123456");
		params.put("Password2", "123456");
		params.put("name", "testname");
		
		ActionProxy proxy = WebworkTestUtil.getActionProxy("/manage/user", "edit");
		ActionConfig actionconfig = proxy.getConfig(); 
		
		String packagename = "manage.user";
		this.assertEquals(packagename, actionconfig.getPackageName());
		PackageConfig packageconfig = ConfigurationManager.getConfiguration().getPackageConfig(packagename);
		this.assertNotNull(packageconfig);
		this.assertEquals(2, packageconfig.getParents().size());
		this.assertEquals(18, actionconfig.getInterceptors().size());
		
		this.assertEquals(4, actionconfig.getResults().size());
		
		proxy.setMethod("submit");
		proxy.setExecuteResult(false);
		Edit action = (Edit) proxy.getAction();
		String result = proxy.execute();
		
		this.assertEquals("success", result);
		this.assertEquals("submit", action.executionMethod);
		this.assertEquals("1234", action.getUserid());
		this.assertEquals("123456", action.getPassword());
		this.assertEquals(120, action.getStaticParam());
	}
	
	@SuppressWarnings("unchecked")
	public void testValidate() throws Exception {
		params.put("userid", "abc");
		params.put("name", "a");
		params.put("password", "123456");
		params.put("password2", "123412");
		
		ActionProxy proxy = WebworkTestUtil.getActionProxy("/manage/user", "edit");
		proxy.setMethod("submit");
		proxy.setExecuteResult(false);
		Edit action = (Edit) proxy.getAction();
		String result = proxy.execute();

		this.assertEquals("123456", action.getPassword());
		this.assertEquals("123412", action.getPassword2());
		this.assertEquals("input", result);
		this.assertEquals(1, action.getActionErrors().size());
		this.assertEquals("密码与确认密码不符", action.getActionErrors().iterator().next());
		this.assertEquals(2, action.getFieldErrors().size());
		Map<String, List<String>> fielderrors = action.getFieldErrors();
		this.assertEquals("数字格式不正确[用户ID]", fielderrors.get("userid").get(0));
		this.assertEquals("字段[名称]长度必须大于6", fielderrors.get("name").get(0));
		this.assertEquals(null, action.executionMethod);

		params.put("userid", "1234");
		params.put("password", "123456");
		params.put("password2", "123456");
		params.put("name", "abcdefg");
		
		proxy = ActionProxyFactory.getFactory().createActionProxy("/manage/user", "edit", extraContext);
		proxy.setMethod("submit");
		proxy.setExecuteResult(false);
		action = (Edit) proxy.getAction();
		result = proxy.execute();

		System.err.println(action.getFieldErrors());
		this.assertEquals("123456", action.getPassword());
		this.assertEquals("123456", action.getPassword2());
		this.assertEquals("success", result);
		this.assertEquals(0, action.getActionErrors().size());
		this.assertEquals("submit", action.executionMethod);
	}
	
	public void testValueStack() throws Exception {
		OgnlValueStack stack = new OgnlValueStack();
		TestUser cg = new TestUser();
		PageTransaction page = new PageTransaction();
		DynaBean article = getBean();
		Object bean = getDynamicBean();
		stack.push(bean);
		stack.push(article);
		stack.push(page);
		stack.push(cg);
		stack.setValue("title", "test title");
		stack.setValue("pageSize", "2");
		stack.setValue("idno", "1");
		stack.setValue("xxx", "asdf");
		stack.setValue("date", "");
		this.assertEquals(Long.valueOf(1), cg.getIdno());
		this.assertEquals(2, page.getPageSize());
		this.assertEquals("test title", BeanUtils.getProperty(bean, "title"));
	}
	
	public void test1() throws Exception {
		DynaBean article = getBean();
		
		article.set("title", "this is a test");//对该对象进行操作
		article.set("content", "oh my god");
		article.set("createdTime", new Date());
		article.set("id", new Integer(1));

		System.out.println(article.get("title"));
		System.out.println(article.get("content"));
		System.out.println(article.get("createdTime"));
		System.out.println(article.get("id"));
	}
	
	private DynamicBean getDynamicBean() throws Exception {
		List<String> fieldNames = new ArrayList<String>();
		List<Class> fieldTypes = new ArrayList<Class>();
		fieldNames.add("title");
		fieldTypes.add(String.class);
		fieldNames.add("content");
		fieldTypes.add(String.class);
		fieldNames.add("id");
		fieldTypes.add(Integer.class);
		fieldNames.add("date");
		fieldTypes.add(Date.class);
		Class c = DynamicBean.createDynamicBean(VenusConfigureProviderTest.class.getPackage().getName() + ".Test", 
				fieldNames, fieldTypes);
		return (DynamicBean)c.newInstance();
	}
	
	private DynaBean getBean() throws Exception {
		// apache 的dynabean不能被webwork的valuestack所识别
		DynaProperty[] pro = new DynaProperty[] {//准备一个属性，动态定义类依靠这些属性来决定类的构成。
			new DynaProperty("title", String.class),
			new DynaProperty("content", String.class), 
			new DynaProperty("createdTime", Date.class), 
			new DynaProperty("id", Integer.class), 
		};

		DynaClass articleClass = new BasicDynaClass("Article", null, pro);//定义了一个类

		DynaBean article = articleClass.newInstance();//声明了一个ArticleClass的对象
		return article;
	}
}
