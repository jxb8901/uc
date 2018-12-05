package net.ninecube.saturn.ast;

import java.util.HashMap;
import java.util.Map;

import net.ninecube.saturn.Context;

public class NamedArgumentList extends SimpleNode {
	public NamedArgumentList(int id) {
		super(id);
	}

	public NamedArgumentList(RuleParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(RuleParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	private Map getArguments(Context context){
		Map<Object, Object> result = new HashMap<Object, Object>();
		int childrennum = jjtGetNumChildren();
		for (int i = 0; i < childrennum; i++) {
			NamedArgument arg = (NamedArgument) jjtGetChild(i);
			result.put(arg.getName(context), arg.execute(context));
		}

		return result;
	}

	public Object execute(Context context) {
		return getArguments(context);
	}

}
