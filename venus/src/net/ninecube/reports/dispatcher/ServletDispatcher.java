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

package net.ninecube.reports.dispatcher;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import net.ninecube.reports.providers.BirtProvider;
import net.ninecube.reports.providers.HibernateProvider;
import net.ninecube.reports.providers.QuartzProvider;

import org.apache.log4j.Logger;

import com.opensymphony.webwork.config.Configuration;
import com.opensymphony.webwork.dispatcher.multipart.MultiPartRequest;
import com.opensymphony.webwork.dispatcher.multipart.MultiPartRequestWrapper;

public class ServletDispatcher
		extends
			com.opensymphony.webwork.dispatcher.ServletDispatcher 
{
	protected static Logger log = Logger.getLogger(ServletDispatcher.class);
	
	private String saveDirectory;	
	
	private HibernateProvider hibernateProvider;
	private QuartzProvider quartzProvider;
	private BirtProvider birtProvider;

	public void init(ServletConfig servletConfig) throws ServletException
	{		
		super.init(servletConfig);			

		try
		{
			hibernateProvider = new HibernateProvider();		
			quartzProvider = new QuartzProvider();
			birtProvider = new BirtProvider();
		}
		catch (Exception e)
		{
			log.error(e.toString());
		}			
	}	
	
	public void destroy()
	{		
		super.destroy();
		
		quartzProvider.destroy();
		hibernateProvider.destroy();		
		birtProvider.destroy();
	}
	
	protected void setSaveDirectory()
	{
		saveDirectory = Configuration.getString("webwork.multipart.saveDir").trim();		
		log.info("Report Upload Path: " + saveDirectory);		
	}

	protected HttpServletRequest wrapRequest(HttpServletRequest request) throws IOException
	{
		// don't wrap more than once
		if (request instanceof MultiPartRequestWrapper)
		{
			return request;
		}

		if (MultiPartRequest.isMultiPart(request))
		{
			if (saveDirectory == null) setSaveDirectory();
			request = new MultiPartRequestWrapper(request, saveDirectory, Integer.MAX_VALUE);
		}

		return request;
	}
}
