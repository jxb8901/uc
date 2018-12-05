/*
 * Copyright (C) 2003 Erik Swenson - erik@oreports.com
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

package net.ninecube.reports.util;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRVirtualizer;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import net.ninecube.reports.ORStatics;
import net.ninecube.reports.engine.ChartReportEngine;
import net.ninecube.reports.engine.JasperReportEngine;
import net.ninecube.reports.engine.ReportEngine;
import net.ninecube.reports.engine.ReportEngineHelper;
import net.ninecube.reports.engine.input.ReportEngineInput;
import net.ninecube.reports.engine.output.ChartEngineOutput;
import net.ninecube.reports.engine.output.JasperReportEngineOutput;
import net.ninecube.reports.engine.output.ReportEngineOutput;
import net.ninecube.reports.objects.MailMessage;
import net.ninecube.reports.objects.Report;
import net.ninecube.reports.objects.ReportLog;
import net.ninecube.reports.objects.ReportSchedule;
import net.ninecube.reports.objects.ReportUser;
import net.ninecube.reports.objects.ReportUserAlert;
import net.ninecube.reports.providers.AlertProvider;
import net.ninecube.reports.providers.AlertProviderAware;
import net.ninecube.reports.providers.DataSourceProvider;
import net.ninecube.reports.providers.DataSourceProviderAware;
import net.ninecube.reports.providers.DirectoryProvider;
import net.ninecube.reports.providers.DirectoryProviderAware;
import net.ninecube.reports.providers.MailProvider;
import net.ninecube.reports.providers.MailProviderAware;
import net.ninecube.reports.providers.PropertiesProvider;
import net.ninecube.reports.providers.PropertiesProviderAware;
import net.ninecube.reports.providers.ReportLogProvider;
import net.ninecube.reports.providers.ReportLogProviderAware;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ScheduledReportJob
	implements
		Job,
		ReportLogProviderAware,		
		DirectoryProviderAware,
		MailProviderAware,
		AlertProviderAware,
		DataSourceProviderAware,
		PropertiesProviderAware
{
	protected static Logger log =
		Logger.getLogger(ScheduledReportJob.class.getName());
	
	private ReportLogProvider reportLogProvider;	
	private DirectoryProvider directoryProvider;
	private MailProvider mailProvider;
	private AlertProvider alertProvider;
	private DataSourceProvider dataSourceProvider;
	private PropertiesProvider propertiesProvider;

	public ScheduledReportJob()
	{
		ORComponentManager.initializeObject(this);			
	}	

	public void execute(JobExecutionContext context)
		throws JobExecutionException
	{
		log.debug("Scheduled Report Executing....");

		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

		ReportSchedule reportSchedule =
			(ReportSchedule) jobDataMap.get(ORStatics.REPORT_SCHEDULE);
		reportSchedule.setScheduleDescription(context.getJobDetail().getDescription());

		Report report = reportSchedule.getReport();
		ReportUser user = reportSchedule.getUser();
		Map reportParameters = reportSchedule.getReportParameters();

		log.debug("Report: " + report.getName());
		log.debug("User: " + user.getName());		

		JRVirtualizer virtualizer = null;
		
		ReportLog reportLog = new ReportLog(user, report, new Date());

		try
		{
			//
			ReportUserAlert alert = reportSchedule.getAlert();			
			
			if (alert != null)
			{
				log.debug("Executing Alert Condition");
				
				alert.setReport(report);
				alert = alertProvider.executeAlert(alert, true);
				
				if (!alert.isTriggered())
				{
					log.debug("Alert Not Triggered. Report not run.");
					return;
				}
				
				log.debug("Alert Triggered. Running report.");
			}
			//			

			// add standard report parameters
			reportParameters.put(ORStatics.USER_ID, user.getId());
			reportParameters.put(ORStatics.EXTERNAL_ID, user.getExternalId());
			reportParameters.put(ORStatics.USER_NAME, user.getName());
			reportParameters.put(ORStatics.IMAGE_DIR, new File(directoryProvider.getReportImageDirectory()));
			reportParameters.put(ORStatics.REPORT_DIR, new File(directoryProvider.getReportDirectory()));
			//
			
			reportLogProvider.insertReportLog(reportLog);		
			
			ReportEngineInput reportInput = new ReportEngineInput(report, reportParameters);
			reportInput.setExportType(reportSchedule.getExportType());
			
			if (report.isJasperReport())
			{
				// add any charts
				if (report.getReportChart() != null)
				{
					log.debug("Adding chart: " + report.getReportChart().getName());
				
					ChartReportEngine chartEngine = new ChartReportEngine(
							dataSourceProvider, directoryProvider, propertiesProvider);
					
					ChartEngineOutput chartOutput = (ChartEngineOutput) chartEngine.generateReport(reportInput);
				
					reportParameters.put("ChartImage", chartOutput.getContent());				
				}

				if (report.isVirtualizationEnabled())
				{
					log.debug("Virtualization Enabled");
					virtualizer = new JRFileVirtualizer(2, directoryProvider.getTempDirectory());
					reportParameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
				}
				
				reportInput.setParameters(reportParameters);
				reportInput.setInlineImages(true);				
			}
			
			ReportEngine reportEngine = ReportEngineHelper.getReportEngine(report,
					dataSourceProvider, directoryProvider, propertiesProvider);
			
			//Use the JasperReportEngine to generate scheduled QueryReports
			if (report.isQueryReport())
			{
				reportEngine = new JasperReportEngine(dataSourceProvider,
						directoryProvider, propertiesProvider);
			}
			
			ReportEngineOutput reportOutput = reportEngine.generateReport(reportInput);
			
			Vector htmlImageDataSources = new Vector();
 			ByteArrayDataSource byteArrayDataSource = exportReport(reportOutput, reportSchedule, htmlImageDataSources);

			MailMessage mail = new MailMessage();				
			mail.setByteArrayDataSource(byteArrayDataSource);
			mail.addHtmlImageDataSources(htmlImageDataSources);			 
			mail.setSender(user.getEmail());
			mail.parseRecipients(reportSchedule.getRecipients());
			
			if (reportSchedule.getScheduleDescription() != null && reportSchedule.getScheduleDescription().trim().length() > 0)
			{
				mail.setSubject(reportSchedule.getScheduleDescription());
			}
			else
			{
				mail.setSubject(report.getName());
			}
			
			if (reportSchedule.getExportType() != ReportEngine.EXPORT_HTML)
			{
				mail.setText(report.getName() + ": Generated on " + new Date());
			}

			mailProvider.sendMail(mail);

			log.debug(
				byteArrayDataSource.getName()
					+ " sent to: "
					+ mail.formatRecipients(";"));

			reportLog.setEndTime(new Date());
			reportLog.setStatus(ReportLog.STATUS_SUCCESS);
			reportLogProvider.updateReportLog(reportLog);

			log.debug("Scheduled Report Finished...");
		}
		catch (Exception e)
		{
			if (e.getMessage() != null && e.getMessage().indexOf("Empty") > 0)
			{
				reportLog.setStatus(ReportLog.STATUS_EMPTY);
			}
			else
			{
				e.printStackTrace();
				log.error(e.toString());

				reportLog.setMessage(e.getMessage());
				reportLog.setStatus(ReportLog.STATUS_FAILURE);
			}

			reportLog.setEndTime(new Date());

			try
			{
				reportLogProvider.updateReportLog(reportLog);
			}
			catch (Exception ex)
			{
				log.error("Unable to create ReportLog: " + ex.getMessage());
			}
		}
		finally
		{
			if (virtualizer != null)
			{
				reportParameters.remove(JRParameter.REPORT_VIRTUALIZER);			
				virtualizer.cleanup();
			}
		}		
	}

	protected ByteArrayDataSource exportReport(ReportEngineOutput reportOutput, ReportSchedule reportSchedule,
			Vector htmlImageDataSources) throws JRException
	{		
		String reportName = StringUtils.deleteWhitespace(reportSchedule.getReport().getName());

		ByteArrayDataSource byteArrayDataSource = new ByteArrayDataSource(reportOutput.getContent(), reportOutput.getContentType());
		byteArrayDataSource.setName(reportName + reportOutput.getContentExtension());
		
		if (reportSchedule.getExportType() == ReportEngine.EXPORT_HTML
				&& reportSchedule.getReport().isJasperReport())
		{
			Map imagesMap = ((JasperReportEngineOutput) reportOutput).getImagesMap();

			for (Iterator entryIter = imagesMap.entrySet().iterator(); entryIter
					.hasNext();)
			{
				Map.Entry entry = (Map.Entry) entryIter.next();

				ByteArrayDataSource imageDataSource = new ByteArrayDataSource(
						(byte[]) entry.getValue(), getImageContentType((byte[]) entry
								.getValue()));

				imageDataSource.setName((String) entry.getKey());

				htmlImageDataSources.add(imageDataSource);
			}
		}

		return byteArrayDataSource;
	}

	/**
	 * Try to figure out the image type from its bytes.
	 */
	private String getImageContentType(byte[] bytes)
	{
		String header = new String(bytes, 0, (bytes.length > 100) ? 100 : bytes.length);
		if (header.startsWith("GIF"))
		{
			return "image/gif";
		}

		if (header.startsWith("BM"))
		{
			return "image/bmp";
		}

		if (header.indexOf("JFIF") >= 0)
		{
			return "image/jpeg";
		}

		if (header.indexOf("PNG") >= 0)
		{
			return "image/png";
		}

		// We are out of guesses, so just guess tiff
		return "image/tiff";
	}
	
	public void setReportLogProvider(ReportLogProvider reportLogProvider)
	{
		this.reportLogProvider = reportLogProvider;
	}

	public void setDirectoryProvider(DirectoryProvider directoryProvider)
	{
		this.directoryProvider = directoryProvider;
	}

	public void setMailProvider(MailProvider mailProvider)
	{
		this.mailProvider = mailProvider;
	}
	
	public void setAlertProvider(AlertProvider alertProvider)
	{
		this.alertProvider = alertProvider;
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
