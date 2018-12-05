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

package net.ninecube.reports.services;

import java.util.ArrayList;
import java.util.Iterator;

import net.ninecube.reports.objects.ReportUser;
import net.ninecube.reports.objects.ReportUserAlert;
import net.ninecube.reports.providers.AlertProvider;
import net.ninecube.reports.providers.ProviderException;
import net.ninecube.reports.providers.UserProvider;
import net.ninecube.reports.services.info.AlertInfo;
import net.ninecube.reports.services.util.Converter;

import org.apache.log4j.Logger;

/**
 * AlertService implementation using standard OpenReports providers. 
 * 
 * @author Erik Swenson
 */

public class AlertServiceImpl implements AlertService
{
	private static Logger log = Logger.getLogger(AlertServiceImpl.class.getName());

	private AlertProvider alertProvider;
	private UserProvider userProvider;

	public AlertServiceImpl()
	{
		log.info("AlertService: Started");
	}		
	
	public AlertInfo[] getAlerts(String userName)
	{
		ReportUser user = null;
		
		try
		{
			user = userProvider.getUser(userName);
		}
		catch(ProviderException pe)
		{
			log.warn(pe);
			return null;
		}		
		
		ArrayList alerts = new ArrayList();
		
		Iterator iterator = user.getAlerts().iterator();	
		while(iterator.hasNext())
		{			
			ReportUserAlert userAlert = (ReportUserAlert) iterator.next();				
			userAlert.setUser(user);
			
			try
			{
				userAlert = alertProvider.executeAlert(userAlert, false);			

				alerts.add(Converter.convertToAlertInfo(userAlert));
			}
			catch (ProviderException e)
			{
				AlertInfo alertInfo = new AlertInfo();
				alertInfo.setMessage(e.toString());
				
				alerts.add(alertInfo);
			}
		}	
		
		AlertInfo[] alertInfos = new AlertInfo[alerts.size()];
		alerts.toArray(alertInfos);
		
		return alertInfos;		
	}
	
	public void setAlertProvider(AlertProvider alertProvider)
	{
		this.alertProvider = alertProvider;
	}

	public void setUserProvider(UserProvider userProvider)
	{
		this.userProvider = userProvider;
	}	
}
