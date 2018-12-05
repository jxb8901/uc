/**
 * 
 * created on 2007-4-29
 */
package net.ninecube.core.task.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ninecube.core.config.FieldConf;
import net.ninecube.core.exception.TaskException;
import net.ninecube.core.exception.TaskNotFoundException;
import net.ninecube.core.task.TaskDefinition;
import net.ninecube.core.task.TaskEngine;
import net.ninecube.core.task.TaskStatus;
import net.ninecube.util.StringUtil;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.StatefulJob;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean.MethodInvokingJob;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean.StatefulMethodInvokingJob;

/**
 * 
 * @author jxb
 */
public class TaskEngineImpl implements TaskEngine {
	
	private Scheduler quartzScheduler;
	private List<TaskDefinitionImpl> definitions = new ArrayList<TaskDefinitionImpl>();
	private TaskStatusManager statusManager = new TaskStatusManager();

	public void init() throws Exception {
		this.quartzScheduler.addJobListener(this.statusManager);
		for (TaskDefinitionImpl task : this.definitions) {
			try {
				this.quartzScheduler.addJob(createJobDetail(task), false);
			} catch (SchedulerException e) {
				throw new TaskException(e);
			}
		}
	}

	public void executeImmediately(String taskId, Map<String, Object> params) {
		TaskDefinition task = this.getTask(taskId);
		try {
			//quartzScheduler.scheduleJob(createJobDetail(task, params), makeImmediateTrigger());
			this.quartzScheduler.triggerJob(task.getName(), task.getGroup(), getJobDataMap(task, params));
		} catch (SchedulerException e) {
			throw new TaskException(e);
		}
	}

	public List<? extends TaskStatus> getExecutingTasks() {
		return this.statusManager.getExecutingTasks();
	}

	public TaskDefinition getTask(String id) {
		for (TaskDefinition td : this.definitions)
			if (td.getId().equals(id)) 
				return td;
		throw new TaskNotFoundException(id);
	}

	public List<? extends TaskDefinition> getTasks() {
		return this.definitions;
	}
	
	public void setTasks(List<TaskDefinitionImpl> definitions) {
		this.definitions = definitions;
	}
	
	public void setQuartzScheduler(Scheduler quartzScheduler) {
		this.quartzScheduler = quartzScheduler;
	}
	
	//~ private method

	protected static Trigger makeImmediateTrigger() {
		return new SimpleTrigger("default1", "default1");
	}
	
	protected JobDataMap getJobDataMap(TaskDefinition task, Map<String, Object> params) {
		JobDataMap ret = (JobDataMap) this.getJobDetail(task).getJobDataMap().clone();
		ret.putAll(filterParameters(task, params));
		return ret;
	}
	
	protected static Map<String, Object> filterParameters(TaskDefinition task, Map<String, Object> params) {
		Map<String, Object> filtered = new HashMap<String, Object>();
		for (FieldConf pd : task.getParameters()) {
			if (!StringUtil.isEmpty(params.get(pd.getName()))) // 去掉空值
				filtered.put(pd.getName(), params.get(pd.getName()));
			else if (!StringUtil.isEmpty(pd.getValue())) // 使用默认值
				filtered.put(pd.getName(), pd.getValue());
		}
		return (filtered);
	}
	
	protected JobDetail createJobDetail(TaskDefinitionImpl task) {
		task.init();
		task.addJobListener(this.statusManager.getName());
		return task;
	}
	
	protected JobDetail getJobDetail(TaskDefinition task) {
		try {
			return this.quartzScheduler.getJobDetail(task.getName(), task.getGroup());
		} catch (SchedulerException e) {
			throw new TaskException(e);
		}
	}
	
	protected MethodInvokingJobDetailFactoryBean getJobDetailFactoryBean(TaskDefinition task) {
		MethodInvokingJobDetailFactoryBean factory = new MethodInvokingJobDetailFactoryBean();
		factory.setBeanName(task.getId());
		factory.setName(task.getName());
		factory.setGroup(task.getGroup());
		factory.setConcurrent(task.isConcurrent());
		factory.setTargetClass(task.getTaskClass());
		try {
			factory.setTargetObject(task.getTaskClass().newInstance());
			factory.afterPropertiesSet();
		} catch (Exception e) {
			throw new TaskException(e);
		}
		return factory;
	}
	
	private void dumpTasks() throws SchedulerException {
		System.out.println("======= jobs =========");
		for (String group : this.quartzScheduler.getJobGroupNames()) {
			System.out.println("" + group);
			for (String name : this.quartzScheduler.getJobNames(group)) {
				System.out.println("\t" + name);
				Trigger[] triggers = this.quartzScheduler.getTriggersOfJob(name, group);
				for (Trigger trigger : triggers) {
					System.out.println("\t\t" + trigger.getFullName());
				}
			}
		}
		System.out.println("======= triggers =========");
		for (String group : this.quartzScheduler.getTriggerGroupNames()) {
			System.out.println("" + group);
			for (String name : this.quartzScheduler.getTriggerNames(group)) {
				System.out.println("\t" + name);
			}
		}
	}
}
