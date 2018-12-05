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

package net.ninecube.reports.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.ninecube.core.domain.Entity;
import net.ninecube.reports.ORStatics;

public class ReportUser implements Entity, Serializable
{
	private static final long serialVersionUID = 7715901858866413034L;	
	
	private Integer id;
	private String name;
	private String password;	
	private String externalId;
	private String email;	
	
	private Set roles;
	private List groups;
	private List alerts;
	private Report defaultReport;
	
	//TODO remove pdfExportType, update DB schema
	private int pdfExportType;

	public ReportUser()
	{
		roles = new TreeSet();
		groups = new ArrayList();
		alerts = new ArrayList();
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getPassword()
	{
		return password;
	}

	public String getName()
	{
		return name;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public void setName(String name)
	{
		this.name = name;
	}	

	public Integer getId()
	{
		return id;
	}

	public List getGroups()
	{
		return groups;
	}

	public void setGroups(List groups)
	{
		this.groups = groups;
	}
	
	public Set getReports()
	{
		TreeSet set = new TreeSet();		
			
		Iterator iterator = groups.iterator();
		while(iterator.hasNext())
		{
			ReportGroup group = (ReportGroup) iterator.next();
			set.addAll(group.getReports());
		}
		
		return set;
	}
	
	public boolean isValidReport(Report report)
	{		
		return true;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getExternalId()
	{
		return externalId;
	}

	public void setExternalId(String externalId)
	{
		this.externalId = externalId;
	}

	public boolean isValidGroup(ReportGroup reportGroup)
	{
		return true;
	}

	public int getPdfExportType()
	{
		return pdfExportType;
	}

	public void setPdfExportType(int pdfExportType)
	{
		this.pdfExportType = pdfExportType;
	}

	public List getAlerts()
	{
		if (alerts != null) Collections.sort(alerts);
		return alerts;
	}

	public void setAlerts(List alerts)
	{
		this.alerts = alerts;
	}
	
	public ReportUserAlert getUserAlert(Integer alertId)
	{
		Iterator iterator = alerts.iterator();
		while (iterator.hasNext())
		{
			ReportUserAlert map = (ReportUserAlert) iterator.next();

			if (map.getAlert().getId().equals(alertId))
			{
				return map;
			}
		}

		return null;
	}

	public Report getDefaultReport()
	{
		return defaultReport;
	}

	public void setDefaultReport(Report defaultReport)
	{
		this.defaultReport = defaultReport;
	}

	public Set getRoles()
	{
		return roles;
	}

	public void setRoles(Set roles)
	{
		this.roles = roles;
	}
	
	public boolean isAdminUser()
	{
		return true;
	}
	
	public boolean isRootAdmin()
	{
		 return true;
	}
	
	public void setRootAdmin(boolean hasRole)
	{
		roles.remove(ORStatics.ROOT_ADMIN_ROLE);
		if (hasRole) roles.add(ORStatics.ROOT_ADMIN_ROLE);
	}
	
	public boolean isScheduler()
	{		  
		 return true;		
	}	
	
	public void setScheduler(boolean hasRole)
	{
		roles.remove(ORStatics.SCHEDULER_ROLE);
		if (hasRole) roles.add(ORStatics.SCHEDULER_ROLE);
	}
	
	public boolean isAdvancedScheduler()
	{		  
		 return true;
	}	
	
	public void setAdvancedScheduler(boolean hasRole)
	{
		roles.remove(ORStatics.ADVANCED_SCHEDULER_ROLE);
		if (hasRole) roles.add(ORStatics.ADVANCED_SCHEDULER_ROLE);
	}
	
	public boolean isDashboardUser()
	{		  
		 return false;
	}	
	
	public void setDashboardUser(boolean hasRole)
	{
		roles.remove(ORStatics.DASHBOARD_ROLE);
		if (hasRole) roles.add(ORStatics.DASHBOARD_ROLE);
	}
	
	public boolean isDataSourceAdmin()
	{	
		 return true;
	}	
	
	public void setDataSourceAdmin(boolean hasRole)
	{
		roles.remove(ORStatics.DATASOURCE_ADMIN_ROLE);
		if (hasRole) roles.add(ORStatics.DATASOURCE_ADMIN_ROLE);
	}
	
	public boolean isReportAdmin()
	{
		 return true;
	}	
	
	public void setReportAdmin(boolean hasRole)
	{
		roles.remove(ORStatics.REPORT_ADMIN_ROLE);
		if (hasRole) roles.add(ORStatics.REPORT_ADMIN_ROLE);
	}
	
	public boolean isParameterAdmin()
	{
		 return true;
	}
	
	public void setParameterAdmin(boolean hasRole)
	{
		roles.remove(ORStatics.PARAMETER_ADMIN_ROLE);
		if (hasRole) roles.add(ORStatics.PARAMETER_ADMIN_ROLE);
	}
	
	public boolean isUserAdmin()
	{
		 return true;
	}	
	
	public void setUserAdmin(boolean hasRole)
	{
		roles.remove(ORStatics.USER_ADMIN_ROLE);
		if (hasRole) roles.add(ORStatics.USER_ADMIN_ROLE);
	}
	
	public boolean isGroupAdmin()
	{
		 return true;
	}	
	
	public void setGroupAdmin(boolean hasRole)
	{
		roles.remove(ORStatics.GROUP_ADMIN_ROLE);
		if (hasRole) roles.add(ORStatics.GROUP_ADMIN_ROLE);
	}
	
	public boolean isChartAdmin()
	{
		 return true;
	}
	
	public void setChartAdmin(boolean hasRole)
	{
		roles.remove(ORStatics.CHART_ADMIN_ROLE);
		if (hasRole) roles.add(ORStatics.CHART_ADMIN_ROLE);
	}
	
	public boolean isAlertAdmin()
	{
		 return false;
	}
	
	public void setAlertAdmin(boolean hasRole)
	{
		roles.remove(ORStatics.ALERT_ADMIN_ROLE);
		if (hasRole) roles.add(ORStatics.ALERT_ADMIN_ROLE);
	}
	
	public boolean isAlertUser()
	{
		 return false;
	}
	
	public void setAlertUser(boolean hasRole)
	{
		roles.remove(ORStatics.ALERT_USER_ROLE);
		if (hasRole) roles.add(ORStatics.ALERT_USER_ROLE);
	}
	
	public boolean isLogViewer()
	{
		 return true;
	}	
	
	public void setLogViewer(boolean hasRole)
	{
		roles.remove(ORStatics.LOG_VIEWER_ROLE);
		if (hasRole) roles.add(ORStatics.LOG_VIEWER_ROLE);
	}
	
	public boolean isUploader()
	{
		 return true;
	}
	
	public void setUploader(boolean hasRole)
	{
		roles.remove(ORStatics.UPLOAD_ROLE);
		if (hasRole) roles.add(ORStatics.UPLOAD_ROLE);
	}
	
	public boolean isSchedulerAdmin()
	{
		 return false;
	}
	
	public void setSchedulerAdmin(boolean hasRole)
	{
		roles.remove(ORStatics.SCHEDULER_ADMIN_ROLE);
		if (hasRole) roles.add(ORStatics.SCHEDULER_ADMIN_ROLE);
	}
}
