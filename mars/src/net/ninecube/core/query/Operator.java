/**
 * 
 * created on 2007-1-12
 */
package net.ninecube.core.query;

/**
 * 
 * @author jxb
 * 
 */
public enum Operator {
	//~ 比较操作符
	eq("="),
	gt(">"),
	ge(">="),
	lt("<"),
	le("<="),
	ne("<>"),
	like("like"),
	between("between"),
	//~ 排序操作符
	asc("asc"),
	desc("desc");
	
	private String op;
	Operator(String op) {
		this.op = op;
	}
	
	public static Operator from(String op) {
		if (op.equals(">=")) return ge;
		if (op.equals(">")) return gt;
		if (op.equals("<=")) return le;
		if (op.equals("<")) return lt;
		if (op.equals("%")) return like;
		if (op.equals("=")) return eq;
		if (op.equals("asc")) return asc;
		if (op.equals("desc")) return desc;

		return Operator.valueOf(op);
	}
	
	public boolean isOrderOperator() {
		return asc.equals(this) || desc.equals(this);
	}
	
	public String toSql() {
		return op;
	}
}
