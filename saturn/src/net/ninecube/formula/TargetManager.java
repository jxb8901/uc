/**
 * created on 2006-4-12
 */
package net.ninecube.formula;

import java.util.List;

/**
 * @author JXB
 */
public interface TargetManager {
	
	public Target getTargetByName(String name, String targetType);
	
	/**
	 * 根据计算对象类型取所有考核对象代码
	 * @param type 计算对象类型，如果为"*"，表示全部
	 */
	public List<Target> getTargetsByType(String type);
	
}
