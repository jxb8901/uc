/**
 * 
 * created on 2007-4-29
 */
package net.ninecube.core.task.impl;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import net.ninecube.core.exception.TaskException;
import net.ninecube.core.task.TaskDefinition;
import net.ninecube.core.task.TaskEngine;
import net.ninecube.core.task.TaskStatus;

import org.quartz.Scheduler;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * 
 * @author jxb
 */
public class TaskEngineImplTest extends TestCase {
	private ClassPathXmlApplicationContext beanFactory;
	private TaskEngine tm;
	private Scheduler scheduler;
	private SchedulerFactoryBean schedulerFactory;
	
	protected void setUp() throws Exception {
		beanFactory = new ClassPathXmlApplicationContext(new String[] { 
				"applicationContext-schedule.xml",
				"applicationContext-tasks-test.xml",
				});
		tm = (TaskEngine) beanFactory.getBean("taskEngine");
		scheduler = (Scheduler) beanFactory.getBean("quartzScheduler");
		schedulerFactory = (SchedulerFactoryBean) beanFactory.getBean("&quartzScheduler");
		schedulerFactory.setWaitForJobsToCompleteOnShutdown(true);
	}
	
	protected void tearDown() throws Exception {
		beanFactory.close();
	}

	public void testGetTasks() {
		this.assertEquals(2, tm.getTasks().size());
	}

	public void testGetTask() {
		TaskDefinition task = tm.getTask("test1");
		this.assertNotNull(task);
	}
	
	public void testExecuteImmediately() throws Exception {
		this.assertEquals(0, tm.getExecutingTasks().size());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", 3);
		params.put("count", 95);
		
		runTask("test1", params);
		this.assertEquals(1, tm.getExecutingTasks().size());

		runTask("test1", params);
		this.assertEquals(2, tm.getExecutingTasks().size());
	}
	
	public void testBindParameters() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", "3");
		params.put("count", "95");
		params.put("date", "2007-12-31");
		params.put("string", "howareyou");
		params.put("booleanValue", "true");
		params.put("type", "Who");
		
		runTask("test1", params);
		
		TaskStatus status = tm.getExecutingTasks().get(0);
		this.assertEquals(TaskStatusImpl.STATUS_COMPLETE, status.getStatus());
		TestTask task = (TestTask) status.getTaskInstance();
		this.assertNotNull(task);
		this.assertEquals(95+1, task.getCount());
		this.assertEquals(3, task.getId());
		this.assertNotNull(task.getDate());
		this.assertEquals("howareyou", task.getString());
		this.assertTrue(task.getBooleanValue());
		this.assertEquals(TestEnumType.Who, task.getType());
	}
	
	public void testBindEmptyParams() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", "3");
		params.put("count", "95");
		params.put("date", "");
		params.put("string", "");
		params.put("booleanValue", "");
		
		runTask("test1", params);
		TaskStatus status = tm.getExecutingTasks().get(0);
		this.assertEquals(TaskStatusImpl.STATUS_COMPLETE, status.getStatus());
		TestTask task = (TestTask) status.getTaskInstance();
		this.assertNotNull(task);
		this.assertNull(task.getDate());
		this.assertNull(task.getString());
		this.assertFalse(task.getBooleanValue());
	}
	
	public void testDefaultParameterValue() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		runTask("test2", params);
		TestTask task = (TestTask) tm.getExecutingTasks().get(0).getTaskInstance();
		this.assertNotNull(task);
		this.assertTrue(task.getBooleanValue());
		this.assertEquals(2, task.getId());
	}
	
	private void runTask(String taskid, Map<String, Object> params) throws Exception {
		this.tm.executeImmediately(taskid, params);
		Thread.sleep(500);
	}
}
