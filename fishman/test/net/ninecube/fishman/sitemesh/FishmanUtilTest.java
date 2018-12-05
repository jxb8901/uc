/**
 * 
 * created on 2007-1-18
 */
package net.ninecube.fishman.sitemesh;

import net.ninecube.fishman.TagNotEndException;
import junit.framework.TestCase;

/**
 * 
 * @author jxb
 * 
 */
public class FishmanUtilTest extends TestCase {
	
	public void testTemplateNoTag() throws Exception {
		String template = "<html><body>中国</body></html>";
		String instance = "<!-- BeginTemplate name='body' -->instance<!-- EndTemplate -->";
		String excepted = "<!-- BeginTemplate name='body' -->instance<!-- EndTemplate -->";
		char[] result = merge(template, instance);
		this.assertEquals(excepted, new String(result));
		this.assertEquals(excepted.length(), result.length);
	}
	
	public void testInstanceNoTag() throws Exception {
		String template = "begin <!-- BeginTemplate name='body' -->template<!-- EndTemplate --> end";
		String instance = "instance";
		String excepted = "instance";
		char[] result = merge(template, instance);
		this.assertEquals(excepted, new String(result));
		this.assertEquals(excepted.length(), result.length);
	}

	public void testNormal() throws Exception {
		String template = "<html><body><!-- BeginTemplate name='body' --><!-- EndTemplate --></body></html>";
		String instance = "<!-- BeginTemplate name='body' -->中国<!-- EndTemplate -->";
		String excepted = "<html><body><!-- BeginTemplate name='body' -->中国<!-- EndTemplate --></body></html>";
		char[] result = merge(template, instance);
		this.assertEquals(excepted, new String(result));
		this.assertEquals(excepted.length(), result.length);
	}
	
	public void testInstanceTagNotEnd() throws Exception {
		String template = "<html><body><!-- BeginTemplate name='content' --><!-- EndTemplate --></body></html>";
		String instance = "<!-- BeginTemplate name='content' -->中国";
		String excepted = "<html><body><!-- BeginTemplate name='content' -->中国<!-- EndTemplate dummy='true' name='content' --></body></html>";
		char[] result = merge(template, instance);
		this.assertEquals(excepted, new String(result));
		this.assertEquals(excepted.length(), result.length);
	}
	
	public void testInstanceTagNotStart() throws Exception {
		String template = "<html><body><!-- BeginTemplate name='content' --><!-- EndTemplate --></body></html>";
		String instance = "中国<!-- EndTemplate -->";
		String excepted = "中国<!-- EndTemplate -->";
		char[] result = merge(template, instance);
		this.assertEquals(excepted, new String(result));
		this.assertEquals(excepted.length(), result.length);
		
		instance = "中国<!-- EndTemplate --><!-- BeginTemplate name='content' -->test<!-- EndTemplate -->";
		excepted = "<html><body><!-- BeginTemplate name='content' -->test<!-- EndTemplate --></body></html>";
		result = merge(template, instance);
		this.assertEquals(excepted, new String(result));
		this.assertEquals(excepted.length(), result.length);
	}
	
	public void testTemplateTagNotEnd() throws Exception {
		String template = "<html><body><!-- BeginTemplate name='body' --></body></html>";
		String instance = "<!-- BeginTemplate name='body' -->中国<!-- EndTemplate -->";
		String excepted = "<html><body><!-- BeginTemplate name='body' -->中国<!-- EndTemplate --></body></html>";
		try {
			char[] result = merge(template, instance);
			this.assertEquals(excepted, new String(result));
			this.assertEquals(excepted.length(), result.length);
		} catch (TagNotEndException e) {
			this.assertTrue(e.isTemplate());
		}
	}
	
	public void testTagNotInInstance() throws Exception {
		String template = "<html><body><!-- BeginTemplate name='content' --><!-- EndTemplate -->"+
						"<!-- BeginTemplate name='comment' -->default<!-- EndTemplate -->" +
						"</body></html>";
		String instance = "<!-- BeginTemplate name='content' -->中国<!-- EndTemplate -->";
		String excepted = "<html><body><!-- BeginTemplate name='content' -->中国<!-- EndTemplate -->"+
						"<!-- BeginTemplate name='comment' -->default<!-- EndTemplate -->" +
						"</body></html>";
		char[] result = merge(template, instance);
		this.assertEquals(excepted, new String(result));
		this.assertEquals(excepted.length(), result.length);
	}

	private static char[] merge(String t, String i) throws Exception {
		return FishManUtil.mergeTemplate(new FishManPage(t.toCharArray()), new FishManPage(i.toCharArray())).getData();
	}
}
