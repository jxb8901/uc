/**
 * 
 * created on 2007-6-13
 */
package net.ninecube.core.exception;

/**
 * 
 * @author jxb
 */
public class TaskNotFoundException extends TaskException {
	private String id;

	public TaskNotFoundException(String id) {
		this.id = id;
	}
	
	public String getMessage() {
		return "找不到任务定义: '" + this.id + "'";
	}

}
