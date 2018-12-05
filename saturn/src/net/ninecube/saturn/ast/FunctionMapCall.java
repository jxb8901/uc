package net.ninecube.saturn.ast;

import net.ninecube.saturn.Context;
import net.ninecube.saturn.RuleEngine;
import net.ninecube.saturn.function.FunctionDefinition;
import net.ninecube.saturn.function.FunctionProvider;
import net.ninecube.saturn.impl.RuleEngineImpl;

public class FunctionMapCall extends SimpleNode {
	private String mapping;

	public FunctionMapCall(int id) {
		super(id);
	}

	public FunctionMapCall(RuleParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(RuleParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public String getMapping() {
		return mapping;
	}

	public void setMapping(String mapping) {
		this.mapping = mapping;
	}
	
	public Object execute(Context context) { 
		Object result = null;
		boolean orgIsInline = context.isInline();
		context.setInline(true);
		FunctionProvider funpro = FunctionProvider.getInstance();
		FunctionDefinition fundef = funpro.getByMapping(mapping);
		String funcallRule = fundef.getFunctionCallRule(mapping);
		RuleEngine ruleEngine = RuleEngine.getInstance();
		((RuleEngineImpl)ruleEngine).executeInContext(context, funcallRule);
		context.setInline(orgIsInline);
		return result;
	}

	
}
