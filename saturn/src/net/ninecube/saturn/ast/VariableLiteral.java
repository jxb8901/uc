package net.ninecube.saturn.ast;

import java.math.BigDecimal;
import java.util.List;

import net.ninecube.saturn.Context;
import net.ninecube.saturn.exception.RuleSyntaxException;
import net.ninecube.util.DataTypeUtil;

public class VariableLiteral extends SimpleNode {
	private String name;

	public VariableLiteral(int id) {
		super(id);
	}

	public VariableLiteral(RuleParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(RuleParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void validate(Context context, List<Exception>exceptions){
		if(context.getVariables().get(name) == null) 
			exceptions.add(new RuleSyntaxException(getBeginLine(),getBeginColumn(),"找不到变量参数'" + name + "'"));
	}

	@Override
	public Object execute(Context context){
		if(context.getVariables().get(name) == null) throw new IllegalStateException("Cann't find the variable : " + name);
		if(context.getVariables().get(name).toString().startsWith("0")) return context.getVariables().get(name);
		BigDecimal val = DataTypeUtil.obj2BigDecimal(context.getVariables().get(name));
		if(val != null) return val;
		return context.getVariables().get(name);
	}

}
