/**
 * 
 * created on 2007-6-25
 */
package org.codehaus.jrc.contribute.factory;

import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.factory.ReflectionFactory;
import org.codehaus.jrc.parser.factories.FunctionFactory;

/**
 * 
 * @author jxb
 */
public class ReflectionFunctionFactory implements FunctionFactory {
	private final ReflectionFactory factory;
	
	private ReflectionFunctionFactory(ReflectionFactory factory) {
		this.factory = factory;
	}
	
	public static ReflectionFunctionFactory constructors(Class<?> cls){
		return new ReflectionFunctionFactory(ReflectionFactory.constructors(cls.getName(), cls));
	}
	
	public static <T> ReflectionFunctionFactory methods(Class<? super T> cls, T obj, String method_name){
		return new ReflectionFunctionFactory(ReflectionFactory.methods(cls.getName(), cls, obj, method_name));
	}
	
	public static <T> ReflectionFunctionFactory methods(Class<T> cls, String method_name){
		return new ReflectionFunctionFactory(ReflectionFactory.methods(cls.getName(), cls, method_name));
	}
	
	public static <T> ReflectionFunctionFactory methods(T obj, String method_name){
		return new ReflectionFunctionFactory(ReflectionFactory.methods(obj.getClass().getName(), obj, method_name));
	}

	public Expression function(String schema, String name, Expression[] args) throws Exception {
		return (Expression)factory.create(args);
	}

	public int getParameterCount() {
		return -1;
	}
}
