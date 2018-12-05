/**
 * 
 * created on 2006-11-30
 */
package net.ninecube.saturn;

import java.util.Map;

/**
 * 
 * @author jxb
 * @name 按比例积分
 * @parameter Field:积分类型
 * @parameter
 */
public interface FunMetaData {
	public static enum WorkMode{
		 batch//批量模式
		,realtime;//实时模式
	}

	public Map getAttributes();
	

	/**
	 * 
	 * @return 函数的工作模式
	 */
	public WorkMode getWorkMode();
	
	/**
	 * 
	 * @return 返回true如果该函数支持条件predicate,该方法仅在批量模式下有效
	 */
	public boolean isSuportPredicate();

	/**
	 * 
	 * 对于批量计算模式下,可应用于If条件中的函数,其应返回一个String类型的Predicate表达式,如"indicator=200"
	 * 
	 * @param context
	 * @return
	 */
	public Object execute(Context context);

	/**
	 * 该函数主要用于语法检查
	 * 
	 * @return
	 */
	public Class getReturnType();

}
