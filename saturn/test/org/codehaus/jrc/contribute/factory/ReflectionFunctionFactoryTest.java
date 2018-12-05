/**
 * 
 * created on 2007-6-25
 */
package org.codehaus.jrc.contribute.factory;

import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.expression.datefunctions.DateAddExpression;
import org.codehaus.jrc.expression.datefunctions.DatePart;
import org.codehaus.jrc.expression.datefunctions.NowExpression;
import org.codehaus.jrc.expression.stringfunctions.SubstringExpression;
import org.codehaus.jrc.parser.SqlParser;
import org.codehaus.jrc.parser.factories.SqlFactoryImpl;

import junit.framework.TestCase;

/**
 * 
 * @author jxb
 */
public class ReflectionFunctionFactoryTest extends TestCase {
	private SqlFactoryImpl factory;
	private SqlParser parser;
	
	protected void setUp() {
		factory = new SqlFactoryImpl();
		parser = SqlParser.getInstance(factory);
	}

	public void testConstructorsWithNonExpressionTypeArgument() {
		factory.registerFunction("dateadd", ReflectionFunctionFactory.constructors(DateAddExpression.class));
		Expression expr = parser.parseExpression("dateadd(now(), day, 2)");
		this.assertEquals(DateAddExpression.class, expr.getClass());
		DateAddExpression dae = (DateAddExpression) expr;
		this.assertEquals(NowExpression.class, dae.getFrom().getClass());
		this.assertEquals(DatePart.day, dae.getDatePart());
		this.assertEquals("2", dae.getNum().toString());
	}

	public void testConstructorsWithAllExpressions() {
		factory.registerFunction("substring", ReflectionFunctionFactory.constructors(SubstringExpression.class));
		Expression expr = parser.parseExpression("substring(a.b, 1, 2)");
		this.assertEquals(SubstringExpression.class, expr.getClass());
		SubstringExpression se = (SubstringExpression) expr;
		this.assertEquals("a.b", se.getString().toString());
		this.assertEquals("1", se.getStart().toString());
		this.assertEquals("2", se.getLength().toString());
	}
}
