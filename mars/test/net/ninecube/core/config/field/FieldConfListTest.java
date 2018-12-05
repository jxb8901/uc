/**
 * 
 * created on 2007-1-24
 */
package net.ninecube.core.config.field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import net.ninecube.core.config.FieldSource;
import net.ninecube.core.config.impl.FieldConfImpl;

/**
 * 
 * @author jxb
 * 
 */
public class FieldConfListTest extends TestCase {

	private FieldSource model;
	
	public void setUp() throws Exception {
		FieldConfTestFixture fixture = new FieldConfTestFixture();
		this.model = fixture.model;
	}
	
	public void testGetViewFields() {
		FieldConfListImpl list = new FieldConfListImpl();
		list.add(IncludeFieldsConf.DEFAULT_OUTPUT_INCLUDEALL);
		list.add(MergeFieldsConf.DEFAULT_OUTPUT_MERGEKEY);
		list.resolve(this.model, true);
		this.assertEquals(4, list.getViewFields().size());
		list.resolve(this.model, false);
		this.assertEquals(5, list.getViewFields().size());
	}
	
	public void testGetName() {
		FieldConfListImpl list = new FieldConfListImpl();
		IncludeFieldsConf f = new IncludeFieldsConf();
		f.setValue("*");
		list.add(f);
		MergeFieldsConf f1 = MergeFieldsConf.newMerge();
		f1.setName("static");
		f1.setValue("320");
		f1.setMergeableFieldNames(Arrays.asList("name", "value"));
		list.add(f1);
		
		list.resolve(model, true);
		this.assertEquals(4, list.getViewFields().size());
		this.assertEquals(5, list.getDataFields().size());
		this.assertEquals("静态字段", list.getByName("static").getCname());
		this.assertEquals("320", list.getByName("static").getValue());
	}
	
	public void testExcludeAdd() {
		List<FieldConfImpl> list = new ArrayList<FieldConfImpl>();
		list.add(create("test0", "test0"));
		list.add(create("test1", "test1"));
		list.add(create("test2", "test2"));
		list.add(create("test3", "test6")); /* ^ */
		list.add(create("test4", "test4"));
		
		for (int i = 0; i <= 4; i++) {
			this.assertEquals("test"+i, list.get(i).getName());
		}
		FieldConfListImpl.excludeAdd(list, create("test3", "test3"), true);
		FieldConfListImpl.excludeAdd(list, create("test5", "test5"), true);
		for (int i = 0; i <= 5; i++) {
			this.assertEquals("test"+i, list.get(i).getName());
			this.assertEquals("test"+i, list.get(i).getCname());
		}
	}

	private FieldConfImpl create(String name, String cname) {
		FieldConfImpl ret = new FieldConfImpl();
		ret.setName(name);
		ret.setCname(cname);
		return ret;
	}
}
