/**
 * 
 * created on 2007-3-30
 */
package net.ninecube.core.config.field;

import net.ninecube.core.config.FieldTagType;
import net.ninecube.core.config.impl.FieldConfImpl;
import net.ninecube.core.config.impl.FieldSourceSupport;

/**
 * 
 * @author jxb
 * 
 */
public class FieldConfTestFixture {

	public FieldSourceSupport model;
	
	public FieldConfTestFixture() {
		model = new FieldSourceSupport();
		FieldConfImpl f = new FieldConfImpl();
		f.setName("name");
		f.setCname("姓名");
		model.addField(f);
		f = new FieldConfImpl();
		f.setName("id");
		f.setCname("ID");
		f.setKey(true);
		model.addField(f);
		f = new FieldConfImpl();
		f.setName("static");
		f.setCname("静态字段");
		f.setValue("310");
		model.addField(f);
		f = new FieldConfImpl();
		f.setName("enumField");
		f.setCname("枚举类型");
		f.setTagtype(FieldTagType.textarea);
		model.addField(f);
		
		final FieldSourceSupport m = new FieldSourceSupport();
		f = new FieldConfImpl();
		f.setName("name");
		f.setCname("姓名");
		m.addField(f);
		f = new FieldConfImpl();
		f.setName("id");
		f.setCname("ID");
		f.setKey(true);
		m.addField(f);
		
		ModelFieldsConf modelfield = new ModelFieldsConf(null, false) {
			public FieldSourceSupport getModel() {
				return m;
			}
		};
		modelfield.setName("customer");
		modelfield.setCname("客户");
		model.addField(modelfield);
	}
}
