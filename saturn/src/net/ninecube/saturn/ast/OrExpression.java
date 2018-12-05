package net.ninecube.saturn.ast;

import net.ninecube.saturn.Context;
import net.ninecube.saturn.database.jrc.JrcUtil;

import org.codehaus.jrc.predicate.LogicalOperator;
import org.codehaus.jrc.predicate.Predicate;

public class OrExpression extends SimpleNode {
	public OrExpression(int id) {
		super(id);
	}

	public OrExpression(RuleParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(RuleParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
	
	public LogicalOperator getOperator(){
		return LogicalOperator.Or;
	}
	
	public Predicate execute(Context context){
		Predicate result = null;
		for(int i = 0 ; i < jjtGetNumChildren() ; i++){
			SimpleNode node = (SimpleNode) jjtGetChild(i);
			result = JrcUtil.and(result, (Predicate)node.execute(context));
		}
		return result;
	}
	
	
}
