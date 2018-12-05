/**
 * 
 * created on 2007-6-27
 */
package net.ninecube.core.config.impl;

import net.ninecube.core.config.FieldType;
import net.ninecube.core.config.TransactionConf;

/**
 * 
 * @author jxb
 */
public class TransactionConfigFixture {
	public WebConfigImpl config;
	public PackageConfImpl pc, pc2;
	public TransactionConfImpl tc1, tc2, tc3, tc4, tc23;
	public static final String TEST_FIELD_NAME = "creationDate";
	public static final String TEST_FIELD_NAME1 = "creator";

	public TransactionConfigFixture() {
		config = new WebConfigImpl("test", "test");
		
		pc = new PackageConfImpl(config, "user", "用户管理", "net.ninecube", "/views");
		ModelConfImpl mc = new ModelConfImpl(pc);
		FieldConfImpl fc = new FieldConfImpl();
		fc.setType(FieldType.date);
		fc.setName(TEST_FIELD_NAME);
		fc.setCname("创建日期");
		mc.addField(fc);
		fc = new FieldConfImpl();
		fc.setType(FieldType.string);
		fc.setName(TEST_FIELD_NAME1);
		fc.setCname("创建人");
		mc.addField(fc);
		pc.setModel(mc);

		tc1 = new TransactionConfImpl(pc, "add", "add");
		tc2 = new TransactionConfImpl(pc, "edit", "edit");
		tc3 = new TransactionConfImpl(pc, "query", "query");
		tc4 = new TransactionConfImpl(pc, "update", "update");
		tc1.postProcessAfterLoadConfig();
		tc2.postProcessAfterLoadConfig();
		tc3.postProcessAfterLoadConfig();
		tc4.postProcessAfterLoadConfig();
		
		pc2 = new PackageConfImpl(config, "customer", "客户管理", "net.ninecube", "/views");
		tc23 = new TransactionConfImpl(pc2, "query", "query");
	}
}
