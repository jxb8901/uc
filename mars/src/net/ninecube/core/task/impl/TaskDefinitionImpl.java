/**
 * 
 * created on 2007-4-29
 */
package net.ninecube.core.task.impl;

import net.ninecube.core.config.FieldConf;
import net.ninecube.core.exception.TaskException;
import net.ninecube.core.task.TaskDefinition;

import org.quartz.JobDetail;

/**
 * 
 * @author jxb
 */
public class TaskDefinitionImpl extends JobDetail implements TaskDefinition {
	private String idno;
	private boolean concurrent = false;
	private Class<? extends Runnable> taskClass;
	private FieldConf[] parameters = new FieldConf[0];
	
	public TaskDefinitionImpl() {
		this.setVolatility(true);
		this.setDurability(true);
	}
	
	public void init() {
		if (this.taskClass == null || !Runnable.class.isAssignableFrom(this.taskClass))
			throw new TaskException("task class must implemnets Runnable interface.");
		this.setJobClass(this.concurrent ? DelegatingJob.class : StatefulDelegatingJob.class);
	}
	
	//~ getter and setter
	public String getId() {
		return this.getIdno();
	}
	public FieldConf[] getParameters() {
		return parameters;
	}
	public void setParameters(FieldConf[] parameters) {
		this.parameters = parameters;
	}
	public Class<? extends Runnable> getTaskClass() {
		return taskClass;
	}
	public void setTaskClass(Class<? extends Runnable> taskClass) {
		this.taskClass = taskClass;
	}
	public String getIdno() {
		return idno;
	}
	public void setIdno(String idno) {
		this.idno = idno;
	}
	public boolean isConcurrent() {
		return concurrent;
	}
	public void setConcurrent(boolean concurrent) {
		this.concurrent = concurrent;
	}

}
