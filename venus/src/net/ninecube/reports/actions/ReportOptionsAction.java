/*
 * Copyright (C) 2002 Erik Swenson - erik@oreports.com
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

import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionSupport;

import net.ninecube.reports.ORStatics;
import net.ninecube.reports.objects.Report;
import net.ninecube.reports.objects.ReportUser;
import net.ninecube.reports.providers.*;
import net.ninecube.reports.util.LocalStrings;

import org.apache.log4j.Logger;

public class ReportOptionsAction extends ActionSupport
		implements
			SchedulerProviderAware			
{
	protected static Logger log = Logger.getLogger(ReportOptionsAction.class);

	private String exportType;
	private boolean submitRun;
	private boolean submitSchedule;
	private Report report;
	
	private SchedulerProvider schedulerProvider;

	public String execute()
	{
		ReportUser user = (ReportUser) ActionContext.getContext().getSession().get(
				ORStatics.REPORT_USER);
		
		report = (Report) ActionContext.getContext().getSession().get(ORStatics.REPORT);

		if (report == null)
		{
			addActionError(LocalStrings.getString(LocalStrings.ERROR_REPORT_INVALID));
			return ERROR;
		}

		if (!user.isValidReport(report))
		{
			addActionError(LocalStrings.getString(LocalStrings.ERROR_REPORT_NOTAUTHORIZED));
			return ERROR;
		}

		ActionContext.getContext().getSession().put(ORStatics.REPORT, report);		

		if (report.isQueryReport() && !submitSchedule)
		{
			return ORStatics.QUERY_REPORT_ACTION;
		}
		
		if (report.isChartReport() && !submitSchedule)
		{
			return ORStatics.CHART_REPORT_ACTION;
		}				
		
		/*
		 * if report is displayed inline, export type already selected and
		 * scheduling not currently supported, so just run report...
		 */
		if (report.isDisplayInline()) return SUCCESS;		
		
		if (submitRun || submitSchedule)
		{
			if (exportType == null || exportType.length() < 1) return INPUT;
			
			ActionContext.getContext().getSession().put(ORStatics.EXPORT_TYPE, exportType);			
			
			if (submitRun)return SUCCESS;	
			if (submitSchedule)	return ORStatics.SCHEDULE_REPORT_ACTION;			
		}

		return INPUT;
	}

	public String getExportType()
	{
		return exportType;
	}

	public void setExportType(String exportType)
	{
		this.exportType = exportType;
	}

	public Report getReport()
	{
		return report;
	}

	public void setReport(Report report)
	{
		this.report = report;
	}

	public boolean isSchedulerAvailable()
	{
		if (schedulerProvider != null) return true;
		return false;
	}

	public void setSchedulerProvider(SchedulerProvider schedulerProvider)
	{
		this.schedulerProvider = schedulerProvider;
	}

	public void setSubmitRun(String submitRun)
	{
		if (submitRun != null) this.submitRun = true;
	}

	public void setSubmitSchedule(String submitSchedule)
	{
		if (submitSchedule != null) this.submitSchedule = true;
	}	
}
