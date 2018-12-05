package net.ninecube.formula.parser;

import net.ninecube.formula.Context;
import net.ninecube.formula.Result;

public class ASTArgument extends SimpleNode {
	private String name, value;

	public ASTArgument(int id) {
		super(id);
	}

	public ASTArgument(FormulaParser p, int id) {
		super(p, id);
	}

	public void setArgument(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public Result eval(Context context) {
		return null;
	}

	public String toString() {
		return "Argument: " + name + "=" + value;
	}
}
