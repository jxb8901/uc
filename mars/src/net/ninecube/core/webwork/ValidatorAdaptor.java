/**
 * 
 * created on 2006-12-19
 */
package net.ninecube.core.webwork;

import java.util.List;

import net.ninecube.core.config.ExpressionConf;
import net.ninecube.core.config.FieldConf;

import com.opensymphony.xwork.validator.ValidatorConfig;

/**
 * 适配器接口以将字段配置转换为webwork的验证器配置
 * TODO: 增加对bool, enumt类型的数据验证支持
 * 
 * @author jxb
 * 
 */
public interface ValidatorAdaptor {

	/**
	 * 将字段配置转换为相应webwork验证器配置
	 * @param fieldconf 要转换的字段
	 * @return 返回字段对应的验证器配置列表
	 */
	public List<ValidatorConfig> adapt(FieldConf fieldconf);
	
	/**
	 * 将表达式配置转换为相应的webwork验证器配置
	 * @param expression 要转换的表达式配置
	 * @return 返回表达式对应的验证器配置
	 */
	public ValidatorConfig adapt(ExpressionConf expression);
}
