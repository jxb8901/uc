package net.ninecube.saturn.ast;

import java.util.HashMap;
import java.util.Map;

import net.ninecube.saturn.exception.RuleSyntaxException;



public enum Operator {
	// 加, 减, 乘, 除, 取余,
	PLUS, MINUS, MUL, DIV, MOD, AND, OR, NOT,
	//>,>=,==,!=,<,<=
	GT,GE,EQ,NE,LT,LE
	;

	public static Operator asOperator(String name){
		Operator result = null;
		result = operatorMap.get(name);
		if (result == null)
			throw new RuleSyntaxException("No such operator '" + name + "' .");
		return result;
	}

	private static Map<String, Operator> buildOperatorMap() {
		Map<String, Operator> map = new HashMap<String, Operator>();
		map.put("+", PLUS);
		map.put("-", MINUS);
		map.put("*", MUL);
		map.put("/", DIV);
		map.put("%", MOD);
		map.put("and", AND);
		map.put("or", OR);
		map.put("not", NOT);
		
		map.put(">",GT );
		map.put(">=",GE );
		map.put("==",EQ );
		map.put("!=",NE );
		map.put("<",LT );
		map.put("<=",LE );

		return map;
	}

	private static Map<String, Operator> operatorMap = buildOperatorMap();
}
