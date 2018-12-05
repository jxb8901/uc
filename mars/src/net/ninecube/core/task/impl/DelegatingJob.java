/**
 * 
 * created on 2007-6-15
 */
package net.ninecube.core.task.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.ninecube.core.exception.TaskException;
import net.ninecube.core.task.TaskDefinition;
import net.ninecube.util.StringUtil;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.StatefulJob;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.propertyeditors.CharacterEditor;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;

/**
 * 
 * @author jxb
 */
public class DelegatingJob implements Job {
	private Runnable task;
	
	public Runnable getTask() {
		return task;
	}

	public void execute(JobExecutionContext context) throws TaskException {
		try {
			this.getTaskInstance(context).run();
		}
		catch (Exception ex) {
			throw new TaskException(ex);
		}
	}
	
	/**
	 * 实例化新的任务对象，并自动绑定属性
	 */
	protected Runnable getTaskInstance(JobExecutionContext context) {
		this.task = bindProperties(
				((TaskDefinition) context.getJobDetail()).getTaskClass(), 
				context.getMergedJobDataMap());
		return this.task;
	}
	
	@SuppressWarnings("unchecked")
	protected static <T> T bindProperties(Class<T> type, Map properties) {
		BeanWrapper bw = new BeanWrapperImpl(type);
		
		bw.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyyMMdd"), true));
		bw.registerCustomEditor(char.class, new CharacterEditor(true));
		bw.registerCustomEditor(char.class, new CharacterEditor(true));
		bw.registerCustomEditor(boolean.class, new CustomBooleanEditor(true));
		bw.registerCustomEditor(byte.class, new CustomNumberEditor(Byte.class, true));
		bw.registerCustomEditor(short.class, new CustomNumberEditor(Short.class, true));
		bw.registerCustomEditor(int.class, new CustomNumberEditor(Integer.class, true));
		bw.registerCustomEditor(long.class, new CustomNumberEditor(Long.class, true));
		bw.registerCustomEditor(float.class, new CustomNumberEditor(Float.class, true));
		bw.registerCustomEditor(double.class, new CustomNumberEditor(Double.class, true));
		
		MutablePropertyValues pvs = new MutablePropertyValues();
		pvs.addPropertyValues(properties);
		bw.setPropertyValues(pvs, true);
		
		return (T) bw.getWrappedInstance();
	}
}

class StatefulDelegatingJob extends DelegatingJob implements StatefulJob {
	
}
