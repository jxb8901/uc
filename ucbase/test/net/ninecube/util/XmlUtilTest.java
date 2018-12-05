/**
 * 
 * created on 2007-3-16
 */
package net.ninecube.util;

import net.ninecube.lang.TypedMap;
import junit.framework.TestCase;

/**
 * 
 * @author jxb
 * 
 */
public class XmlUtilTest extends TestCase {

	@SuppressWarnings("unchecked")
	public void testObj2xml() {
		TypedMap<String, Object> map1 = new TypedMap<String, Object>();
		map1.put("a", "1234");
		TypedMap<String, Object> map2 = new TypedMap<String, Object>();
		map2.put("b", "1234");
		map1.put("c", map2);
		
		String s = XmlUtil.obj2xml(map1);
		System.out.println(s);
		TypedMap<String, Object> map3 = (TypedMap<String, Object>) XmlUtil.xml2obj(s);
		this.assertEquals(2, map3.size());
		this.assertEquals(1, ((TypedMap)map3.get("c")).size());
		this.assertEquals("1234", map3.get("a"));
	}

}
