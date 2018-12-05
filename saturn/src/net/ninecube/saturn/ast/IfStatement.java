package net.ninecube.saturn.ast;

import net.ninecube.saturn.Context;
import net.ninecube.saturn.database.DataSet;
import net.ninecube.saturn.exception.RuleSyntaxException;

import org.apache.log4j.Logger;
import org.codehaus.jrc.predicate.Predicate;
import org.codehaus.jrc.predicate.Predicates;

public class IfStatement extends SimpleNode {
	private Logger log = Logger.getLogger(this.getClass());

	public IfStatement(int id) {
		super(id);
	}

	public IfStatement(RuleParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(RuleParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public Object execute(Context context) {
		DataSet orgDataset = context.getDataSet();
		context.setDataSet(orgDataset.clone());
		try {
			SimpleNode exprNode = (SimpleNode) jjtGetChild(0);
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

			DataSet dataset = context.getDataSet();
			DataSet[] branchDataset = dataset.split(pred);
			context.setDataSet(branchDataset[0]);
			SimpleNode thenNode = (SimpleNode) jjtGetChild(1);
			thenNode.execute(context);
			log.debug("if expr : " + pred + " , child node : "
					+ jjtGetNumChildren());
			if (jjtGetNumChildren() > 2) {
				SimpleNode elseNode = (SimpleNode) jjtGetChild(2);
				context.setDataSet(branchDataset[1]);
				elseNode.execute(context);

			}

		} finally {
			context.setDataSet(orgDataset);
		}

		return null;
	}

}
