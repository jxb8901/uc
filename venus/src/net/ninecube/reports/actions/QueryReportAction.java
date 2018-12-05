/*
 * Copyright (C) 2004 Erik Swenson - erik@oreports.com
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

package net.ninecube.reports.actions;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.ninecube.reports.ORStatics;
import net.ninecube.reports.engine.JFreeReportEngine;
import net.ninecube.reports.engine.QueryReportEngine;
import net.ninecube.reports.engine.input.ReportEngineInput;
import net.ninecube.reports.engine.output.QueryEngineOutput;
import net.ninecube.reports.engine.output.ReportEngineOutput;
import net.ninecube.reports.objects.Report;
import net.ninecube.reports.objects.ReportLog;
import net.ninecube.reports.objects.ReportUser;
import net.ninecube.reports.providers.DataSourceProvider;
import net.ninecube.reports.providers.DataSourceProviderAware;
import net.ninecube.reports.providers.DirectoryProvider;
import net.ninecube.reports.providers.DirectoryProviderAware;
import net.ninecube.reports.providers.PropertiesProvider;
import net.ninecube.reports.providers.PropertiesProviderAware;
import net.ninecube.reports.providers.ReportLogProvider;
import net.ninecube.reports.providers.ReportLogProviderAware;

import org.apache.log4j.Logger;

import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionSupport;

public class QueryReportAction extends ActionSupport
		implements
			ReportLogProviderAware,
			DataSourceProviderAware,
			PropertiesProviderAware, DirectoryProviderAware
{
	protected static Logger log = Logger.getLogger(QueryReportAction.class);

	protected DataSourceProvider dataSourceProvider;
	protected ReportLogProvider reportLogProvider;
	protected PropertiesProvider propertiesProvider;
	protected DirectoryProvider directoryProvider;	
	
	protected Report report;
	
	private String html;	

	public String execute()
	{
		//remove results of any previous query report from session
		ActionContext.getContext().getSession().remove(ORStatics.QUERY_REPORT_RESULTS);
		ActionContext.getContext().getSession().remove(ORStatics.QUERY_REPORT_PROPERTIES);	
		
		ReportUser user = (ReportUser) ActionContext.getContext().getSession().get(
				ORStatics.REPORT_USER);

		report = (Report) ActionContext.getContext().getSession().get(ORStatics.REPORT);

		Map reportParameters = getReportParameterMap(user);

		ReportLog reportLog = new ReportLog(user, report, new Date());

		try
		{
			log.debug("Starting Query Report: " + report.getName());
			log.debug("Query: " + report.getQuery());

			reportLogProvider.insertReportLog(reportLog);		
			
			ReportEngineInput input = new ReportEngineInput(report, reportParameters);
			
			if (report.isJFreeReport())
			{
				JFreeReportEngine jfreeReportEngine = new JFreeReportEngine(
						dataSourceProvider, directoryProvider, propertiesProvider);
				
				ReportEngineOutput output = jfreeReportEngine.generateReport(input);				
				
				html = new String(output.getContent());
			}
			else
			{
				QueryReportEngine queryReportEngine = new QueryReportEngine(
						dataSourceProvider, directoryProvider, propertiesProvider);
				
				QueryEngineOutput output = (QueryEngineOutput) queryReportEngine.generateReport(input);				
				
				ActionContext.getContext().getSession().put(
						ORStatics.QUERY_REPORT_RESULTS, output.getResults());
				
				ActionContext.getContext().getSession().put(
						ORStatics.QUERY_REPORT_PROPERTIES,
						output.getProperties());
			}			

			reportLog.setEndTime(new Date());
			reportLog.setStatus(ReportLog.STATUS_SUCCESS);
			reportLogProvider.updateReportLog(reportLog);

			log.debug("Finished Query Report: " + report.getName());
		}
		catch (Exception e)
		{

			addActionError(e.getMessage());

			log.error(e.getMessage(), e);

			reportLog.setMessage(e.getMessage());
			reportLog.setStatus(ReportLog.STATUS_FAILURE);

			reportLog.setEndTime(new Date());

			try
			{
				reportLogProvider.updateReportLog(reportLog);
			}
			catch (Exception ex)
			{
				log.error("Unable to create ReportLog: " + ex.getMessage());
			}

			return ERROR;
		}		
		
		if (report.isJFreeReport()) return ORStatics.JFREEREPORT_RESULT;		
		
		return SUCCESS;		
	}	

	protected Map getReportParameterMap(ReportUser user)
	{
		Map reportParameters = new HashMap();

		if (ActionContext.getContext().getSession().get(ORStatics.REPORT_PARAMETERS) != null)
		{
			reportParameters = (Map) ActionContext.getContext().getSession().get(
					ORStatics.REPORT_PARAMETERS);
		}

		// add standard report parameters
		reportParameters.put(ORStatics.USER_ID, user.getId());
		reportParameters.put(ORStatics.EXTERNAL_ID, user.getExternalId());
		reportParameters.put(ORStatics.USER_NAME, user.getName());

		return reportParameters;
	}

	public void setReportLogProvider(ReportLogProvider reportLogProvider)
	{
		this.reportLogProvider = reportLogProvider;
	}

	public void setDataSourceProvider(DataSourceProvider dataSourceProvider)
	{
		this.dataSourceProvider = dataSourceProvider;
	}

	public void setPropertiesProvider(PropertiesProvider propertiesProvider)
	{
		this.propertiesProvider = propertiesProvider;		
	}	
	
	public void setDirectoryProvider(DirectoryProvider directoryProvider)
	{
		this.directoryProvider = directoryProvider;
	}

	public String getHtml()
	{
		return html;
	}
	
	public Report getReport()
	{
		return report;
	}
}
