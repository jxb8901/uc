package net.ninecube.saturn.ast;

import java.util.ArrayList;
import java.util.List;

import net.ninecube.saturn.Context;

public class IndexArgumentList extends SimpleNode {
	public IndexArgumentList(int id) {
		super(id);
	}

	public IndexArgumentList(RuleParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(RuleParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	private List getArguments(Context context) {
		List<Object> args = new ArrayList<Object>();
		int childrennum = jjtGetNumChildren();
		for(int i = 0 ; i < childrennum ; i++){
			SimpleNode node = (SimpleNode)jjtGetChild(i);
			args.add(node.execute(context));
		}
		return args;
	}

	public Object execute(Context context) {
		return getArguments(context);
	}

}
