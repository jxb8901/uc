/**
 * 
 * created on 2007-6-13
 */
package net.ninecube.core.task;

import java.util.Date;

/**
 * 
 * @author jxb
 */
public interface TaskStatus {
	
	public String getId();

	public TaskDefinition getDetail();
	
	public Runnable getTaskInstance();
	
	public Date getStartDateTime();
	
	public String getStatus();
}
