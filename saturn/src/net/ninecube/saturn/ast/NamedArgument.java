package net.ninecube.saturn.ast;

import net.ninecube.saturn.Context;

public class NamedArgument extends SimpleNode {

	public NamedArgument(int id) {
		super(id);
	}

	public NamedArgument(RuleParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(RuleParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public Object getName(Context context) {
		Object result = null;
		Name node = (Name) jjtGetChild(0);
		result = node.getName();
		return result;
	}

	public Object execute(Context context) {
		Object result = null;
		SimpleNode node = (SimpleNode) jjtGetChild(1);
		result = node.execute(context);
		return result;
	}

}
