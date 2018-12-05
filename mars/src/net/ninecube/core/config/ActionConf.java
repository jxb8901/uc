/**
 * 
 * created on 2007-1-19
 */
package net.ninecube.core.config;

import java.util.List;

import net.ninecube.core.query.QueryCondition;
import net.ninecube.util.DynamicBean;

/**
 * 
 * @author jxb
 * 
 */
public interface ActionConf {
	
	public String getName();
	
	public String getCname();

	public String getFullName();

	public TransactionConf getOwner();

	public List<String> getInterceptors();

	public String getResultView(String resultcode);
	
	/**
	 * 取默认结果页，当getResultView返回的结果页面不存在时调用
	 * 返回null表示不支持默认视图
	 */
	public String getDefaultResultView(String resultcode);
	
	//~ validate

	public List<? extends ExpressionConf> getExpressions();

	public List<? extends FieldConf> getFieldValidators();
	
	//~ input view

	public List<? extends FieldConf> getInputStaticFields();

	public List<? extends FieldConf> getInputViewFields();
	
	public List<? extends FieldConf> getInputDataFields();
	
	public DynamicBean newInputBean();
	
	public FieldConf getInputField(String name);
	
	//~ query
	
	public List<? extends QueryCondition> getQueryFields();
	
	//~ output
	
	public List<? extends FieldConf> getOutputViewFields();
	
	public FieldConf getOutputField(String name);
	
	//~ entity

	public Class getEntityClass();
	
	public String getEntityName();
	
	public String getEntityManagerName();
	
	public String getKeyFieldName();
}
