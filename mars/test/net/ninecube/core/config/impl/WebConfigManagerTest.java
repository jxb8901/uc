/**
 * 
 * created on 2006-12-15
 */
package net.ninecube.core.config.impl;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import net.ninecube.core.config.ActionConf;
import net.ninecube.core.config.ExpressionConf;
import net.ninecube.core.config.FieldConf;
import net.ninecube.core.config.FieldTagType;
import net.ninecube.core.config.FieldType;
import net.ninecube.core.config.ModelConf;
import net.ninecube.core.config.PackageConf;
import net.ninecube.core.config.TransactionConf;
import net.ninecube.core.config.WebConfig;
import net.ninecube.core.config.field.FieldConfListImpl;
import net.ninecube.core.config.field.MergeFieldsConf;
import net.ninecube.test.domain.TestUser;
import net.ninecube.util.DynamicBean;

/**
 * 
 * @author jxb
 * 
 */
public class WebConfigManagerTest extends TestCase {
	private WebConfigManagerImpl cm;
	private WebConfig config;
	private final static String JAVABASEDIR = "net.ninecube.test.trans";
	private final static String VIEWBASEDIR = "/views";
	
	public void setUp() {
		cm = new WebConfigManagerImpl("venus-test.xml");
		cm.init();
		config = cm.getConfiguration();
	}
	
	public void testGetConfiguration() {
		this.assertNotNull(config);
		this.assertEquals("test", config.getAppname());
		this.assertEquals(1, config.getPackages().size());
		
		TransactionConf tc = config.getTransaction("/manage/user", "edit");
		this.assertNotNull(tc);
		this.assertEquals(8, tc.getAction("submit").getInputViewFields().size());
		this.assertEquals(9, tc.getAction("submit").getInputDataFields().size());
		FieldConf fc = tc.getAction("submit").getInputDataFields().get(1);
		this.assertNotNull(fc);
		this.assertEquals("userid", fc.getName());
		this.assertNull(fc.getValue());
		fc = tc.getAction("submit").getInputDataFields().get(7);
		this.assertNotNull(fc);
		this.assertEquals("staticParam", fc.getName());
		this.assertEquals("120", fc.getValue());
	} 
	
	public void testPackageGetName() {
		PackageConf sys = config.getPackageByNamespace("/manage");
		this.assertNotNull(sys);
		this.assertEquals("manage", sys.getName());
		this.assertEquals(JAVABASEDIR + ".manage", sys.getJavaPackageName());
		this.assertEquals(VIEWBASEDIR + "/manage", sys.getViewPackageName());
		this.assertEquals("/manage", sys.getNamespace());
		
		PackageConf um =sys.getSubPackage("user");
		this.assertNotNull(um);
		this.assertEquals("manage.user", um.getName());
		this.assertEquals(JAVABASEDIR + ".manage.user", um.getJavaPackageName());
		this.assertEquals(VIEWBASEDIR + "/manage/user", um.getViewPackageName());
		this.assertEquals("/manage/user", um.getNamespace());
	}
	
	public void testGetTransactionClassName() {
		TransactionConf tc = config.getTransaction("/manage/user", "edit");
		this.assertEquals(JAVABASEDIR+".manage.user.Edit", tc.getTransactionClassName());
	}
	
	public void testGetPackage() {
		this.assertNull(config.getPackageByNamespace("/xxxx"));
		
		this.assertNotNull(config.getPackageByNamespace("/manage"));
		this.assertNotNull(config.getPackageByNamespace("manage"));
		this.assertNotNull(config.getPackageByNamespace("manage/user"));
		this.assertNull(config.getPackageByNamespace("/managexxxx"));
		this.assertNull(config.getPackageByNamespace("manage/user/xxxx"));
		
		this.assertNull(config.getPackageByNamespace("/user"));
		PackageConf p = config.getPackageByNamespace("/manage/user");
		this.assertNotNull(p);
		this.assertNotNull(p.getModel());
		this.assertEquals(9, p.getModel().getFields().size());
		this.assertEquals("idno", p.getModel().getFields().get(0).getName());
		this.assertEquals("ID", p.getModel().getFields().get(0).getCname());
	}
	
	public void testGetTransaction() {
		TransactionConf tc = config.getTransaction("/manage/user", "edit");
		this.assertNotNull(tc);
		this.assertEquals(1, tc.getAction("submit").getExpressions().size());
		this.assertEquals("密码与确认密码不符", ((ExpressionConf)tc.getAction("submit").getExpressions().get(0)).getErrormsg());
		this.assertNull(config.getTransaction("xxx", "edit"));
		
		tc = config.getTransaction("/manage/userxxxxxxxxxx", "edit");
		this.assertNull(tc);
	}
	
