/*
 * Copyright (C) 2003 Erik Swenson - eswenson@opensourcesoft.net
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

package net.ninecube.reports.providers.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import net.ninecube.reports.ORStatics;
import net.ninecube.reports.objects.ReportSchedule;
import net.ninecube.reports.objects.ReportUser;
import net.ninecube.reports.providers.ProviderException;
import net.ninecube.reports.providers.QuartzProvider;
import net.ninecube.reports.providers.SchedulerProvider;
import net.ninecube.reports.util.ScheduledReportJob;

import org.apache.log4j.Logger;
import org.quartz.*;

public class SchedulerProviderImpl implements SchedulerProvider
{
	protected static Logger log = Logger.getLogger(SchedulerProviderImpl.class.getName());

	public SchedulerProviderImpl() 
	{		
		log.info("SchedulerProviderImpl created.");		
	}

	public void scheduleReport(ReportSchedule reportSchedule) throws ProviderException
	{
		JobDetail jobDetail = new JobDetail(reportSchedule.getScheduleName(), reportSchedule
				.getScheduleGroup(), ScheduledReportJob.class);

		jobDetail.getJobDataMap().put(ORStatics.REPORT_SCHEDULE, reportSchedule);
		jobDetail.setDescription(reportSchedule.getScheduleDescription());

		if (reportSchedule.getScheduleType() == ReportSchedule.DAILY
				|| reportSchedule.getScheduleType() == ReportSchedule.WEEKLY
				|| reportSchedule.getScheduleType() == ReportSchedule.MONTHLY 
				|| reportSchedule.getScheduleType() == ReportSchedule.WEEKDAYS
				|| reportSchedule.getScheduleType() == ReportSchedule.HOURLY
				|| reportSchedule.getScheduleType() == ReportSchedule.CRON)
		{			
			StringBuffer cronExpression = new StringBuffer();
			
			if (reportSchedule.getScheduleType() == ReportSchedule.CRON)
			{
				cronExpression.append(reportSchedule.getCronExpression());
			}
			else
			{
				cronExpression.append("0 ");
				cronExpression.append(reportSchedule.getStartMinute());
				cronExpression.append(" ");
				cronExpression.append(reportSchedule.getAbsoluteStartHour());

				if (reportSchedule.getScheduleType() == ReportSchedule.HOURLY)
				{
					cronExpression.append("-" + reportSchedule.getAbsoluteEndHour());
				}
				
				if (reportSchedule.getScheduleType() == ReportSchedule.WEEKLY)
				{
					cronExpression.append(" ? * ");
					cronExpression.append(reportSchedule.getDayOfWeek());
				}
				else if (reportSchedule.getScheduleType() == ReportSchedule.MONTHLY)
				{
					cronExpression.append(" " + reportSchedule.getDayOfMonth());
					cronExpression.append(" * ? ");
				}
				else if (reportSchedule.getScheduleType() == ReportSchedule.WEEKDAYS)
				{
					cronExpression.append(" ? * MON-FRI");
				}
				else
				{
					cronExpression.append(" * * ?");
				}
			}			

			CronTrigger cronTrigger = new CronTrigger(reportSchedule.getScheduleName(),
					reportSchedule.getScheduleGroup());

			try
			{
				cronTrigger.setCronExpression(cronExpression.toString());
			}
			catch(ParseException pe)
			{
				throw new ProviderException(pe);
			}
			
			cronTrigger.setStartTime(reportSchedule.getStartDate());

			QuartzProvider.scheduleJob(jobDetail, cronTrigger);
		}		
		else
		{
			// default to run once...
			SimpleTrigger trigger = new SimpleTrigger(reportSchedule.getScheduleName(),
					reportSchedule.getScheduleGroup(), reportSchedule.getStartDateTime(), null,
					0, 0L);

			QuartzProvider.scheduleJob(jobDetail, trigger);
		}		
	}

	public List getScheduledReports(ReportUser reportUser) throws ProviderException
	{
		List scheduledReports = new ArrayList();

		String group = reportUser.getId().toString();

		String[] jobNames = QuartzProvider.getJobNames(group);

		for (int i = 0; i < jobNames.length; i++)
		{
			try
			{
				JobDetail jobDetail = QuartzProvider.getJobDetail(jobNames[i], group);

				ReportSchedule reportSchedule = (ReportSchedule) jobDetail.getJobDataMap().get(
						ORStatics.REPORT_SCHEDULE);
				reportSchedule.setScheduleDescription(jobDetail.getDescription());
				reportSchedule.setScheduleState(QuartzProvider.getTriggerState(jobNames[i], group));
				
				Trigger trigger = QuartzProvider.getTrigger(jobNames[i], group);
				if (trigger != null)
				{
					reportSchedule.setNextFireDate(trigger.getNextFireTime());					
				}

				scheduledReports.add(reportSchedule);
			}
			catch(ProviderException pe)
			{
				log.error(group + " | " + jobNames[i] + " | " + pe.toString());
			}
		}		

		return scheduledReports;
	}

	public void deleteScheduledReport(ReportUser reportUser, String name)
			throws ProviderException
	{
		String group = reportUser.getId().toString();

		QuartzProvider.deleteJob(name, group);		
	}

	public ReportSchedule getScheduledReport(ReportUser reportUser, String name)
			throws ProviderException
	{
		String group = reportUser.getId().toString();

		JobDetail jobDetail = QuartzProvider.getJobDetail(name,	group);

		ReportSchedule reportSchedule = (ReportSchedule) jobDetail.getJobDataMap().get(ORStatics.REPORT_SCHEDULE);
		reportSchedule.setScheduleDescription(jobDetail.getDescription());
		reportSchedule.setScheduleState(QuartzProvider.getTriggerState(name, group));
				
		return reportSchedule;
	}
	
	public void pauseScheduledReport(ReportUser reportUser, String name)
			throws ProviderException
	{
		QuartzProvider.pauseJob(name, reportUser.getId().toString());		
	}
	
	public void resumeScheduledReport(ReportUser reportUser, String name)
			throws ProviderException
	{
		QuartzProvider.resumeJob(name, reportUser.getId().toString());
	}	
}
