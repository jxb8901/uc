/*
 * Copyright (C) 2006 Erik Swenson - erik@oreports.com
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 *  
 */

package net.ninecube.reports.services;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import net.ninecube.reports.ORStatics;
import net.ninecube.reports.engine.ReportEngine;
import net.ninecube.reports.engine.ReportEngineHelper;
import net.ninecube.reports.engine.input.ReportEngineInput;
import net.ninecube.reports.engine.output.ReportEngineOutput;
import net.ninecube.reports.objects.Report;
import net.ninecube.reports.objects.ReportLog;
import net.ninecube.reports.objects.ReportSchedule;
import net.ninecube.reports.objects.ReportUser;
import net.ninecube.reports.providers.DataSourceProvider;
import net.ninecube.reports.providers.DirectoryProvider;
import net.ninecube.reports.providers.ParameterProvider;
import net.ninecube.reports.providers.PropertiesProvider;
import net.ninecube.reports.providers.ProviderException;
import net.ninecube.reports.providers.ReportLogProvider;
import net.ninecube.reports.providers.ReportProvider;
import net.ninecube.reports.providers.SchedulerProvider;
import net.ninecube.reports.providers.UserProvider;
import net.ninecube.reports.services.info.ReportInfo;
import net.ninecube.reports.services.input.ReportServiceInput;
import net.ninecube.reports.services.input.ServletReportServiceInput;
import net.ninecube.reports.services.util.Converter;
import net.ninecube.reports.util.LocalStrings;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

/**
 * ReportService implementation using standard OpenReports providers. 
 * 
 * @author Erik Swenson
 */

public class ReportServiceImpl implements ReportService
{
	private static Logger log = Logger.getLogger(ReportServiceImpl.class.getName());

	private ReportProvider reportProvider;
	private ReportLogProvider reportLogProvider;	
	private SchedulerProvider schedulerProvider;
	private UserProvider userProvider;	
	private DirectoryProvider directoryProvider;
	private ParameterProvider parameterProvider;	
	private DataSourceProvider dataSourceProvider;
	private PropertiesProvider propertiesProvider;	

	public ReportServiceImpl()
	{
		log.info("ReportService: Started");
	}		
	
	/**
	 * Generate a Report and return a ReportEngineOutput. Contents of the
	 * ReportEngineOutput vary with the deliveryMethod and exportType of the
	 * ReportServiceInput.
	 * 
	 * Returns errors messages in the message field of the ReportServiceOutput.
	 * 
	 * Includes Report Logging functionality.
	 */
	public ReportEngineOutput generateReport(ReportServiceInput reportInput)
	{		
		ReportEngineOutput reportOutput = new ReportEngineOutput();			
		
		if (reportInput.getUserName() == null || reportInput.getReportName() == null)
		{
			reportOutput.setContentMessage("Invalid ReportInput - UserName and ReportName required.");
			log.warn("generateReport: " + reportOutput.getContentMessage());
			
			return reportOutput;
		}
		
		ReportLog reportLog = null;
		
		try
		{
			Report report = reportProvider.getReport(reportInput.getReportName());
			if (report == null)
			{
				reportOutput.setContentMessage("Invalid ReportInput - Report not found: " + reportInput.getReportName());
				log.warn("generateReport: " + reportOutput.getContentMessage());
				
				return reportOutput;
			}
			
			ReportUser user = userProvider.getUser(reportInput.getUserName());
			if (user == null)
			{
				reportOutput.setContentMessage("Invalid ReportInput - User not found: " + reportInput.getUserName());
				log.warn("generateReport: " + reportOutput.getContentMessage());
				
				return reportOutput;
			}
			
			if (!user.isValidReport(report))
			{
				reportOutput.setContentMessage("Invalid ReportInput - "
						+ reportInput.getUserName() + " not authorized to run: "
						+ reportInput.getReportName());
				
				log.warn("generateReport: " + reportOutput.getContentMessage());
				
				return reportOutput;	
			}
			
			reportLog = new ReportLog(user, report, new Date());
			reportLog = reportLogProvider.insertReportLog(reportLog);
			
			log.info("generateReport: " + user.getName() + " : " + report.getName() + " : " + reportInput.getDeliveryMethod());
			
			if (reportInput.getDeliveryMethod().equals(ReportService.DELIVERY_EMAIL))
			{
				ReportSchedule schedule = new ReportSchedule();
				schedule.setReport(report);
				schedule.setUser(user);
				schedule.setReportParameters(buildParameterMap(reportInput, report));
				schedule.setExportType(reportInput.getExportType());
				schedule.setRecipients(user.getEmail());
				schedule.setScheduleName(report.getId() + "|" + new Date().getTime());
				schedule.setScheduleDescription(reportInput.getScheduleDescription());				
				schedule.setScheduleType(ReportSchedule.ONCE);
				
				// advanced scheduling
				if (reportInput.getStartDate() != null)
				{
					if (!user.isAdvancedScheduler())
					{
						throw new ProviderException("Not Authorized: Advanced Scheduling permission required");					
					}
					
					schedule.setScheduleType(reportInput.getScheduleType());
					schedule.setStartDate(reportInput.getStartDate());
					schedule.setStartHour(reportInput.getStartHour());
					schedule.setStartMinute(reportInput.getStartMinute());
					schedule.setStartAmPm(reportInput.getStartAmPm());
					schedule.setHours(reportInput.getHours());
					schedule.setCronExpression(reportInput.getCronExpression());					
				}
								
				schedulerProvider.scheduleReport(schedule);					
			}
			else
			{
				ReportEngine reportEngine = ReportEngineHelper.getReportEngine(report,
						dataSourceProvider, directoryProvider, propertiesProvider);
				
				ReportEngineInput engineInput = new ReportEngineInput(report, buildParameterMap(reportInput, report));
				engineInput.setExportType(reportInput.getExportType());								 
				
				reportOutput = reportEngine.generateReport(engineInput);		
			}
			
			reportOutput.setContentMessage(LocalStrings.getString(LocalStrings.SERVICE_REQUEST_COMPLETE));
			
			reportLog.setStatus(ReportLog.STATUS_SUCCESS);
			reportLog.setEndTime(new Date());
			reportLogProvider.updateReportLog(reportLog);			
		}
		catch (Exception e)
		{			
			log.error(e);					
			reportOutput.setContentMessage(e.getMessage());		
			
			if (reportLog != null && reportLog.getId() != null)
			{
				reportLog.setStatus(ReportLog.STATUS_FAILURE);
				reportLog.setMessage(e.getMessage());
				reportLog.setEndTime(new Date());
				
				try
				{
					reportLogProvider.updateReportLog(reportLog);
				}
				catch (Exception ex)
				{
					log.error("Unable to update ReportLog: " + ex.getMessage());
				}						
			}
		}
		
		log.info("generateReport: " + reportOutput.getContentMessage());				
		
		return reportOutput;
	}		
	
