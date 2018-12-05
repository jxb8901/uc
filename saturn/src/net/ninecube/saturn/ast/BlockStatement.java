package net.ninecube.saturn.ast;

import net.ninecube.saturn.Context;

public class BlockStatement extends SimpleNode {
	public BlockStatement(int id) {
		super(id);
	}

	public BlockStatement(RuleParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(RuleParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public Object execute(Context context) {
		Object result = null;

		int childrenNum = jjtGetNumChildren();
		for (int i = 0; i < childrenNum; i++) {
			SimpleNode node = (SimpleNode) jjtGetChild(i);
			node.execute(context);
		}

		return result;
	}
}
