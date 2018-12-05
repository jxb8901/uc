package org.codehaus.jrc.contribute.mssql;

import org.codehaus.jrc.ast.AstHandlerMap;
import org.codehaus.jrc.codegen.CodeBuilder;
import org.codehaus.jrc.contribute.SqlGenerator;
import org.codehaus.jrc.contribute.mysql.MySqlGenerator;
import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.predicate.Predicate;
import org.codehaus.jrc.relation.Relation;

public class MsSqlGenerator extends org.codehaus.jrc.codegen.mssql.MsSqlGenerator implements SqlGenerator {
	public MsSqlGenerator(){
		super(null, null);
	}
	
	public MsSqlGenerator(CodeBuilder builder, AstHandlerMap<Object> handlers) {
		super(builder, handlers);
	}
	
	public String getSql(Relation r) {
		final CodeBuilder builder = new CodeBuilder();
		generateRelation(r, builder, getDefaultAstHandlers(builder));
		return builder.toString();
	}
	
	public String getSql(Expression e) {
		final CodeBuilder builder = new CodeBuilder();
		e.accept(new MsSqlGenerator(builder, getDefaultAstHandlers(builder)));
		return builder.toString();
	}
	
	public String getSql(Predicate p) {
		final CodeBuilder builder = new CodeBuilder();
		p.accept(new MsSqlGenerator(builder, getDefaultAstHandlers(builder)));
		return builder.toString();
	}
}
