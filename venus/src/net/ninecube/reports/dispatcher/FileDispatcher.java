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

package net.ninecube.reports.dispatcher;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ninecube.reports.ORStatics;
import net.ninecube.reports.objects.ReportUser;
import net.ninecube.reports.providers.DirectoryProvider;
import net.ninecube.reports.providers.DirectoryProviderAware;
import net.ninecube.reports.services.ReportService;
import net.ninecube.reports.util.ORComponentManager;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;



/**
 * Basic servlet for file delivery. Parses image or file name from RequestURI
 * and tries to load from the image, image temp, or report generation directory. 
 * 
 * @author Erik Swenson
 */

public class FileDispatcher extends HttpServlet implements DirectoryProviderAware
{
	private static Logger log = Logger.getLogger(FileDispatcher.class.getName());
	
	private DirectoryProvider directoryProvider;	
	
	private String imageDirectory;
	private String imageTempDirectory;
	private String reportGenerationDirectory;
	
	public void init(ServletConfig servletConfig) throws ServletException
	{		
		ORComponentManager.initializeObject(this);
		
		//If not using WebWork IOC try Spring...
		if (directoryProvider == null)
		{
			ApplicationContext appContext = WebApplicationContextUtils
				.getRequiredWebApplicationContext(servletConfig.getServletContext());
	
			directoryProvider = (DirectoryProvider) appContext.getBean("directoryProvider", ReportService.class);
		}
		
		imageDirectory = directoryProvider.getReportImageDirectory();
		imageTempDirectory = directoryProvider.getReportImageTempDirectory();
		reportGenerationDirectory = directoryProvider.getReportGenerationDirectory();

		super.init(servletConfig);

		log.info("Started...");
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		String fileName = "";
		
		try
		{
			fileName = StringUtils.substringAfterLast(request.getRequestURI(), "/");

			File file = new File(imageDirectory + fileName);
			if (!file.exists())
			{
				file = new File(imageTempDirectory + fileName);
			}
			if (!file.exists())
			{				
				fileName = request.getParameter("fileName");
				
				// report file delivery validates the filename against the username 
				// of the user in session for security purposes.
				ReportUser user = (ReportUser) request.getSession().getAttribute(ORStatics.REPORT_USER);
				if (user == null || fileName.indexOf(user.getName()) < 0)
				{
					String message = "Not Authorized...";
					response.getOutputStream().write(message.getBytes());
					
					return;
				}				
				
				file = new File(reportGenerationDirectory + fileName);
			}			

			byte[] fileDate = FileUtils.readFileToByteArray(file);

			response.setContentLength(fileDate.length);
			ServletOutputStream ouputStream = response.getOutputStream();

			ouputStream.write(fileDate, 0, fileDate.length);
			ouputStream.flush();
			ouputStream.close();
		}
		catch (Exception e)
		{
			log.warn(e);
			
			String message = "Error Loading File...";
			response.getOutputStream().write(message.getBytes());
		}
	}

	public void setDirectoryProvider(DirectoryProvider directoryProvider)
	{
		this.directoryProvider = directoryProvider;
	}		
}
