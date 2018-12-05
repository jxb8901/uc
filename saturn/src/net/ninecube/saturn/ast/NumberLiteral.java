package net.ninecube.saturn.ast;

import java.math.BigDecimal;

import net.ninecube.saturn.Context;

public class NumberLiteral extends SimpleNode {
	private BigDecimal literal;

	public NumberLiteral(int id) {
		super(id);
	}

	public NumberLiteral(RuleParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(RuleParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public BigDecimal getLiteral() {
		return literal;
	}

	public void setLiteral(BigDecimal literal) {
		this.literal = literal;
	}
	
	public BigDecimal execute(Context context){
		return getLiteral();
	}
	
}
