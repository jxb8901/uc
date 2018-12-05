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

package net.ninecube.reports.services.input;

import java.io.Serializable;
import java.util.Date;

import net.ninecube.reports.engine.ReportEngine;
import net.ninecube.reports.objects.ReportSchedule;
import net.ninecube.reports.services.ReportService;

/**
 * Standard ReportServiceInput object. UserName and ReportName are required, the rest
 * are optional. If not specified, exportType defaults to PDF and deliveryMethod
 * defaults to API.
 * 
 * @author Erik Swenson
 */

public class ReportServiceInput implements Serializable
{	
	private static final long serialVersionUID = -3094443722330870862L;	
	
	private String userName;
	private String reportName;
	private int exportType = ReportEngine.EXPORT_PDF;
	private String deliveryMethod = ReportService.DELIVERY_API;	
	private String[] parameters;		
	
	// schedule options	
	private Date startDate;		
	private String startHour;
	private String startMinute;
	private String startAmPm;
	private int scheduleType = ReportSchedule.ONCE;		
	private String scheduleDescription;
	private int hours;	
	private String cronExpression;
	// 
	
	// not used
	private String xmlData;	
	
	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getDeliveryMethod()
	{
		return deliveryMethod;
	}
	
	public void setDeliveryMethod(String deliveryMethod)
	{
		this.deliveryMethod = deliveryMethod;
	}	

	public int getExportType()
	{
		return exportType;
	}

	public void setExportType(int exportType)
	{
		this.exportType = exportType;
	}

	public String[] getParameters()
	{
		return parameters;
	}
	
	/**
	 * String array of parameters. String must be in the following format:
	 * 'city=Boston'
	 */
	public void setParameters(String[] parameters)
	{
		this.parameters = parameters;
	}	
	
	public String getReportName()
	{
		return reportName;
	}
	
	public void setReportName(String reportName)
	{
		this.reportName = reportName;
	}

	public String getXmlData()
	{
		return xmlData;
	}

	public void setXmlData(String xmlData)
	{
		this.xmlData = xmlData;
	}

	public int getScheduleType()
	{
		return scheduleType;
	}

	public void setScheduleType(int scheduleType)
	{
		this.scheduleType = scheduleType;
	}

	public String getStartAmPm()
	{
		return startAmPm;
	}

	public void setStartAmPm(String startAmPm)
	{
		this.startAmPm = startAmPm;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public String getStartHour()
	{
		return startHour;
	}

	public void setStartHour(String startHour)
	{
		this.startHour = startHour;
	}

	public String getStartMinute()
	{
		return startMinute;
	}

	public void setStartMinute(String startMinute)
	{
		this.startMinute = startMinute;
	}

	public String getScheduleDescription()
	{
		return scheduleDescription;
	}

	public void setScheduleDescription(String scheduleDescription)
	{
		this.scheduleDescription = scheduleDescription;
	}

	public String getCronExpression()
	{
		return cronExpression;
	}

	public void setCronExpression(String cronExpression)
	{
		this.cronExpression = cronExpression;
	}

	public int getHours()
	{
		return hours;
	}

	public void setHours(int hours)
	{
		this.hours = hours;
	}		
}
