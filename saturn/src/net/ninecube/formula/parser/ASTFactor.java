package net.ninecube.formula.parser;

import java.math.BigDecimal;

import net.ninecube.formula.Context;
import net.ninecube.formula.Result;
import net.ninecube.formula.impl.DefaultResult;

public class ASTFactor extends SimpleNode {
	private String factor;

	private boolean percent = false;

	public ASTFactor(int id) {
		super(id);
	}

	public ASTFactor(FormulaParser p, int id) {
		super(p, id);
	}

	//~

	public Result eval(Context context) {
		BigDecimal b = new BigDecimal(factor);
		return new DefaultResult(context, percent ? b.multiply(new BigDecimal("0.01")) : b);
	}
	
	//~

	public void setFactor(String factor) {
		this.factor = factor;
	}

	public void setPercent(boolean b) {
		this.percent = b;
	}

	public String toString() {
		return "Factor: " + this.factor + (this.percent ? "%" : "");
	}
}
