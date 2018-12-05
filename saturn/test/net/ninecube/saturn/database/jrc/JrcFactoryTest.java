/**
 * 
 * created on 2007-4-10
 */
package net.ninecube.saturn.database.jrc;

import junit.framework.TestCase;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jrc.contribute.Jrc;
import org.codehaus.jrc.predicate.Predicate;

/**
 * 
 * @author jxb
 * 
 */
public class JrcFactoryTest extends TestCase {

	public void testParse() {
		Predicate p = Jrc.getParser().parsePredicate("a.b = 1");
		System.out.println(ReflectionToStringBuilder.toString(p, ToStringStyle.SHORT_PREFIX_STYLE, true, true, null));
		p = Jrc.getParser().parsePredicate("[a].[b] = 1");
		System.out.println(ToStringBuilder.reflectionToString(p));
		p = Jrc.getParser().parsePredicate("cust_info.id = 1");
		System.out.println(ToStringBuilder.reflectionToString(p));
	}

}
