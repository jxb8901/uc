package org.codehaus.jrc.contribute;

import org.codehaus.jrc.codegen.CodeBuilder;
import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.predicate.Predicate;
import org.codehaus.jrc.relation.Relation;

public interface SqlGenerator {
		
	public String getSql(Relation r);
	
	public String getSql(Predicate p);
	
	public String getSql(Expression e);
}
