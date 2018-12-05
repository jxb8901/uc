package net.ninecube.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class ArrayUtilTest extends TestCase {
	
	public void testMerge() {
		String[] s1 = {"1", "2"};
		String[] s2 = {"3", "4"};
		String[] s3 = ArrayUtil.merge(s1, s2);
		this.assertEquals(4, s3.length);
		this.assertEquals(2, s1.length);
		this.assertEquals(2, s2.length);
	}
	
	public void testIn() {
		this.assertTrue(ArrayUtil.in("01", "01,02,03"));
		this.assertTrue(ArrayUtil.in("", "01,,03"));
		this.assertTrue(ArrayUtil.in("", ",02,03"));
		this.assertTrue(ArrayUtil.in("01", new String[]{"01", "02"}));
	}
	
	public void testJoin() {
		this.assertEquals("1,2,3", ArrayUtil.join(new String[]{"1", "2", "3"}));
		this.assertEquals("1,2,3", ArrayUtil.join(new int[]{1, 2, 3}));
	}

	@SuppressWarnings("unchecked")
	public void testAsArray() {
		String s = "1001";
		Object[] arr = ArrayUtil.asArray(s);
		this.assertEquals(1, arr.length);
		this.assertEquals(s, arr[0]);
		
		arr = ArrayUtil.asArray("1,2,3");
		this.assertEquals(3, arr.length);
		
		List list = new ArrayList();
		list.add("1");
		list.add("2");
		arr = ArrayUtil.asArray(list);
		this.assertEquals(2, arr.length);
		
		arr = ArrayUtil.asArray(new Object());
		this.assertEquals(1, arr.length);
		
		String[] ss = new String[]{"01,02,03"};
		arr = ArrayUtil.asArray(ss);
		this.assertEquals(3, arr.length);
		
		arr = ArrayUtil.asArray(new int[]{1, 2, 3});
		this.assertEquals(3, arr.length);
	}

	public static void assertEquals(List expected, List result) {
		TestCase.assertEquals(expected.size(), result.size());
		for (int i = 0;  i<expected.size(); i++) {
			TestCase.assertEquals(expected.get(i), result.get(i));
		}
	}

	public static void assertEquals(Object[] expected, Object[] result) {
		TestCase.assertEquals(expected.length, result.length);
		for (int i = 0;  i<expected.length; i++) {
			TestCase.assertEquals(expected[i], result[i]);
		}
	}

}
