/**
 * 
 * created on 2007-1-24
 */
package net.ninecube.core.config.field;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import net.ninecube.core.config.FieldConf;
import net.ninecube.core.config.FieldSource;
import net.ninecube.core.config.FieldTagType;

/**
 * 
 * @author jxb
 * 
 */
public class IncludeFieldsConfTest extends TestCase {

	private FieldSource model;
	
	public void setUp() throws Exception {
		FieldConfTestFixture fixture = new FieldConfTestFixture();
		this.model = fixture.model;
	}
	
	public void testMergeValue() {
		IncludeFieldsConf f = new IncludeFieldsConf();
		f.setValue("*");
		List<FieldConf> result = f.resolve(model);
		this.assertEquals(5, result.size());
		this.assertEquals(null, result.get(1).getValue());
		this.assertEquals("310", result.get(2).getValue());
		this.assertEquals("ID", result.get(1).getCname());
	}
	
	public void testMergeEnumType() {
		IncludeFieldsConf f = new IncludeFieldsConf();
		f.setValue("id,enumField");
		this.assertEquals(FieldTagType.textarea, f.resolve(model).get(1).getTagtype());
		f.setTagtype(FieldTagType.auto); 
		f.setMergeableFieldNames(Arrays.asList("tagtype", "value"));
		this.assertEquals(FieldTagType.auto, f.resolve(model).get(1).getTagtype());
	}

	public void testMergeModelField() {
		IncludeFieldsConf f = new IncludeFieldsConf();
		f.setValue("customer.id,customer.name");
		List<FieldConf> result = f.resolve(model);
		this.assertEquals(2, result.size());
		
	}
}
