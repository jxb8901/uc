package net.ninecube.saturn.ast;

import java.util.Date;

import net.ninecube.lang.Frequence;
import net.ninecube.lang.FrequenceDate;
import net.ninecube.saturn.Context;
import net.ninecube.saturn.database.DataSet;

public class CycleRuleRegion extends SimpleNode {
	Frequence frequence;

	public CycleRuleRegion(int id) {
		super(id);
	}

	public CycleRuleRegion(RuleParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(RuleParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public Object execute(Context context) {
		// load properties
		SimpleNode child = null;
		frequence = getFrequence(context);
		FrequenceDate freqDate = getFrequenceDate(context);
		DataSet orgds = null;
		try {
			orgds = context.getDataSet();
			if (freqDate != null || context.isInline()) {
				if(! context.isInline()){
					context.setCurrentFrequence(freqDate);
					DataSet rootds = orgds.clone();
					rootds.setDateRange(new Date[] { freqDate.getStartDate(),
							freqDate.getEndDate() });
					context.setDataSet(rootds);
				}
				
//				if(context.getDataSet().getDateRange() == null)
//					throw new IllegalArgumentException("Unexpected state exception !!!");
				int childrennum = jjtGetNumChildren();
				for (int i = getStmtStartIndex(); i < childrennum; i++) {
					child = (SimpleNode) jjtGetChild(i);
					child.execute(context);
				}
			}

		} finally {
			context.setDataSet(orgds);
		}
		return null;
	}

	private FrequenceDate getFrequenceDate(Context context) {
		for (FrequenceDate f : context.getFrequenceDates())
			if (frequence.equals(f.getFrequence()))
				return f;
		return null;
	}

	protected Frequence getFrequence(Context context) {
		SimpleNode child = (SimpleNode) jjtGetChild(0);
		frequence = (Frequence) child.execute(context);
		return frequence;
	}

	protected int getStmtStartIndex() {
		return 1;
	}

}
