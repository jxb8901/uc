package net.ninecube.saturn.ast;

import net.ninecube.saturn.Context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FunctionStatement extends SimpleNode {
	private static Log log = LogFactory.getLog(FunctionStatement.class);

	public FunctionStatement(int id) {
		super(id);
	}

	public FunctionStatement(RuleParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(RuleParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public Object execute(Context context) {
		SimpleNode func = (SimpleNode) jjtGetChild(0);
		func.execute(context);

		return null;
	}

}
