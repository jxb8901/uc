/**
 * 
 * created on 2006-12-18
 */
package net.ninecube.core.webwork;

import java.util.Arrays;

import junit.framework.TestCase;
import net.ninecube.core.config.ExpressionConf;
import net.ninecube.core.config.FieldType;
import net.ninecube.core.config.field.FieldConfListImpl;
import net.ninecube.core.config.impl.FieldConfImpl;
import net.ninecube.core.config.impl.ModelConfImpl;
import net.ninecube.core.config.impl.PackageConfImpl;
import net.ninecube.core.config.impl.TransactionConfImpl;
import net.ninecube.core.config.impl.WebConfigImpl;

/**
 * 
 * @author jxb
 * 
 */
public class VenusActionValidatorManagerTest extends TestCase {

	private VenusActionValidatorManager validatorManager;
	private TransactionConfImpl tc;
	
	public void setUp() throws Exception {
		ValidatorAdaptor vs = (ValidatorAdaptor) Class.forName("net.ninecube.core.webwork.ValidatorAdaptorImpl").newInstance();
		validatorManager = new VenusActionValidatorManager(vs);
		validatorManager.clearValidatorCache();
		WebConfigImpl config = new WebConfigImpl("test", "test");
		PackageConfImpl pc = new PackageConfImpl(config, "package", "package", "net.ninecube", "/views");
		pc.setModel(new ModelConfImpl(pc));
		tc = new TransactionConfImpl(pc, "add", "add");
	}
	
	public void testBuildValidatorWithNonAction() {
		FieldConfImpl fc = new FieldConfImpl();
		fc.setName("f1");
		fc.setReq(true);
		addField(tc, fc);
		
		this.assertEquals(1, validatorManager.getValidators(tc.getAction("submit")).size());
		this.assertEquals(1, validatorManager.getValidators(tc.getAction("anyaction")).size());
		this.assertEquals(0, validatorManager.getValidators(tc.getAction("default")).size());
	}
	
	public void testBuildValidatorWithOneAction() {
		FieldConfImpl fc = new FieldConfImpl();
		fc.setName("f1");
		fc.setReq(true);
		fc.setAction(Arrays.asList("submit"));
		addField(tc, fc);
		
		this.assertEquals(0, validatorManager.getValidators(tc.getAction("default")).size());
		this.assertEquals(1, validatorManager.getValidators(tc.getAction("submit")).size());
	}
	
	public void testBuildValidatorWithTwoAction() {
		FieldConfImpl fc = new FieldConfImpl();
		fc.setName("f1");
		fc.setReq(true);
		fc.setAction(Arrays.asList("submit", "confirm"));
		addField(tc, fc);
		
		this.assertEquals(0, validatorManager.getValidators(tc.getAction("default")).size());
		this.assertEquals(1, validatorManager.getValidators(tc.getAction("confirm")).size());
		this.assertEquals(1, validatorManager.getValidators(tc.getAction("submit")).size());
	}
	
	public void testBuildValidatorWithAnyAction() {
		FieldConfImpl fc = new FieldConfImpl();
		fc.setName("f1");
		fc.setReq(true);
		fc.setAction(Arrays.asList("any"));
		addField(tc, fc);
		
		this.assertEquals(1, validatorManager.getValidators(tc.getAction("any")).size());
		this.assertEquals(1, validatorManager.getValidators(tc.getAction("default")).size());
		this.assertEquals(1, validatorManager.getValidators(tc.getAction("submit")).size());
	}

	public void testRequiredValidator() {
		FieldConfImpl fc = new FieldConfImpl();
		fc.setName("f1");
		fc.setReq(true);
		fc.setType(FieldType.string);
		addField(tc, fc);
		
		this.assertEquals("requiredstring", validatorManager.getValidators(tc.getAction("submit")).get(0).getType());
	}

	public void testRequiredStringValidator() {
		FieldConfImpl fc = new FieldConfImpl();
		fc.setName("f1");
		fc.setReq(true);
		fc.setType(FieldType.string);
		addField(tc, fc);
		
		this.assertEquals("requiredstring", validatorManager.getValidators(tc.getAction("submit")).get(0).getType());
	}

