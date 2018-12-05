package org.codehaus.jrc.contribute.hsql;

import org.codehaus.jrc.ast.AstHandlerMap;
import org.codehaus.jrc.codegen.CodeBuilder;
import org.codehaus.jrc.codegen.QueryBean;
import org.codehaus.jrc.contribute.mysql.MySqlGenerator;
import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.predicate.Predicate;
import org.codehaus.jrc.relation.Relation;

public class HsqlGenerator extends MySqlGenerator {
	
	public HsqlGenerator(){
		
	}
	
	public HsqlGenerator(CodeBuilder builder, AstHandlerMap<Object> handlers) {
		super(builder, handlers);
	}

	@Override
	public void generateQuery(QueryBean query) {
		if (!needSelect(query) && query.isUnion()) {
			generateUnion(query.getFrom());
			return;
		}
		print("select ");
		generateDistinct(query);
		generateTop(query);
		generateProjectionList(query);
		generateFromClause(query);
		generateWhereClause(query);
		generateGroupByClause(query);
		generateOrderByClause(query);
	}

	@Override
	protected void generateTop(QueryBean query) {
		final int top = query.getTop();
		if (top >= 0) {
			print(" top " + top + "  ");
		}
	}
	
	public String getSql(Relation r) {
		final CodeBuilder builder = new CodeBuilder();
		new HsqlGenerator(builder, getDefaultAstHandlers(builder)).generateRelation(r);
		return builder.toString();
	}
	
	public String getSql(Expression e) {
		final CodeBuilder builder = new CodeBuilder();
		e.accept(new HsqlGenerator(builder, getDefaultAstHandlers(builder)));
		return builder.toString();
	}
	
	public String getSql(Predicate p) {
		final CodeBuilder builder = new CodeBuilder();
		p.accept(new HsqlGenerator(builder, getDefaultAstHandlers(builder)));
		return builder.toString();
	}


}
