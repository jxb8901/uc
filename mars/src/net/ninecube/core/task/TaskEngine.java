/**
 * 
 * created on 2007-4-29
 */
package net.ninecube.core.task;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author jxb
 */
public interface TaskEngine {
	
	public TaskDefinition getTask(String id);
	
	public List<? extends TaskDefinition> getTasks();
	
	public List<? extends TaskStatus> getExecutingTasks();

	public void executeImmediately(String taskId, Map<String, Object> params);
}
