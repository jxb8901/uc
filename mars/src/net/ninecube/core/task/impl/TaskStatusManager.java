/**
 * 
 * created on 2007-6-15
 */
package net.ninecube.core.task.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.ninecube.core.task.TaskEngine;
import net.ninecube.core.task.TaskStatus;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

/**
 * 
 * @author jxb
 */
public class TaskStatusManager implements JobListener {
	private List<TaskStatusImpl> status = Collections.synchronizedList(new ArrayList<TaskStatusImpl>());
	
	public TaskStatusImpl getTaskStatus(String id) {
		for (TaskStatusImpl s : status) {
			if (s.getId().equals(id)) return s;
		}
		return null;
	}
	
	public List<? extends TaskStatus> getExecutingTasks() {
		return this.status;
	}

	public String getName() {
		return "task.listener";
	}

	public void jobExecutionVetoed(JobExecutionContext context) {
		getTaskStatus(context).setStatus(TaskStatusImpl.STATUS_VETOED);
	}

	public void jobToBeExecuted(JobExecutionContext context) {
		getTaskStatus(context).setStatus(TaskStatusImpl.STATUS_EXECUTING);
	}

	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		if (jobException != null) {
			getTaskStatus(context).setStatus(TaskStatusImpl.STATUS_EXCEPTION + ":" + jobException.getMessage());
		}
		else {
			getTaskStatus(context).setStatus(TaskStatusImpl.STATUS_COMPLETE);
		}
	}

	protected TaskStatusImpl getTaskStatus(JobExecutionContext context) {
		String id = context.getTrigger().getFireInstanceId();
		TaskStatusImpl ret = this.getTaskStatus(id);
		if (ret == null)  {
			ret = new TaskStatusImpl(context);
			this.status.add(0, ret);
			// TODO: 在内存中只保存100条任务状态
			while (this.status.size() > 100) {
				this.status.remove(this.status.size() - 1);
			}
		}
		return ret;
	}
}
