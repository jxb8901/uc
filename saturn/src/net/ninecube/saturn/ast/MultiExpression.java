package net.ninecube.saturn.ast;

public class MultiExpression extends AddExpression {

	public MultiExpression(int id) {
		super(id);
	}

	public MultiExpression(RuleParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(RuleParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

}
