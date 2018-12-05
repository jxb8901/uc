package net.ninecube.saturn.ast;

import net.ninecube.lang.Frequence;
import net.ninecube.saturn.Context;

public class RegularRuleRegion extends CycleRuleRegion {
	public static final Frequence DEFAULT_FREQUENCE = Frequence.DAY;

	public RegularRuleRegion(int id) {
		super(id);
	}

	public RegularRuleRegion(RuleParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(RuleParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	@Override
	protected Frequence getFrequence(Context context) {
		return DEFAULT_FREQUENCE;
	}

	@Override
	protected int getStmtStartIndex() {
		return 0;
	}

}