	public void testTransactionExtend() {
		TransactionConfImpl tc = (TransactionConfImpl)config.getTransaction("/manage/user", "add");
		this.assertNotNull(tc);
		this.assertTrue(tc.isValidExtend());
		this.assertTrue(tc.isInherit(TransactionConfImpl.EXTEND_CLASS));
		this.assertEquals(JAVABASEDIR+".manage.user.Edit", tc.getTransactionClassName());
	}
	
	public void testTransactionInputOutput() {
		TransactionConfImpl tc = (TransactionConfImpl) config.getTransaction("/manage/user", "query");
		this.assertEquals(6, tc.getInternalInput().getQueryFields().size());
		this.assertEquals(9, tc.getInternalOutput().getQueryFields().size());
		List<? extends FieldConf> input = tc.getAction("submit").getInputViewFields();
		this.assertNotNull(input);
		this.assertEquals(5, input.size());
		List<? extends FieldConf> output = tc.getAction("submit").getOutputViewFields();
		this.assertNotNull(output);
		this.assertEquals(9, output.size());
	}
	
	public void testReferenceFields() {
		TransactionConfImpl tc = (TransactionConfImpl) config.getTransaction("/manage/user", "delete");
		FieldConfListImpl input = tc.getInternalInput();
		this.assertNotNull(input);
		this.assertEquals(1, input.getRawFields().size());
	}
	
	public void testLoadEnumField() {
		PackageConf p = config.getPackageByNamespace("/manage/user");
		ModelConf m = p.getModel();
		this.assertEquals(9, m.getFields().size());
	}
	
	public void testQueryField() {
		TransactionConfImpl tc = (TransactionConfImpl) config.getTransaction("/manage/user", "query");
		FieldConfListImpl input = tc.getInternalInput();
		this.assertNotNull(input);
		this.assertEquals(5, input.getRawFields().size());
		
		FieldConfListImpl output = tc.getInternalOutput();
		this.assertNotNull(output);
		this.assertEquals("idno", tc.getAction("").getOutputViewFields().get(0).getName());
		this.assertEquals("ID", tc.getAction("").getOutputViewFields().get(0).getCname());
	}
	
	public void testGetEntityClass() {
		TransactionConfImpl tc = (TransactionConfImpl) config.getTransaction("/manage/user", "query");
		this.assertNotNull(tc.getEntityClass());
		this.assertEquals(TestUser.class, tc.getEntityClass());
		
		tc = (TransactionConfImpl) config.getTransaction("/manage/user", "read");
		ActionConf ac = tc.getAction("default");
		this.assertEquals(TestUser.class, ac.getEntityClass());
		this.assertEquals("idno", ac.getKeyFieldName());
	}
	
	public void testInputDynamicBean() {
		TransactionConfImpl tc = (TransactionConfImpl) config.getTransaction("/manage/user", "query");
		ActionConf ac = tc.getAction("submit");
		DynamicBean bean = ac.newInputBean();
		Map<String, Object> map = bean.toMap();
		this.assertEquals(6, map.size());
	}
	
	public void testActionInherit() {
		TransactionConfImpl tc = (TransactionConfImpl) config.getTransaction("/manage/user", "query");
		ActionConf ac = tc.getAction("default");
		this.assertTrue(ac.getOutputViewFields().size() > 0);
	}
	
	public void testMergeField() {
		TransactionConfImpl tc = (TransactionConfImpl) config.getTransaction("/manage/user", "create");
		FieldConfListImpl input = tc.getInternalInput();
		this.assertEquals(2, input.getRawFields().size());
		MergeFieldsConf m = (MergeFieldsConf)input.getRawFields().get(1);
		this.assertEquals(FieldTagType.hidden, m.getTagtype());
		FieldConf f = tc.getAction("").getInputField("idno");
		this.assertNotNull(f);
		this.assertEquals(FieldTagType.hidden, f.getTagtype());
	}
	
	public void testMergeFieldWithDefaultDtdValue() {
		TransactionConfImpl tc = (TransactionConfImpl) config.getTransaction("/manage/user", "testMerge");
		FieldConfListImpl input = tc.getInternalInput();
		this.assertEquals(2, input.getRawFields().size());
		System.out.println("***" + input.getRawFields());
		FieldConf f = tc.getAction("").getInputField("idno");
		this.assertNotNull(f);
		this.assertEquals(FieldTagType.hidden, f.getTagtype());
		this.assertEquals(FieldType.date, f.getType());
		f = tc.getAction("").getInputField("userid");
		this.assertNotNull(f);
		this.assertEquals(FieldTagType.auto, f.getTagtype());
		this.assertEquals(FieldType.integer, f.getType());
	}
}
