package net.ninecube.saturn.ast;

import net.ninecube.saturn.Context;
import net.ninecube.saturn.database.jrc.JrcUtil;

import org.codehaus.jrc.predicate.LogicalOperator;
import org.codehaus.jrc.predicate.Predicate;
import org.codehaus.jrc.predicate.Predicates;

public class AndExpression extends SimpleNode {
	public AndExpression(int id) {
		super(id);
	}

	public AndExpression(RuleParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(RuleParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public LogicalOperator getOperator() {
		return LogicalOperator.And;
	}

	public Predicate execute(Context context){
		Predicate result = null;
		for(int i = 0 ; i < jjtGetNumChildren() ; i++){
			SimpleNode node = (SimpleNode) jjtGetChild(i);
			result = JrcUtil.and(result, (Predicate)node.execute(context));
		}
		return result;
	}

	protected Boolean cal(Boolean loperand, Boolean roperand) {
		boolean result = false;
		

		return new Boolean(result);
	}
}
