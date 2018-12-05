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

import net.ninecube.reports.engine.output.ReportEngineOutput;
import net.ninecube.reports.services.info.ReportInfo;
import net.ninecube.reports.services.input.ReportServiceInput;

/**
 * Central Interface for external access into OpenReports report 
 * generation services.
 * 
 * @author Erik Swenson
 * @see ReportServiceImpl
 */

public interface ReportService
{
	public static final String DELIVERY_EMAIL = "EMAIL";
	public static final String DELIVERY_SCHEDULED_EMAIL = "SCHEDULED_EMAIL";
	public static final String DELIVERY_API = "API";
	public static final String DELIVERY_FAX = "FAX";
	public static final String DELIVERY_ONSCREEN = "ONSCREEN";		
	
	/**
	 * Generate report from ReportServiceInput
	 */	
	public ReportEngineOutput generateReport(ReportServiceInput reportRequest);	
	
	
	/**
	 * Gets the ReportInfo object for a given report name
	 */	
	public ReportInfo getReportInfo(String reportName);
	
	
}
