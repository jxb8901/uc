/**
 * created on 2006-4-8
 */
package net.ninecube.formula;


/**
 * @author JXB
 */
public interface ResultCollector {
	
	/**
	 * 取公式的计算结果
	 * @return 计算结果，如果未曾计算返回null
	 */
	public Result getResult(Context context);

	/**
	 * 添加公式计算结果（以便后续统一保存）
	 */
	public void addResult(Context context, Result result);
}
