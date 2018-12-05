/*
 * Copyright (C) 2006 Erik Swenson - erik@oreports.com
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 *  
 */

package net.ninecube.reports.providers;

import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.DisposableBean;

public class QuartzProvider implements DisposableBean
{
	protected static Logger log = Logger.getLogger(QuartzProvider.class.getName());

	public QuartzProvider() throws ProviderException
	{
//		try
//		{
//			StdSchedulerFactory.getDefaultScheduler().start();
//		}
//		catch(SchedulerException se)
//		{
//			se.printStackTrace();
//			throw new ProviderException(se);
//		}		
		
		log.info("Report Scheduler started.");		
	}

	public static void scheduleJob(JobDetail jobDetail, Trigger trigger) throws ProviderException
	{
//		try
//		{
//			StdSchedulerFactory.getDefaultScheduler().scheduleJob(jobDetail, trigger);
//		}
//		catch(Exception e)
//		{
//			throw new ProviderException(e);
//		}
	}
	
	public static String[] getJobNames(String group) throws ProviderException
	{
		return null;
//		try
//		{
//			return StdSchedulerFactory.getDefaultScheduler().getJobNames(group);
//		}
//		catch(Exception e)
//		{
//			throw new ProviderException(e);
//		}		
	}
	
	public static JobDetail getJobDetail(String jobName, String group) throws ProviderException
	{
		return null;
//		try
//		{
//			return StdSchedulerFactory.getDefaultScheduler().getJobDetail(jobName, group);
//		}
//		catch(Exception e)
//		{
//			throw new ProviderException(e);
//		}		
	}
	
	public static Trigger getTrigger(String jobName, String group) throws ProviderException
	{
		return null;
//		try
//		{
//			return StdSchedulerFactory.getDefaultScheduler().getTrigger(jobName, group);
//		}
//		catch(Exception e)
//		{
//			throw new ProviderException(e);
//		}		
	}
	
	public static String getTriggerState(String jobName, String group) throws ProviderException
	{
		return "";
//		try
//		{
//			int state = StdSchedulerFactory.getDefaultScheduler().getTriggerState(jobName, group);
//			return getTriggerStateName(state);
//		}
//		catch(Exception e)
//		{
//			throw new ProviderException(e);
//		}		
	}
	
	public static void pauseJob(String jobName, String group) throws ProviderException
	{
//		try
//		{
//			StdSchedulerFactory.getDefaultScheduler().pauseJob(jobName, group);
//		}
//		catch(Exception e)
//		{
//			throw new ProviderException(e);
//		}		
	}
	
	public static void resumeJob(String jobName, String group) throws ProviderException
	{
//		try
//		{
//			StdSchedulerFactory.getDefaultScheduler().resumeJob(jobName, group);
//		}
//		catch(Exception e)
//		{
//			throw new ProviderException(e);
//		}		
	}
	
	public static void deleteJob(String jobName, String group) throws ProviderException
	{
//		try
//		{
//			StdSchedulerFactory.getDefaultScheduler().deleteJob(jobName, group);
//		}
//		catch(Exception e)
//		{
//			throw new ProviderException(e);
//		}		
	}		
	
	public void destroy()
	{
//		try
//		{			
//			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
//            if (scheduler != null) scheduler.shutdown();
//
//			log.info("Report Scheduler shutdown.");
//		}
//		catch (SchedulerException se)
//		{
//			log.error(se.toString());
//		}		
	}
	
	private static String getTriggerStateName(int state)
	{
		switch (state)
		{
			case Trigger.STATE_BLOCKED:
				return "Blocked";
			
			case Trigger.STATE_COMPLETE:
				return "Complete";
			
			case Trigger.STATE_ERROR:
				return "ERROR";

			case Trigger.STATE_NORMAL:
				return "Normal";

			case Trigger.STATE_PAUSED:
				return "Paused";

			default:
				return "";
		}
	}
	


}
