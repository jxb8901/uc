package net.ninecube.saturn.ast;

import net.ninecube.saturn.Context;
import net.ninecube.saturn.database.DataSet;

public class SwitchStatement extends SimpleNode {
	public SwitchStatement(int id) {
		super(id);
	}

	public SwitchStatement(RuleParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(RuleParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

		public Object execute(Context context)  {
		DataSet[] branchDataSets = null;
		DataSet orgDataset = context.getDataSet();
		context.setDataSet(orgDataset.clone());
		try {
			int childrenNum = jjtGetNumChildren();
			for (int i = 0; i < childrenNum; i++) {
				if (i > 0) {
					context.setDataSet(branchDataSets[1]);
				}
				SimpleNode child = (SimpleNode) jjtGetChild(i);
				branchDataSets = (DataSet[]) child.execute(context);
			}

		} finally {
			context.setDataSet(orgDataset);
		}
		return null;
	}

}
