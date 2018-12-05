package net.ninecube.saturn.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ninecube.saturn.Context;

public class ArgumentList extends SimpleNode {
	private List indexArgs;
	private Map mapArgs;
	
	public ArgumentList(int id) {
		super(id);
	}

	public ArgumentList(RuleParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(RuleParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public Map getMapArgument(Context context) {
		if(mapArgs == null){
			mapArgs = new HashMap();
			for(int i = 0 ; i < jjtGetNumChildren() ; i++){
				if(jjtGetChild(i) instanceof NamedArgument){
					NamedArgument na = (NamedArgument)jjtGetChild(i);
					mapArgs.put(na.getName(context), na.execute(context));
				}
			}
		}
		return mapArgs;
	}

	public List getIndexArguments(Context context) {
		if(indexArgs == null){
			indexArgs = new ArrayList();
			for(int i = 0 ; i < jjtGetNumChildren() ; i++){
				if(!( jjtGetChild(i) instanceof NamedArgument)){
					indexArgs.add(((SimpleNode)jjtGetChild(i)).execute(context));
				}
			}
		}
		return indexArgs;
	}

}
