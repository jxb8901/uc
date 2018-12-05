package net.ninecube.saturn.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ninecube.saturn.Context;

public class AssignmentStatement extends Function {
	private String operator;

	public AssignmentStatement(int id) {
		super(id);
	}

	public AssignmentStatement(RuleParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(RuleParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Override
	String getFunctionName() {
		return getOperator();
	}
	
	protected int getParameterNodeStartIndex(){
		return 0;
	}
	
	@Override
	List getIndexArguments(Context context){
		List<Object> result = new ArrayList<Object>();
		SimpleNode node = (SimpleNode)jjtGetChild(0);
		result.add(node.execute(context));
		node = (SimpleNode)jjtGetChild(1);
		result.add(node.execute(context));
		
		return result;
	}
	
	@Override
	Map getNamedArguments(Context context){
		Map map = new HashMap();
		
		return map;
	}
	
}
