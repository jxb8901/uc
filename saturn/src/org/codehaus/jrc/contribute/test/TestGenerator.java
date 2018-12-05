package org.codehaus.jrc.contribute.test;

import org.codehaus.jrc.ast.AstHandlerMap;
import org.codehaus.jrc.codegen.AstPrinter;
import org.codehaus.jrc.codegen.CodeBuilder;
import org.codehaus.jrc.codegen.mssql.MsSqlGenerator;
import org.codehaus.jrc.contribute.SqlGenerator;
import org.codehaus.jrc.contribute.hsql.HsqlGenerator;
import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.predicate.Predicate;
import org.codehaus.jrc.relation.Relation;

public class TestGenerator extends HsqlGenerator implements SqlGenerator {
	public TestGenerator(){
	}
	
	public TestGenerator(CodeBuilder builder, AstHandlerMap<Object> handlers) {
		super(builder, handlers);
	}
	
	/**
	 * 不打印换行符
	 */
	@Override
	public AstPrinter println() {
		return this;
	}

	public String getSql(Relation r) {
		final CodeBuilder builder = new CodeBuilder();
		generateRelation(r, builder, getDefaultAstHandlers(builder));
		return trimWhiteSpaceAndLinebreak(builder.toString());
	}
	
	public String getSql(Expression e) {
		final CodeBuilder builder = new CodeBuilder();
		e.accept(new TestGenerator(builder, getDefaultAstHandlers(builder)));
		return trimWhiteSpaceAndLinebreak(builder.toString());
	}
	
	public String getSql(Predicate p) {
		final CodeBuilder builder = new CodeBuilder();
		p.accept(new TestGenerator(builder, getDefaultAstHandlers(builder)));
		return trimWhiteSpaceAndLinebreak(builder.toString());
	}
	
	protected static String trimWhiteSpaceAndLinebreak(String s) {
		s = s.replaceAll("[\\s]+", " ");
		s = s.replaceAll("[\r\n]", "");
		return s;
	}
}
