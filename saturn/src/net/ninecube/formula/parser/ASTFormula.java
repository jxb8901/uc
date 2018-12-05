package net.ninecube.formula.parser;

import net.ninecube.formula.Arguments;
import net.ninecube.formula.Context;
import net.ninecube.formula.Evaluable;
import net.ninecube.formula.Result;
import net.ninecube.lang.TypedMap;

public class ASTFormula extends SimpleNode implements Evaluable {
	private Arguments args;
	
	//~ constructor
	
	public ASTFormula(int id) {
		super(id);
	}

	public ASTFormula(FormulaParser p, int id) {
		super(p, id);
	}

	// ~

	public Result eval(Context context) {
		return super.jjtGetChild(0).eval(context);
	}
	
	public TypedMap<String, String> getProperties() {
		if (args == null) {
			args = NodeUtil.getArguments(this, true);
		}
		return args;
	}

	public String toString() {
		return "Root " + this.getProperties();
	}

}
