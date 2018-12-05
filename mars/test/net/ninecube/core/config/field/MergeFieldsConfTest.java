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
public class MergeFieldsConfTest extends TestCase {

	private FieldSource model;
	
	public void setUp() throws Exception {
		FieldConfTestFixture fixture = new FieldConfTestFixture();
		this.model = fixture.model;
	}
	
	public void testMergeValue() {		
		MergeFieldsConf f1 = MergeFieldsConf.newMerge();
		f1.setName("static");
		f1.setValue("320");
		f1.setMergeableFieldNames(Arrays.asList("name", "value"));
		List<FieldConf> result = f1.resolve(model);
		this.assertEquals(1, result.size());
		this.assertEquals("320", result.get(0).getValue());
		this.assertEquals("静态字段", result.get(0).getCname());
	}
	
	public void testMergeKey() {
		MergeFieldsConf f = MergeFieldsConf.DEFAULT_INPUT_MERGEKEY;
		List<FieldConf> list = f.resolve(model);
		this.assertEquals(1, list.size());
		this.assertEquals("id", list.get(0).getName());
		this.assertEquals(FieldTagType.hidden, list.get(0).getTagtype());

		f = MergeFieldsConf.DEFAULT_OUTPUT_MERGEKEY;
		list = f.resolve(model);
		this.assertEquals(1, list.size());
		this.assertEquals("id", list.get(0).getName());
		this.assertEquals(FieldTagType.radio, list.get(0).getTagtype());
	}
}
