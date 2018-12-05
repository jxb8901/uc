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

package net.ninecube.reports.services.servlet;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ninecube.reports.engine.ReportEngine;
import net.ninecube.reports.engine.output.QueryEngineOutput;
import net.ninecube.reports.engine.output.ReportEngineOutput;
import net.ninecube.reports.services.ReportService;
import net.ninecube.reports.services.input.ServletReportServiceInput;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Basic servlet for onscreen report delivery.  The service method uses
 * reflection to build a ServletReportServiceInput from the ServletRequest parameters 
 * and calls the generateReport method of the ReportService.
 * 
 * @author Erik Swenson
 */

public class ReportServiceServlet extends HttpServlet
{
	private static Logger log = Logger.getLogger(ReportServiceServlet.class.getName());
	
	private ReportService reportService;	
	private PropertyDescriptor[] descriptors;
	
	public void init(ServletConfig servletConfig) throws ServletException
	{		
		ApplicationContext appContext = WebApplicationContextUtils
				.getRequiredWebApplicationContext(servletConfig.getServletContext());
	
		reportService = (ReportService) appContext.getBean("reportService", ReportService.class);
		
		//cache ServletReportServiceInput PropertyDescriptors
		descriptors = PropertyUtils.getPropertyDescriptors(ServletReportServiceInput.class);

		super.init(servletConfig);

		log.info("Started...");
	}	
	
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		try
		{				
			ServletReportServiceInput reportInput = buildReportServiceInput(request);			
			reportInput.setParameterMap(request.getParameterMap());

			ReportEngineOutput reportOutput = reportService.generateReport(reportInput);
			
			if (reportOutput instanceof QueryEngineOutput)
			{
				QueryEngineOutput queryReport = (QueryEngineOutput) reportOutput;
				
				request.getSession().setAttribute("properties", queryReport.getProperties());
				request.getSession().setAttribute("results", queryReport.getResults());
				request.getSession().setAttribute("reportName", StringUtils.deleteWhitespace(reportInput.getReportName()));
				
				response.sendRedirect("report-viewer/query-report.jsp");
				
				//getServletContext().getRequestDispatcher("/report-viewer/query-report.jsp").forward(request, response);
				
				return;
			}
			
			ServletOutputStream out = response.getOutputStream();
			
			if (reportOutput.getContent() != null)
			{				
				if (reportInput.getExportType() != ReportEngine.EXPORT_HTML)
				{
					response.setContentType(reportOutput.getContentType());
					response.setContentLength(reportOutput.getContent().length);
					response.setHeader("Content-disposition", "inline; filename="
							+ StringUtils.deleteWhitespace(reportInput.getReportName())
							+ reportOutput.getContentExtension());
				}

				out.write(reportOutput.getContent(), 0, reportOutput.getContent().length);				
			}			
			else
			{
				out.write(reportOutput.getContentMessage().getBytes());				
			}
			
			out.flush();
			out.close();
		}
		catch (Exception e)
		{
			response.getOutputStream().write(e.toString().getBytes());
		}
	}	
	
	/*
	 * Use reflection to build a ServletReportServiceInput from a HttpServletRequest.
	 * HttpServletRequest parameter name must match ServletReportServiceInput
	 * property name and the property type must be an int or String.
	 * 
	 */
	protected ServletReportServiceInput buildReportServiceInput(HttpServletRequest request)
			throws IllegalAccessException, InvocationTargetException
	{
		ServletReportServiceInput reportInput = new ServletReportServiceInput();		
		
		for (int i = 0; i < descriptors.length; i++)
		{
			String value = request.getParameter(descriptors[i].getName());
			if (value != null)
			{
				Method method = descriptors[i].getWriteMethod();
				
				log.debug("Building ServletReportRequest: " + descriptors[i].getName()
						+ " | " + method.getName() + " | " + value);

				if (descriptors[i].getPropertyType() == int.class)
				{
					method.invoke(reportInput, new Integer[]{Integer.valueOf(value)});
				}
				else if (descriptors[i].getPropertyType() == String.class)
				{
					method.invoke(reportInput, new String[]{value});
				}
			}
		}		

		return reportInput;
	}
}
