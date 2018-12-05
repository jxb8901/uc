/*
 * Copyright (C) 2002 Erik Swenson - erik@oreports.com
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

package net.ninecube.reports.providers;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.interceptor.component.ComponentManager;

import net.ninecube.reports.objects.ORProperty;
import net.ninecube.util.DateUtil;

import org.apache.log4j.Logger;

public class DateProvider implements PropertiesProviderAware
{
	protected static Logger log =
		Logger.getLogger(DateProvider.class.getName());

	protected String dateFormat = "yyyyMMdd";

	private PropertiesProvider propertiesProvider;
	
	// constructor for Spring IOC
	public DateProvider(PropertiesProvider propertiesProvider) throws ProviderException
	{
		this.propertiesProvider = propertiesProvider;			
		init();
	}	

	// constructor for WebWork IOC
	public DateProvider() throws ProviderException
	{
		
	}
	
	protected void init() throws ProviderException
	{
		
	}
	
	public String getDateFormat()
	{
		return dateFormat;
	}

	public void setDateFormat(String format)
	{
		dateFormat = format;
	}

	public Date parseDate(String date) throws ProviderException
	{
		Date ret = DateUtil.parseDate(date, this.dateFormat);
		if (ret == null) throw new ProviderException("Can't parse date '" + date + "' with pattern '" + dateFormat + "'");
		return ret;
	}
	
	public String formatDate(Date date) 
	{		
		return DateUtil.format(date, this.dateFormat);	
	}	

	public void setPropertiesProvider(PropertiesProvider propertiesProvider)
	{
		this.propertiesProvider = propertiesProvider;
	}

}
