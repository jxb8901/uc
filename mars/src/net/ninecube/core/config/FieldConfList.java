/**
 * 
 * created on 2007-1-26
 */
package net.ninecube.core.config;

import java.util.List;

/**
 * 
 * @author jxb
 * 
 */
public interface FieldConfList {

	/**
	 * 取原始未转换的字段
	 */
	public List<? extends FieldConf> getRawFields();
	
	/**
	 * 取用于验证的字段
	 */
	public List<? extends FieldConf> getValidateFields();
	
	/**
	 * 取静态字段（包含字段值的字段）
	 */
	public List<? extends FieldConf> getStaticFields();
	
	/**
	 * 取查询条件字段
	 */
	public List<? extends FieldConf> getQueryFields();
	
	/**
	 * 取用于页面显示（输入 ／输出）的字段
	 * 不包括静态字段，包括模型字段，但不包括模型引用字段
	 */
	public List<? extends FieldConf> getViewFields();
	
	/**
	 * 取交易输入数据字段
	 * 相当于getValidateFields＋getStaticFields
	 */
	public List<? extends FieldConf> getDataFields();
}
