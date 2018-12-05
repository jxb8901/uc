/**
 * 
 * created on 2007-3-25
 */
package net.ninecube.saturn.database.jrc;

import junit.framework.TestCase;

import org.codehaus.jrc.contribute.Jrc;
import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.expression.Expressions;

/**
 * 
 * @author jxb
 * 
 */
public class SqlGeneratorTest extends TestCase {

	public void testGetSqlExpression() {
		Expression e = Expressions.literal("str");
		this.assertEquals("'str'", Jrc.getSqlGenerator().getSql(e));
		java.sql.Date d = new java.sql.Date(System.currentTimeMillis()); 
		e = Expressions.literal(d);
		this.assertEquals("'" + d.toString() + "'", Jrc.getSqlGenerator("test").getSql(e));
	}

}
