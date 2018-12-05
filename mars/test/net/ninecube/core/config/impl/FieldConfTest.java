/**
 * 
 * created on 2007-1-11
 */
package net.ninecube.core.config.impl;

import java.util.Arrays;

import junit.framework.TestCase;

/**
 * 
 * @author jxb
 * 
 */
public class FieldConfTest extends TestCase {

	public void testClone() {
		FieldConfImpl f = new FieldConfImpl();
		f.setName("test");
		FieldConfImpl nf = f.clone();
		this.assertNotNull(nf);
		this.assertEquals(f.getName(), nf.getName());
		this.assertEquals(0, nf.getAction().size());
		
		f.setAction(Arrays.asList("default", "submit"));
		nf = f.clone();
		this.assertNotNull(nf.getAction());
		this.assertEquals(2, nf.getAction().size());
	}
	
	public void testMerge() {
		FieldConfImpl f1 = new FieldConfImpl();
		f1.setName("test1");
		f1.setReq(true);
		f1.setAction(Arrays.asList("default", "submit"));
		f1.setMergeableFieldNames(Arrays.asList("name", "req", "action"));

		FieldConfImpl f2 = new FieldConfImpl();
		f2.setName("test2");
		f2.setReq(false);
		f2.setLen(10);
		f2.setMergeableFieldNames(Arrays.asList("name", "req", "len"));
		
		FieldConfImpl f3 = f1.merge(f2);
		this.assertEquals(f2.getName(), f3.getName());
		this.assertEquals(f2.isReq(), f3.isReq());
		this.assertEquals(f1.getAction().size(), f3.getAction().size());
		this.assertEquals(f2.getLen(), f3.getLen());
		
		f3 = f2.merge(f1);
		this.assertEquals(f1.getName(), f3.getName());
		this.assertEquals(f1.isReq(), f3.isReq());
		this.assertEquals(f1.getAction().size(), f3.getAction().size());
		this.assertEquals(f2.getLen(), f3.getLen());
	}

}
