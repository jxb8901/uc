package net.ninecube.saturn.ast;

import net.ninecube.saturn.Context;

public class BooleanLiteral extends SimpleNode {
	private Boolean literal;

	public BooleanLiteral(int id) {
		super(id);
	}

	public BooleanLiteral(RuleParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(RuleParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public Boolean getLiteral() {
		return literal;
	}

	public void setLiteral(Boolean literal) {
		this.literal = literal;
	}

	public Object execute(Context context) {
		return null;
	}
}
