package net.ninecube.saturn.ast;

import net.ninecube.saturn.Context;

public class StringLiteral extends SimpleNode {
	private String literal;

	public StringLiteral(int id) {
		super(id);
	}

	public StringLiteral(RuleParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(RuleParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public String getLiteral() {
		return literal;
	}

	public void setLiteral(String literal) {
		this.literal = literal;
	}

	public String execute(Context context){
		return literal;
	}

	@Override
	public String toString() {
		String str = super.toString();
		str += "[" + literal + "]";
		return str;
	}
}
