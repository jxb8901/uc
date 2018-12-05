/**
 * created on 2006-4-12
 */
package net.ninecube.formula;

import org.apache.log4j.Logger;

/**
 * 输出计算结果的日志，通常在log4j中另外配置一个appender，以便将
 * 用户信息输入到特定文件
 * @author JXB
 */
public class Log {
	public static final Logger log = Logger.getLogger(Log.class);
}
