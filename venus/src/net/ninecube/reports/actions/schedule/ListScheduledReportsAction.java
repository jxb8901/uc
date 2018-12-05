/*
 * Copyright (C) 2002 Erik Swenson - eswenson@opensourcesoft.net
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

package net.ninecube.reports.actions.schedule;

import java.util.List;

import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionSupport;

import net.ninecube.reports.ORStatics;
import net.ninecube.reports.objects.ReportUser;
import net.ninecube.reports.providers.ProviderException;
import net.ninecube.reports.providers.SchedulerProvider;
import net.ninecube.reports.providers.SchedulerProviderAware;

public class ListScheduledReportsAction
	extends ActionSupport
	implements SchedulerProviderAware
{
	private List scheduledReports;

	private SchedulerProvider schedulerProvider;

	public String execute()
	{
		try
		{
			ReportUser reportUser =
				(ReportUser) ActionContext.getContext().getSession().get(
					ORStatics.REPORT_USER);

			scheduledReports =
				schedulerProvider.getScheduledReports(reportUser);
		}
		catch (ProviderException pe)
		{
			addActionError(pe.getMessage());
			return ERROR;
		}

		return SUCCESS;
	}

	public void setSchedulerProvider(SchedulerProvider schedulerProvider)
	{
		this.schedulerProvider = schedulerProvider;
	}

	public List getScheduledReports()
	{
		return scheduledReports;
	}

}