	public ReportInfo getReportInfo(String reportName)
	{
		ReportInfo reportInfo = null;
		
		try
		{
			Report report = reportProvider.getReport(reportName);
			if (report != null)
			{
				reportInfo = Converter.convertToReportInfo(report);
			}
		}
		catch(ProviderException pe)
		{
			log.warn(pe);
		}
		
		return reportInfo;		
	}

	/**
	 * Builds report parameter map from incoming ReportServiceInput and adds
	 * standard report parameters.
	 */	
	private Map buildParameterMap(ReportServiceInput reportServiceInput, Report report) throws ProviderException
	{
		Map inputParameters = new HashMap();
		
		if (reportServiceInput instanceof ServletReportServiceInput)
		{
			inputParameters = ((ServletReportServiceInput) reportServiceInput).getParameterMap();
		}
		else
		{
			String[] parameters = reportServiceInput.getParameters();

			if (parameters != null)
			{
				/*
				 * Incoming String[] containing Strings in the format 'city=Boston'
				 * is converted into a Map with a String key and a String[] value
				 * in order to mimic the format of the map returned by 
				 * HttpServletRequest.getParameterMap() and required by 
				 * parameterProvider.getReportParametersMap(...)
				 */
				for (int i = 0; i < parameters.length; i++)
				{
					StringTokenizer tokenizer = new StringTokenizer(parameters[i], "=");
					if (tokenizer.countTokens() == 2)
					{
						String key = tokenizer.nextToken();
						String[] value = new String[] { tokenizer.nextToken() };

						if (inputParameters.containsKey(key))
						{
							String[] currentValue = (String[]) inputParameters.get(key);
							value = (String[]) ArrayUtils.addAll(currentValue, value);
						}

						inputParameters.put(key, value);
					}
					else
					{
						log.warn("Invalid Parameter Format: " + parameters[i]);
					}
				}
			}
		}		
		
		Map parsedParameters = parameterProvider.getReportParametersMap(report.getParameters(), inputParameters);		
		parsedParameters.put(ORStatics.IMAGE_DIR, new File(directoryProvider.getReportImageDirectory()));		
		parsedParameters.put(ORStatics.REPORT_DIR, new File(directoryProvider.getReportDirectory()));		
		
		return parsedParameters;
	}

	public void setDirectoryProvider(DirectoryProvider directoryProvider)
	{
		this.directoryProvider = directoryProvider;
	}

	public void setParameterProvider(ParameterProvider parameterProvider)
	{
		this.parameterProvider = parameterProvider;
	}

	public void setReportLogProvider(ReportLogProvider reportLogProvider)
	{
		this.reportLogProvider = reportLogProvider;
	}

	public void setReportProvider(ReportProvider reportProvider)
	{
		this.reportProvider = reportProvider;
	}	

	public void setSchedulerProvider(SchedulerProvider schedulerProvider)
	{
		this.schedulerProvider = schedulerProvider;
	}

	public void setUserProvider(UserProvider userProvider)
	{
		this.userProvider = userProvider;
	}	

	public void setDataSourceProvider(DataSourceProvider dataSourceProvider)
	{
		this.dataSourceProvider = dataSourceProvider;
	}

	public void setPropertiesProvider(PropertiesProvider propertiesProvider)
	{
		this.propertiesProvider = propertiesProvider;
	}	
}
