/*
 * Copyright (C) 2002 Erik Swenson - erik@oreports.com
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *  
 */

package net.ninecube.reports.actions;

import java.io.File;
import java.util.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRVirtualizer;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import net.ninecube.reports.ORStatics;
import net.ninecube.reports.engine.ChartReportEngine;
import net.ninecube.reports.engine.JasperReportEngine;
import net.ninecube.reports.engine.ReportEngine;
import net.ninecube.reports.engine.ReportEngineHelper;
import net.ninecube.reports.engine.input.ReportEngineInput;
import net.ninecube.reports.engine.output.ChartEngineOutput;
import net.ninecube.reports.engine.output.ReportEngineOutput;
import net.ninecube.reports.objects.*;
import net.ninecube.reports.providers.*;
import net.ninecube.reports.util.LocalStrings;

public class ReportRunAction
	extends ActionSupport implements ReportLogProviderAware,
		DirectoryProviderAware, DataSourceProviderAware,
		PropertiesProviderAware
{
	protected static Logger log = Logger.getLogger(ReportRunAction.class);
	
	private ReportLogProvider reportLogProvider;
	private DirectoryProvider directoryProvider;
	private DataSourceProvider dataSourceProvider;
	private PropertiesProvider propertiesProvider;		

	public String execute()
	{		
		ReportUser user =
			(ReportUser) ActionContext.getContext().getSession().get(ORStatics.REPORT_USER);

		Report report = (Report) ActionContext.getContext().getSession().get(ORStatics.REPORT);

		int exportType =
			Integer.parseInt(
				(String) ActionContext.getContext().getSession().get(ORStatics.EXPORT_TYPE));

		Map reportParameters = getReportParameterMap(user, report, exportType);
		Map imagesMap = getImagesMap();

		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		// set headers to disable caching		
		response.setHeader("Pragma", "public");
		response.setHeader("Cache-Control", "max-age=0");

		ReportLog reportLog = new ReportLog(user, report, new Date());
		
		JRVirtualizer virtualizer = null;

		try
		{
			if (exportType == ReportEngine.EXPORT_PDF)				
			{
				// Handle "contype" request from Internet Explorer
				if ("contype".equals(request.getHeader("User-Agent"))) 
				{					
	                response.setContentType("application/pdf");	            
	                response.setContentLength(0);               
	                
	                ServletOutputStream outputStream = response.getOutputStream();
	                outputStream.close();	    
	                
	                return NONE;
				}
			}			
							
			log.debug("Filling report: " + report.getName());
			
			reportLogProvider.insertReportLog(reportLog);						

			if (report.isVirtualizationEnabled() && exportType != ReportEngine.EXPORT_IMAGE)
			{
				log.debug("Virtualization Enabled");
				virtualizer = new JRFileVirtualizer(2, directoryProvider.getTempDirectory());
				reportParameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
			}

			ReportEngineInput reportInput = new ReportEngineInput(report, reportParameters);
			reportInput.setExportType(exportType);
			reportInput.setImagesMap(imagesMap);

			// add any charts
			if (report.getReportChart() != null)
			{
				log.debug("Adding chart: " + report.getReportChart().getName());

				ChartReportEngine chartEngine = new ChartReportEngine(dataSourceProvider,
						directoryProvider, propertiesProvider);
				
				ChartEngineOutput chartOutput = (ChartEngineOutput) chartEngine
						.generateReport(reportInput);

				reportParameters.put("ChartImage", chartOutput.getContent());
			}
			
			ReportEngineOutput reportOutput = null;
			JasperPrint jasperPrint = null;
			
			if (report.isJasperReport())
			{
				JasperReportEngine jasperEngine = new JasperReportEngine(
						dataSourceProvider, directoryProvider, propertiesProvider);
				
				jasperPrint = jasperEngine.fillReport(reportInput);

				log.debug("Report filled - " + report.getName() + " : size = "
						+ jasperPrint.getPages().size());

				log.debug("Exporting report: " + report.getName());

				reportOutput = jasperEngine.exportReport(jasperPrint, exportType, report
						.getReportExportOption(), imagesMap, false);
			}
			else
			{
				ReportEngine reportEngine = ReportEngineHelper.getReportEngine(report, dataSourceProvider, directoryProvider, propertiesProvider);
				reportOutput = reportEngine.generateReport(reportInput);
			}
			
			response.setContentType(reportOutput.getContentType());
			
			if (exportType != ReportEngine.EXPORT_HTML && exportType != ReportEngine.EXPORT_IMAGE)
			{
				response.setHeader("Content-disposition", "inline; filename="
						+ StringUtils.deleteWhitespace(report.getName()) + reportOutput.getContentExtension());
			}			
			
			if (exportType == ReportEngine.EXPORT_IMAGE)
			{
				if (jasperPrint != null)
				{					
					ActionContext.getContext().getSession().put(ORStatics.JASPERPRINT, jasperPrint);
				}
			}
			else
			{				
				byte[] content = reportOutput.getContent();
				
				response.setContentLength(content.length);
				
				ServletOutputStream out = response.getOutputStream();
				out.write(content, 0, content.length);
				out.flush();
				out.close();				
			}

			reportLog.setEndTime(new Date());
			reportLog.setStatus(ReportLog.STATUS_SUCCESS);
			reportLogProvider.updateReportLog(reportLog);			

			log.debug("Finished report: " + report.getName());
		}
		catch (Exception e)
		{
			if (e.getMessage() != null && e.getMessage().indexOf("Empty") > 0)
			{
				addActionError(LocalStrings.getString(LocalStrings.ERROR_REPORT_EMPTY));
				reportLog.setStatus(ReportLog.STATUS_EMPTY);
			}
			else
			{
				addActionError(e.getMessage());

				log.error(e.getMessage(), e);

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
			
			return ERROR;
		}
		finally
		{
			if (virtualizer != null)
			{
				reportParameters.remove(JRParameter.REPORT_VIRTUALIZER);			
				virtualizer.cleanup();
			}
		}		
		
		if (exportType == ReportEngine.EXPORT_IMAGE) return SUCCESS;
		
		return NONE;
	}

	protected Map getReportParameterMap(ReportUser user, Report report, int exportType)
	{
		Map reportParameters = new HashMap();
		if (ActionContext.getContext().getSession().get(ORStatics.REPORT_PARAMETERS) != null)
		{
			reportParameters =
				(Map) ActionContext.getContext().getSession().get(ORStatics.REPORT_PARAMETERS);
		}
		
		reportParameters.put(ORStatics.IMAGE_DIR, new File(directoryProvider.getReportImageDirectory()));		
		reportParameters.put(ORStatics.REPORT_DIR, new File(directoryProvider.getReportDirectory()));		
		reportParameters.put(ORStatics.EXPORT_TYPE_PARAM, new Integer(exportType));

		return reportParameters;
	}

	protected Map getImagesMap()
	{
		// used by JasperReports HTML export
		// see ImageLoaderAction for more information
		Map imagesMap = null;
		if (ActionContext.getContext().getSession().get(ORStatics.IMAGES_MAP) != null)
		{
			imagesMap = (Map) ActionContext.getContext().getSession().get(ORStatics.IMAGES_MAP);
		}
		else
		{
			imagesMap = new HashMap();
			ActionContext.getContext().getSession().put(ORStatics.IMAGES_MAP, imagesMap);
		}

		return imagesMap;
	}	

	public void setReportLogProvider(ReportLogProvider reportLogProvider)
	{
		this.reportLogProvider = reportLogProvider;
	}

	public void setDirectoryProvider(DirectoryProvider directoryProvider)
	{
		this.directoryProvider = directoryProvider;
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
