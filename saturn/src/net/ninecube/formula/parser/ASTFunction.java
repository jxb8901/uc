package net.ninecube.formula.parser;

import net.ninecube.formula.Arguments;
import net.ninecube.formula.Context;
import net.ninecube.formula.Formula;
import net.ninecube.formula.Result;

public class ASTFunction extends SimpleNode {
	private String name;
	private Arguments args;
	private Formula function;
	
	public ASTFunction(int id) {
		super(id);
	}

	public ASTFunction(FormulaParser p, int id) {
		super(p, id);
	}
	
	//~
	
	public Result eval(Context context) {
		Arguments old = context.getVariables();
		context.setVariables(this.getArguments());
		try {
			return function.eval(context);
		} finally {
			context.setVariables(old);
		}
	}
	
	//~
	
	public Arguments getArguments() {
		if (args == null) {
			args = NodeUtil.getArguments(this, false);
		}
		return args;
	}

	public void setFunction(Formula function) {
		this.function = function;
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return "Function: " + this.name + " " + this.getArguments();
	}
}