	public void testStringLengthValidator() {
		FieldConfImpl fc = new FieldConfImpl();
		fc.setName("f1");
		fc.setMaxlen(20);
		fc.setMinlen(5);
		addField(tc, fc);
		
		this.assertEquals("stringlength", validatorManager.getValidators(tc.getAction("submit")).get(0).getType());
	}

	public void testIntValidator() {
		FieldConfImpl fc = new FieldConfImpl();
		fc.setName("f1");
		fc.setType(FieldType.integer);
		fc.setMaxvalue("10");
		fc.setMinvalue("3");
		addField(tc, fc);
		
		this.assertEquals(2, validatorManager.getValidators(tc.getAction("submit")).size());
		this.assertEquals("regex", validatorManager.getValidators(tc.getAction("submit")).get(0).getType());
		this.assertEquals("int", validatorManager.getValidators(tc.getAction("submit")).get(1).getType());
	}

	public void testDoubleValidator() {
		FieldConfImpl fc = new FieldConfImpl();
		fc.setName("f1");
		fc.setType(FieldType.amount);
		fc.setMaxvalue("9999.99");
		fc.setMinvalue("0.1");
		addField(tc, fc);
		
		this.assertEquals(2, validatorManager.getValidators(tc.getAction("submit")).size());
		this.assertEquals("regex", validatorManager.getValidators(tc.getAction("submit")).get(0).getType());
		this.assertEquals("double", validatorManager.getValidators(tc.getAction("submit")).get(1).getType());
	}

	public void testDateValidator() {
		FieldConfImpl fc = new FieldConfImpl();
		fc.setName("f1");
		fc.setType(FieldType.date);
		fc.setMaxvalue("20071231");
		fc.setMinvalue("20070101");
		addField(tc, fc);
		
		this.assertEquals(2, validatorManager.getValidators(tc.getAction("submit")).size());
		this.assertEquals("regex", validatorManager.getValidators(tc.getAction("submit")).get(0).getType());
		this.assertEquals("date", validatorManager.getValidators(tc.getAction("submit")).get(1).getType());
	}

	public void testEmailValidator() {
		FieldConfImpl fc = new FieldConfImpl();
		fc.setName("f1");
		fc.setType(FieldType.email);
		addField(tc, fc);
		
		this.assertEquals(1, validatorManager.getValidators(tc.getAction("submit")).size());
		this.assertEquals("email", validatorManager.getValidators(tc.getAction("submit")).get(0).getType());
	}

	public void testUrlValidator() {
		FieldConfImpl fc = new FieldConfImpl();
		fc.setName("f1");
		fc.setType(FieldType.url);
		addField(tc, fc);
		
		this.assertEquals(1, validatorManager.getValidators(tc.getAction("submit")).size());
		this.assertEquals("url", validatorManager.getValidators(tc.getAction("submit")).get(0).getType());
	}

	public void testPatternValidator() {
		FieldConfImpl fc = new FieldConfImpl();
		fc.setName("f1");
		fc.setPattern("[0-9]{3}");
		addField(tc, fc);
		
		this.assertEquals(1, validatorManager.getValidators(tc.getAction("submit")).size());
		this.assertEquals("regex", validatorManager.getValidators(tc.getAction("submit")).get(0).getType());
	}

	public void testFieldExpressionValidator() {
		ExpressionConf ec = new ExpressionConf();
		ec.setName("f1");
		ec.setFieldName("f1");
		ec.setJava("password == password2");
		ec.setErrormsg("password is not equals to password2");
		tc.addExpression(ec);
		
		ec = new ExpressionConf();
		ec.setName("f2");
		ec.setJava("amount >= amount2");
		tc.addExpression(ec);
		
		this.assertEquals(2, validatorManager.getValidators(tc.getAction("submit")).size());
		this.assertEquals("fieldexpression", validatorManager.getValidators(tc.getAction("submit")).get(0).getType());
		this.assertEquals("password is not equals to password2", 
				validatorManager.getValidators(tc.getAction("submit")).get(0).getDefaultMessage());
		this.assertEquals("expression", validatorManager.getValidators(tc.getAction("submit")).get(1).getType());
	}
	
	private void addField(TransactionConfImpl tc, FieldConfImpl f) {
		FieldConfListImpl input = new FieldConfListImpl();
		input.add(f);
		tc.setInput(input);
		tc.postProcessAfterLoadConfig();
	}
}
