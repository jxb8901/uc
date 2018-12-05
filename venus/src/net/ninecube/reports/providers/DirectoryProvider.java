/*
 * Copyright (C) 2003 Erik Swenson - erik@oreports.com
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

import javax.servlet.ServletContext;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.webwork.config.Configuration;
import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.interceptor.component.ComponentManager;

import net.ninecube.reports.objects.ORProperty;

import org.apache.log4j.Logger;
import org.springframework.web.context.ServletContextAware;

public class DirectoryProvider implements PropertiesProviderAware, ServletContextAware
{
	protected static Logger log = Logger.getLogger(DirectoryProvider.class.getName());

	private String reportDirectory;
	private String reportImageDirectory;
	private String reportGenerationDirectory;
	private String tempDirectory;
	private String separator = System.getProperty("file.separator");	
	private ServletContext servletContext;

	private PropertiesProvider propertiesProvider;
	
	// constructor for Spring IOC
	public DirectoryProvider(PropertiesProvider propertiesProvider) throws ProviderException
	{
		this.propertiesProvider = propertiesProvider;
	}

	// constructor for WebWork IOC
	public DirectoryProvider() throws ProviderException
	{
		// try getting baseDirectory from properties
		ComponentManager container = (ComponentManager) ActionContext.getContext().get(
				"com.opensymphony.xwork.interceptor.component.ComponentManager");

		container.initializeObject(this);
		
		init();
	}
	
	protected void init() throws ProviderException
	{
		log.info("Loading BaseDirectory from OR_PROPERTIES table.");

		String baseDirectory = null;

//		ORProperty property = propertiesProvider.getProperty(ORProperty.BASE_DIRECTORY);		
//		if (property != null) baseDirectory = property.getValue(); 
		
//		if (baseDirectory == null || baseDirectory.trim().length() < 1)
//		{
//			log.info("BaseDirectory not set in OR_PROPERTIES table. Trying to get path from ServletContext.");

			try
			{
				ServletContext sc = this.servletContext;
				if (sc == null) sc = ServletActionContext.getServletContext();
				baseDirectory = sc.getRealPath("");
				baseDirectory = baseDirectory + separator + "WEB-INF" + separator + "reports";
			}
			catch (NullPointerException npe)
			{
				log.info("ServletActionContext not available.");
				baseDirectory = ".";
			}
//		}

		log.info("BaseDirectory: " + baseDirectory);
		
		reportDirectory = baseDirectory + separator;
		reportImageDirectory = reportDirectory + "images" + separator;			
		
		//set path for uploaded reports
//		Configuration.set("webwork.multipart.saveDir", baseDirectory);
		
		//set temp directory path for report virtualization
		tempDirectory = reportDirectory + "temp" + separator;
//		ORProperty property = propertiesProvider.getProperty(ORProperty.TEMP_DIRECTORY);		
//		if (property != null && property.getValue() != null && property.getValue().trim().length() > 0)
//		{
//			tempDirectory = property.getValue(); 
//			log.info("TempDirectory: " + tempDirectory);
//		}
		
		//set report generation directory path for storing generated reports
		reportGenerationDirectory = reportDirectory + "generated" + separator;
//		property = propertiesProvider.getProperty(ORProperty.REPORT_GENERATION_DIRECTORY);		
//		if (property != null && property.getValue() != null && property.getValue().trim().length() > 0)
//		{
//			reportGenerationDirectory = property.getValue(); 
//			log.info("ReportGenerationDirectory: " + reportGenerationDirectory);
//		}		
		
		log.info("Created");
	}

	public String getReportDirectory()
	{
		return reportDirectory;
	}

	public void setReportDirectory(String reportDirectory)
	{
		this.reportDirectory = reportDirectory;
		reportImageDirectory = reportDirectory + "images" + separator;	
	}
	
	public String getReportImageDirectory()
	{
		return reportImageDirectory;
	}
	
	public String getReportImageTempDirectory()
	{
		return reportImageDirectory + "temp" + separator;
	}
	
	public String getTempDirectory()
	{
		return tempDirectory;
	}
	
	public void setTempDirectory(String tempDirectory)
	{
		this.tempDirectory = tempDirectory;
	}

	public String getReportGenerationDirectory()
	{
		return reportGenerationDirectory + separator;
	}

	public void setReportGenerationDirectory(String reportGenerationDirectory)
	{
		this.reportGenerationDirectory = reportGenerationDirectory;
	}

	public void setPropertiesProvider(PropertiesProvider propertiesProvider)
	{
		this.propertiesProvider = propertiesProvider;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

}
