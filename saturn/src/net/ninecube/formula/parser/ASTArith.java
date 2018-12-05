package net.ninecube.formula.parser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.ninecube.formula.Context;
import net.ninecube.formula.Result;
import net.ninecube.formula.impl.DefaultResult;

public class ASTArith extends SimpleNode {
	enum Operator { add, sub, mult, div }
	private List<Operator> operators = new ArrayList<Operator>();

	public ASTArith(int id) {
		super(id);
	}

	public ASTArith(FormulaParser p, int id) {
		super(p, id);
	}

	// ~

	public Result eval(Context context) {
		BigDecimal result = super.jjtGetChild(0).eval(context).getValue();
		for (int i = 1; i < super.jjtGetNumChildren(); i++) {
			Result e = super.jjtGetChild(i).eval(context);
			switch (operators.get(i - 1)) {
			case add :
				result = result.add(e.getValue()); break;
			case sub:
				result = result.subtract(e.getValue()); break;
			case mult:
				result = result.multiply(e.getValue()); break;
			case div:
				if (e.getValue().compareTo(BigDecimal.valueOf(0)) == 0) {
					//log.error("除数为0，计算结果不变");
					//this.set(BigDecimal.valueOf(-1)); //@ TODO: 除0时如何处理？
				}
				result = result.divide(e.getValue(), 10, BigDecimal.ROUND_HALF_UP);
			}
		}
		return new DefaultResult(context, result);
	}

	// ~

	public void setOperator(Operator op) {
		operators.add(op);
	}
	
	public String toString() {
		StringBuffer ret = new StringBuffer(super.toString());
		ret.append(this.operators);
		return ret.toString();
	}

}
