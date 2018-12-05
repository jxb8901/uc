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

package net.ninecube.reports.providers.persistence;

import java.util.List;

import net.ninecube.reports.objects.ReportAlert;
import net.ninecube.reports.providers.HibernateProvider;
import net.ninecube.reports.providers.ProviderException;
import net.ninecube.reports.util.LocalStrings;

import org.apache.log4j.Logger;


public class AlertPersistenceProvider 
{
	protected static Logger log =
		Logger.getLogger(AlertPersistenceProvider.class.getName());

	public AlertPersistenceProvider() throws ProviderException
	{
		super();

		log.info("Created");
	}

	public ReportAlert getReportAlert(Integer id)
		throws ProviderException
	{
		return (ReportAlert) HibernateProvider.load(ReportAlert.class, id);
	}

	public List getReportAlerts() throws ProviderException
	{
		String fromClause =
			"from net.ninecube.reports.objects.ReportAlert reportAlert order by reportAlert.name ";
		
		return HibernateProvider.query(fromClause);
	}

	public ReportAlert insertReportAlert(ReportAlert reportAlert)
		throws ProviderException
	{
		return (ReportAlert) HibernateProvider.save(reportAlert);
	}

	public void updateReportAlert(ReportAlert reportAlert)
		throws ProviderException
	{
		HibernateProvider.update(reportAlert);
	}

	public void deleteReportAlert(ReportAlert reportAlert)
		throws ProviderException
	{
		try
		{
			HibernateProvider.delete(reportAlert);
		}
		catch (ConstraintException ce)
		{
			throw new ProviderException(LocalStrings.getString(LocalStrings.ERROR_ALERT_DELETION), ce);
		}
	}
}
