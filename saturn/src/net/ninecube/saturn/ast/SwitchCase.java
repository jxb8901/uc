package net.ninecube.saturn.ast;

import net.ninecube.saturn.Context;
import net.ninecube.saturn.database.DataSet;
import net.ninecube.saturn.exception.RuleSyntaxException;

import org.codehaus.jrc.predicate.Predicate;
import org.codehaus.jrc.predicate.Predicates;

public class SwitchCase extends SimpleNode {
	public SwitchCase(int id) {
		super(id);
	}

	public SwitchCase(RuleParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(RuleParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public DataSet[] execute(Context context){
		DataSet[] result = null;
		SimpleNode exprNode = null;
		SimpleNode stmtNode = null;
		if (jjtGetNumChildren() == 1) {
			stmtNode = (SimpleNode) jjtGetChild(0);
			stmtNode.execute(context);
			return null;
		}
		exprNode = (SimpleNode) jjtGetChild(0);
		stmtNode = (SimpleNode) jjtGetChild(1);
		Object exprResult = exprNode.execute(context);
		Predicate pred = null;
		if (exprResult instanceof Boolean) {
			pred = Predicates.always(((Boolean) exprResult).booleanValue());
		} else if (exprResult instanceof Predicate) {
			pred = (Predicate) exprResult;
		} else {
			throw new RuleSyntaxException("invalid if expression val : "
					+ exprResult + " .");
		}

		result = context.getDataSet().split(pred);
		context.setDataSet(result[0]);
		stmtNode.execute(context);

		return result;
	}

}
