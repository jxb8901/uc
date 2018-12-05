/**
 * 
 * created on 2007-6-13
 */
package net.ninecube.core.task.impl;

import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import net.ninecube.core.exception.TaskException;
import net.ninecube.core.task.TaskDefinition;
import net.ninecube.core.task.TaskStatus;

/**
 * 
 * @author jxb
 */
public class TaskStatusImpl implements TaskStatus {
	public static final String STATUS_EXECUTING = "running";
	public static final String STATUS_COMPLETE = "complete";
	public static final String STATUS_EXCEPTION = "exception";
	public static final String STATUS_VETOED = "vetoed";
	
	private JobExecutionContext context;
	private String status;

	public TaskStatusImpl(JobExecutionContext context) {
		this.context = context;
	}

	public TaskDefinition getDetail() {
		return (TaskDefinition) this.context.getJobDetail();
	}
	
	public Runnable getTaskInstance() {
		return ((DelegatingJob)this.context.getJobInstance()).getTask();
	}
	
	public String getIdno() {
		return this.context.getTrigger().getFireInstanceId();
	}

	public String getId() {
		return this.getIdno();
	}

	public Date getStartDateTime() {
		return this.context.getScheduledFireTime();
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
//		if (this.status != null)
//			this.status += "\r\n" + status;
//		else
			this.status = status;
	}
}
