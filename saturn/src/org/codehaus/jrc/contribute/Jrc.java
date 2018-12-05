/**
 * 
 * created on 2007-6-23
 */
package org.codehaus.jrc.contribute;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jrc.contribute.factory.ReflectionFunctionFactory;
import org.codehaus.jrc.contribute.hsql.HsqlGenerator;
import org.codehaus.jrc.contribute.mssql.MsSqlGenerator;
import org.codehaus.jrc.contribute.mysql.MySqlGenerator;
import org.codehaus.jrc.contribute.test.TestGenerator;
import org.codehaus.jrc.expression.datefunctions.*;
import org.codehaus.jrc.expression.stringfunctions.*;
import org.codehaus.jrc.parser.SqlParser;
import org.codehaus.jrc.parser.factories.SqlFactory;
import org.codehaus.jrc.parser.factories.SqlFactoryImpl;

/**
 * 
 * @author jxb
 */
public class Jrc {
	private static final SqlFactoryImpl factory;
	private static final SqlParser parser;
	private static String defaultSqlGenerator = "test";
	private static final Map<String, SqlGenerator> sqlgenerators = new HashMap<String, SqlGenerator>();
	static {
		factory = new SqlFactoryImpl();
//		factory.registerFunction("cast", org.codehaus.jrc.expression.cast.CastExpression.class);
		
//		factory.registerFunction("", org.codehaus.jrc.expression.misc.CoalesceFunctionExpression.class);

//		factory.registerFunction("", org.codehaus.jrc.expression.extagg.StddevpAggregate.class);
//		factory.registerFunction("", org.codehaus.jrc.expression.extagg.StddevAggregate.class);
//		factory.registerFunction("", org.codehaus.jrc.expression.extagg.ProductAggregate.class);
//		factory.registerFunction("", org.codehaus.jrc.expression.extagg.VarpAggregate.class);
//		factory.registerFunction("", org.codehaus.jrc.expression.extagg.VarAggregate.class);

		factory.registerFunction("lower", ReflectionFunctionFactory.constructors(ToLowerExpression.class));
		factory.registerFunction("upper", ReflectionFunctionFactory.constructors(ToUpperExpression.class));
		factory.registerFunction("replace", ReflectionFunctionFactory.constructors(ReplaceExpression.class));
		factory.registerFunction("substring", ReflectionFunctionFactory.constructors(SubstringExpression.class));

//		factory.registerFunction("", org.codehaus.jrc.expression.mathfunctions.AbsFunctionExpression.class);
//		factory.registerFunction("", org.codehaus.jrc.expression.mathfunctions.LogFunctionExpression.class);
//		factory.registerFunction("", org.codehaus.jrc.expression.mathfunctions.PowerFunctionExpression.class);
//		factory.registerFunction("", org.codehaus.jrc.expression.mathfunctions.FloorFunctionExpression.class);
//		factory.registerFunction("", org.codehaus.jrc.expression.mathfunctions.Log10FunctionExpression.class);
//		factory.registerFunction("", org.codehaus.jrc.expression.mathfunctions.CeilingFunctionExpression.class);
//		factory.registerFunction("", org.codehaus.jrc.expression.mathfunctions.ExpFunctionExpression.class);
//		factory.registerFunction("", org.codehaus.jrc.expression.mathfunctions.SqrtFunctionExpression.class);
		
		factory.registerFunction("datepart", ReflectionFunctionFactory.constructors(DatePartExpression.class));
		factory.registerFunction("datediff", ReflectionFunctionFactory.constructors(DateDiffExpression.class));
		factory.registerFunction("dateadd", ReflectionFunctionFactory.constructors(DateAddExpression.class));
		factory.registerFunction("day", ReflectionFunctionFactory.methods(new SimpleDatePartFunctions(DatePart.day), "datepart"));
		factory.registerFunction("month", ReflectionFunctionFactory.methods(new SimpleDatePartFunctions(DatePart.month), "datepart"));
		factory.registerFunction("year", ReflectionFunctionFactory.methods(new SimpleDatePartFunctions(DatePart.year), "datepart"));
		factory.registerFunction("week", ReflectionFunctionFactory.methods(new SimpleDatePartFunctions(DatePart.week), "datepart"));

		parser = SqlParser.getInstance(factory);
		
		registerSqlGenerator("mysql", new MySqlGenerator());
		registerSqlGenerator("mssql", new MsSqlGenerator());
		registerSqlGenerator("hsql", new HsqlGenerator());
		registerSqlGenerator("test", new TestGenerator());
	}
	
	public static void registerSqlGenerator(String name, SqlGenerator sqlgenerator) {
		sqlgenerators.put(name, sqlgenerator);
	}
	
	public static SqlFactory getSqlFactory() {
		return factory;
	}

	public static SqlParser getParser() {
		return parser;
	}

	public static SqlGenerator getSqlGenerator() {
		return getSqlGenerator(defaultSqlGenerator);
	}
	
	public static SqlGenerator getSqlGenerator(String type) {
		return sqlgenerators.get(type);
	}
	
	public static String setDefaultSqlGeneratorName(String name) {
		if (!sqlgenerators.containsKey(name)) 
			throw new IllegalArgumentException("invalid sql generator type: '" + name + "'");
		String old = defaultSqlGenerator;
		defaultSqlGenerator = name;
		return old;
	}
}
