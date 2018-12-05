package net.ninecube.saturn.function;

import java.util.HashMap;
import java.util.Map;

import net.ninecube.saturn.function.impl.FunProportionPoint;
import net.ninecube.saturn.function.impl.FuncAddAssign;
import net.ninecube.saturn.function.operation.PointOperation;

public class FunctionFactory {
	static Map<String, Function> funmap = init();
	
	private static Map<String, Function> init(){
		Map<String, Function> map = new HashMap<String, Function>();
		map.put("按比例积分", new FunProportionPoint());
		map.put("+=", new FuncAddAssign(PointOperation.ADD_ASSIGN_OP));
		map.put("*=", new FuncAddAssign(PointOperation.MUL_ASSIGN_OP));
		
		
		return map;
	}

	public static Function getFunction(String name) {
		return funmap.get(name);
	}
}
