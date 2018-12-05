/**
 * 
 * created on 2006-12-24
 */
package net.ninecube.core.config.impl;

import java.util.Arrays;

import junit.framework.TestCase;
import net.ninecube.core.config.FieldTagType;
import net.ninecube.core.config.FieldType;
import net.ninecube.core.config.field.FieldConfListImpl;
import net.ninecube.core.config.field.IncludeFieldsConf;
import net.ninecube.core.config.field.MergeFieldsConf;
import net.ninecube.core.config.field.QueryFieldsConfImpl;
import net.ninecube.core.query.Operator;
import net.ninecube.util.StringUtil;

/**
 * 
 * @author jxb
 * 
 */
public class TransactionConfTest extends TestCase {
	private TransactionConfigFixture fixture;
	
	public void setUp() {
		fixture = new TransactionConfigFixture();
	}

	// TODO: 进一步测试完善testGetResultView
	public void testGetResultView() {
		this.assertEquals("/views/user/query", fixture.tc3.getAction("").getResultView("success"));
		this.assertEquals("/views/user/query", fixture.tc3.getAction("").getResultView("input"));
		// TODO: 对error结果的处理：actual: /views/package/query
		// this.assertEquals("/views/error", fixture.tc3.getAction("").getResultView("error"));
	}

	// TODO: 继承实现与测试
	public void xxtestGetExtendTransaction() {
		this.assertNull(fixture.tc1.getExtendTransaction());
		this.assertEquals("net.ninecube.package.Add", fixture.tc1.getTransactionClassName());
		this.assertEquals("/views/package/add.submit", fixture.tc1.getAction("submit").getResultView("success"));
		
		fixture.tc1.setExtend("package.edit");
		fixture.tc1.setInherit(StringUtil.split("all"));
		this.assertEquals(fixture.tc2, fixture.tc1.getExtendTransaction());
		this.assertEquals("net.ninecube.package.Edit", fixture.tc1.getTransactionClassName());
		this.assertEquals("/views/package/edit.submit", fixture.tc1.getAction("submit").getResultView("success"));

		fixture.tc1.setExtend("edit");
		this.assertEquals(fixture.tc2, fixture.tc1.getExtendTransaction());
		this.assertEquals("net.ninecube.package.Edit", fixture.tc1.getTransactionClassName());
		this.assertEquals("/views/package/edit.submit", fixture.tc1.getAction("submit").getResultView("success"));
	}
	
	public void testGetOutputViewFieldsByDefault() {
		this.assertEquals(2, fixture.tc1.getAction("").getOutputViewFields().size());
	}
	
