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

package net.ninecube.reports.actions;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.ninecube.reports.ORStatics;
import net.ninecube.reports.engine.ChartReportEngine;
import net.ninecube.reports.engine.input.ReportEngineInput;
import net.ninecube.reports.engine.output.ChartEngineOutput;
import net.ninecube.reports.objects.Report;
import net.ninecube.reports.objects.ReportLog;
import net.ninecube.reports.objects.ReportUser;
import net.ninecube.reports.objects.chart.ChartValue;
import net.ninecube.reports.util.LocalStrings;

import org.apache.log4j.Logger;
import org.jfree.chart.imagemap.ImageMapUtilities;

import com.opensymphony.xwork.ActionContext;

public class ChartReportAction extends QueryReportAction 
{
	protected static Logger log = Logger.getLogger(ChartReportAction.class);
	
	private String imageMap;
	private ChartValue[] chartValues;

	public String execute()
	{
		ReportUser user = (ReportUser) ActionContext.getContext().getSession().get(
				ORStatics.REPORT_USER);

		report = (Report) ActionContext.getContext().getSession().get(ORStatics.REPORT);

		ReportLog reportLog = new ReportLog(user, report, new Date());

		try
		{
			log.debug("Starting Chart Report: " + report.getName());			

			reportLogProvider.insertReportLog(reportLog);

			ChartEngineOutput chartOutput = getOutput();	
			
			chartValues = chartOutput.getChartValues();
			if (chartValues.length == 0)
			{
				addActionError(LocalStrings.getString(LocalStrings.ERROR_REPORT_EMPTY));
			}
			
			imageMap = ImageMapUtilities.getImageMap("chart", chartOutput.getChartRenderingInfo());						
			
			HashMap imagesMap = new HashMap();
			imagesMap.put("ChartImage", chartOutput.getContent());			
			
			ActionContext.getContext().getSession().put(ORStatics.IMAGES_MAP, imagesMap);			
						
			reportLog.setEndTime(new Date());
			reportLog.setStatus(ReportLog.STATUS_SUCCESS);
			reportLogProvider.updateReportLog(reportLog);

			log.debug("Finished Chart Report: " + report.getName());
		}
		catch (Exception e)
		{
			addActionError(e.getMessage());

			log.error(e.toString(), e);

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

		return SUCCESS;
	}	
	
	protected ChartEngineOutput getOutput() throws Exception {
		ReportUser user = (ReportUser) ActionContext.getContext().getSession().get(
				ORStatics.REPORT_USER);

		Map reportParameters = getReportParameterMap(user);
		
		ChartReportEngine chartReportEngine = new ChartReportEngine(
				dataSourceProvider, directoryProvider, propertiesProvider);

		ReportEngineInput input = new ReportEngineInput(report, reportParameters);

		ChartEngineOutput chartOutput = (ChartEngineOutput) chartReportEngine
				.generateReport(input);	
		return chartOutput;
	}

	public String getImageMap()
	{
		return imageMap;
	}

	public void setImageMap(String imageMap)
	{
		this.imageMap = imageMap;
	}

	public ChartValue[] getChartValues()
	{
		return chartValues;
	}		
}
