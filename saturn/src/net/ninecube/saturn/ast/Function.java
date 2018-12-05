package net.ninecube.saturn.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ninecube.saturn.Context;
import net.ninecube.saturn.exception.RuleSyntaxException;
import net.ninecube.saturn.function.FunctionDefinition;
import net.ninecube.saturn.function.FunctionProvider;

import org.apache.log4j.Logger;

public class Function extends SimpleNode {
	private Logger log = Logger.getLogger(Function.class);
	private FunctionDefinition fundef;

	public Function(int id) {
		super(id);
	}

	public Function(RuleParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(RuleParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public Object execute(Context context) {
		String funname = getFunctionName();
		log.debug("invoke function '" + funname + "'");
		Object ret = null;
		ret = fundef.getFunction().execute(context, getIndexArguments(context),
				getNamedArguments(context));
		return ret;
	}
	
	
	public void validate(Context context, List<Exception> exceptions) {
		initLexicalInfo();
		fundef = FunctionProvider.getInstance().getByName(getFunctionName());
		if (fundef == null) {
			exceptions.add(new RuleSyntaxException(getBeginLine(),
					getBeginColumn(), "不支持的函数'" + getFunctionName() + "'"));
		}
		for (int i = getParameterNodeStartIndex(); i < jjtGetNumChildren(); i++) {
			((SimpleNode) jjtGetChild(i)).validate(context, exceptions);
		}
	}	
	
	protected void initLexicalInfo(){
		Name name = (Name) jjtGetChild(0);
		setBeginLine(name.getBeginLine());
		setBeginColumn(name.getBeginColumn());
	}
	
	protected int getParameterNodeStartIndex(){
		return 1;
	}

	List getIndexArguments(Context context){
		return ((ArgumentList) jjtGetChild(1)).getIndexArguments(context);
	}

	Map getNamedArguments(Context context){
		return ((ArgumentList) jjtGetChild(1)).getMapArgument(context);
	}

	String getFunctionName() {
		String name = "";
		Name node = (Name) jjtGetChild(0);
		name = node.getName();
		return name;
	}

}
