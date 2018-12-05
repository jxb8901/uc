package net.ninecube.saturn.ast;

import net.ninecube.saturn.Context;

public class Start extends SimpleNode {
	public Start(int id) {
		super(id);
	}

	public Start(RuleParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(RuleParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public Object execute(Context context){
		int childrennum = jjtGetNumChildren();
		for (int i = 0; i < childrennum; i++) {
			SimpleNode child = (SimpleNode) jjtGetChild(i);
			child.execute(context);
		}

		return null;
	}
}
