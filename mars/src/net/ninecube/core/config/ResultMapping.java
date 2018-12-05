/**
 * 
 * created on 2007-1-22
 */
package net.ninecube.core.config;

/**
 * 
 * @author jxb
 * 
 */
public interface ResultMapping {

	/**
	 * 根据交易信息查找相应的结果视图
	 * 如果返回值以chain:开头，表示结果类型为chain类型
	 * 如果返回值以"/"开头，表示结果视图为绝对路径
	 */
	public String getResultView(TransactionConf transaction, 
			ModelConf model, ActionConf action, String resultcode);

}
