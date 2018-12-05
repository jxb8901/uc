/**
 * created on 2006-4-6
 */
package net.ninecube.formula;

import java.util.Map;

import net.ninecube.formula.exception.FormulaNotFoundException;
import net.ninecube.formula.exception.FormulaSyntaxException;
import net.ninecube.lang.Frequence;

/**
 * 公式类，公式可以是一个函数或者是一个符合语法定义的表达式公式
 * @author JXB
 */
public interface Formula extends Evaluable {
	
	/** 指标未指定维度时，维度字段的默认值 */
	public static final String DEFAULT_DIMENSION_VALUE = "*";

	/**
	 * 公式ID，可作为数据库记录标识，保存公式值时可能使用
	 */
	public Long getId();
	
	/**
	 * 公式名称，必须唯一，在公式中引用其它公式时使用名称进行引用
	 */
	public String getName();
	
	/**
	 * 取得公式的字符串值
	 */
	public String getFormula();
	
	/**
	 * 取公式的计算频度
	 */
	public Frequence getFrequence();
	
	/**
	 * 计算目标对象类型
	 */
	public String getTargetType();
	
	/**
	 * 取公式属性集
	 */
	public Map<String, String> getProperties();
	
	/**
	 * 按名称取公式属性
	 */
	public String getProperty(String name);
	
	/**
	 * 取计算级别
	 */
	public int getEvalLevel();
	
	/**
	 * 取公式所引用的公式名称
	 */
	public String[] getReferences();
	
	/**
	 * 对给定的公式进行求值
	 * @param context 求值上下文
	 * @return 
	 */
	public Result eval(Context context);

	/**
	 * 根据context中的Variables检查参数有效性
	 * @param context
	 */
	public void validate(Context context) throws FormulaSyntaxException, FormulaNotFoundException;
	
	

}