	public void testGetInputFields() {
		// 全部为空时，输入字段取model中的字段
		int exceptedFields = 2;
		this.assertEquals(exceptedFields, fixture.tc1.getAction("submit").getInputViewFields().size());
		this.assertEquals(0, fixture.tc1.getAction("submit").getInputStaticFields().size());
		
		// TODO：从fixture.tc2中继承一个输入字段
		FieldConfListImpl mc = new FieldConfListImpl();
//		mc.add(new FieldConfImpl());
//		fixture.tc2.setInput(mc);
//		fixture.tc1.setExtend("package.edit");
//		fixture.tc1.setInherit(StringUtil.split("all"));
//		exceptedFields += 1; // 继承了一个
//		this.assertEquals(exceptedFields, fixture.tc1.getAction("submit").getInputFields().size());
		
		// 本身也设置了包含字段
		IncludeFieldsConf f = new IncludeFieldsConf();
		f.setValue(fixture.TEST_FIELD_NAME);
		mc.add(f);
		fixture.tc1.setInput(mc);
		fixture.tc1.postProcessAfterLoadConfig();
		exceptedFields = 1; // 本身的一个字段，同时原来默认的字段将无效
		this.assertEquals(exceptedFields, fixture.tc1.getAction("submit").getInputViewFields().size());
		this.assertEquals(0, fixture.tc1.getAction("submit").getInputStaticFields().size());
		
		// 本身设置了静态字段
		MergeFieldsConf f1 = MergeFieldsConf.newMerge();
		f1.setName(fixture.TEST_FIELD_NAME1);
		f1.setValue("static value");
		f1.setMergeableFieldNames(Arrays.asList("name", "value"));
		mc.add(f1);
		fixture.tc1.setInput(mc);
		fixture.tc1.postProcessAfterLoadConfig();
		exceptedFields += 0; // 静态字段将不会在getInputViewFields中反映
		this.assertEquals(exceptedFields, fixture.tc1.getAction("submit").getInputViewFields().size());
		this.assertEquals(1, fixture.tc1.getAction("submit").getInputStaticFields().size());

		// 本身也设置了merge字段
		MergeFieldsConf m = MergeFieldsConf.newMerge();
		m.setName(fixture.TEST_FIELD_NAME1);
		m.setTagtype(FieldTagType.hidden);
		m.setMergeableFieldNames(Arrays.asList("name", "tagtype"));
		mc.add(m);
		fixture.tc1.setInput(mc);
		fixture.tc1.postProcessAfterLoadConfig();
		exceptedFields += 1; // 本身的一个字段
		this.assertEquals(exceptedFields, fixture.tc1.getAction("submit").getInputViewFields().size());
		this.assertEquals(fixture.TEST_FIELD_NAME1, fixture.tc1.getAction("submit").getInputViewFields().get(exceptedFields - 1).getName());
		this.assertEquals(FieldTagType.hidden, fixture.tc1.getAction("submit").getInputViewFields().get(exceptedFields - 1).getTagtype());
		this.assertEquals(0, fixture.tc1.getAction("submit").getInputStaticFields().size());
		
		// 本身设置了查询字段<queryfield>
		QueryFieldsConfImpl q = new QueryFieldsConfImpl();
		q.setName(fixture.TEST_FIELD_NAME);
		q.setOperator(Operator.between);
		q.setMergeableFieldNames(Arrays.asList("name", "operator"));
		mc.add(q);
		fixture.tc1.postProcessAfterLoadConfig();
		exceptedFields += 2; // between会生成两个字段
		this.assertEquals(exceptedFields, fixture.tc1.getAction("submit").getInputViewFields().size());
		this.assertEquals(fixture.TEST_FIELD_NAME+"Start", fixture.tc1.getAction("submit").getInputViewFields().get(exceptedFields - 2).getName());
		this.assertEquals(fixture.TEST_FIELD_NAME+"End", fixture.tc1.getAction("submit").getInputViewFields().get(exceptedFields - 1).getName());
		q = new QueryFieldsConfImpl();
		q.setName(fixture.TEST_FIELD_NAME);
		q.setOperator(Operator.like);
		q.setMergeableFieldNames(Arrays.asList("name", "operator"));
		mc.add(q);
		fixture.tc1.postProcessAfterLoadConfig();
		exceptedFields += 1; // like会生成一个字段
		this.assertEquals(exceptedFields, fixture.tc1.getAction("submit").getInputViewFields().size());
		this.assertEquals(0, fixture.tc1.getAction("submit").getInputStaticFields().size());
	}
	
	public void testGetQueryFields() {
		FieldConfListImpl mc = new FieldConfListImpl();
		fixture.tc1.setInput(mc);
		QueryFieldsConfImpl q = new QueryFieldsConfImpl();
		q.setName(fixture.TEST_FIELD_NAME);
		q.setOperator(Operator.between);
		mc.add(q);
		fixture.tc1.postProcessAfterLoadConfig();
		int exceptedFields = 2; // between会生成两个字段
		this.assertEquals(exceptedFields, fixture.tc1.getAction("submit").getQueryFields().size());
		this.assertEquals(fixture.TEST_FIELD_NAME+"Start", fixture.tc1.getAction("submit").getQueryFields().get(0).getName()); // 测试字段名
		this.assertEquals(fixture.TEST_FIELD_NAME+"End", fixture.tc1.getAction("submit").getQueryFields().get(1).getName());
		this.assertEquals(fixture.TEST_FIELD_NAME, fixture.tc1.getAction("submit").getQueryFields().get(0).getEntityName()); // 测试输入字段名
		this.assertEquals(fixture.TEST_FIELD_NAME, fixture.tc1.getAction("submit").getQueryFields().get(1).getEntityName());
		q = new QueryFieldsConfImpl();
		q.setName(fixture.TEST_FIELD_NAME);
		q.setOperator(Operator.like);
		mc.add(q);
		fixture.tc1.postProcessAfterLoadConfig();
		exceptedFields += 1; // like会生成一个字段
		this.assertEquals(exceptedFields, fixture.tc1.getAction("submit").getQueryFields().size());
	}
}
