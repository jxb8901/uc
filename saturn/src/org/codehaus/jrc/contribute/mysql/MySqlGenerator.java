package org.codehaus.jrc.contribute.mysql;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jrc.ast.AstHandlerMap;
import org.codehaus.jrc.codegen.CodeBuilder;
import org.codehaus.jrc.codegen.QueryBean;
import org.codehaus.jrc.codegen.Relation2Query;
import org.codehaus.jrc.codegen.mssql.MsSqlGenerator;
import org.codehaus.jrc.contribute.SqlGenerator;
import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.predicate.Predicate;
import org.codehaus.jrc.relation.Relation;

public class MySqlGenerator extends MsSqlGenerator implements SqlGenerator{

	public MySqlGenerator(){
		super(null,null);
	}
	
	public MySqlGenerator(CodeBuilder builder, AstHandlerMap<Object> handlers) {
		super(builder, handlers);
	}

	@Override
	protected void generateSubRelation(Relation rel, String alias) {
		final QueryBean innerQuery = Relation2Query.toQuery(rel);
		if (needSubQuery(innerQuery)) {
			print("(");
			generateQuery(innerQuery);
			print(") AS ");
			print("" + (alias == null ? "_sub" : alias) + "");
		} else {
			// rel.accept(new MssqlGenerator(builder, handlers, alias));
			apply(rel, alias);
		}
	}

	public void generateQuery(QueryBean query) {
		if (!needSelect(query) && query.isUnion()) {
			generateUnion(query.getFrom());
			return;
		}
		print("select ");
		generateDistinct(query);
		generateProjectionList(query);
		generateFromClause(query);
		generateWhereClause(query);
		generateGroupByClause(query);
		generateOrderByClause(query);
		generateTop(query);
	}

	protected void generateTop(QueryBean query) {
		final int top = query.getTop();
		if (top >= 0) {
			print(" limit " + top + "  ");
		}
	}

	@Override
	public Object visitLiteral(Object val) {
		if (val instanceof String) {
			print("'");
			print(StringUtils.replace((String) val, "'", "''"));
			print("'");
		} else if (val instanceof Date) {
			print("'");
			print(new java.sql.Date(((Date)val).getTime()).toString());
			// @TODO: 生成的日期格式可能不正确，提交到JRC
			// print(SimpleDateFormat.getDateTimeInstance().format(val));
			print("'");
		} else
			getCodeBuilder().append(val);
		return null;
	}

	private void apply(Relation rel, String alias) {
		rel.accept(this);
		if (alias != null) {
			print(" AS ");
			appendName(alias);
		}
	}

	private static boolean needSubQuery(QueryBean query) {
		return query.isUnion() || needSelect(query);
	}

	protected static boolean needSelect(QueryBean query) {
		return query.hasTop() || query.isDistinct() || query.getGroupbyClause() != null || query.getOrderbyClause() != null || query.getProjected() != null
				|| query.getWhereClause() != null;
	}

	@Override
	protected void appendName(String name) {
		// super.appendName(name);
		print(name);
		// log.debug("append name : " + name);
		// appendAlias();
	}
	
	public String getSql(Relation r) {
		final CodeBuilder builder = new CodeBuilder();
		new MySqlGenerator(builder, getDefaultAstHandlers(builder)).generateRelation(r);
		return builder.toString();
	}
	
	public String getSql(Expression e) {
		final CodeBuilder builder = new CodeBuilder();
		e.accept(new MySqlGenerator(builder, getDefaultAstHandlers(builder)));
		return builder.toString();
	}
	
	public String getSql(Predicate p) {
		final CodeBuilder builder = new CodeBuilder();
		p.accept(new MySqlGenerator(builder, getDefaultAstHandlers(builder)));
		return builder.toString();
	}
	
	
}

