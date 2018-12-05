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

package net.ninecube.reports.services.util;

import java.util.ArrayList;

import net.ninecube.reports.engine.ReportEngine;
import net.ninecube.reports.objects.Report;
import net.ninecube.reports.objects.ReportParameterMap;
import net.ninecube.reports.objects.ReportUser;
import net.ninecube.reports.objects.ReportUserAlert;
import net.ninecube.reports.services.info.AlertInfo;
import net.ninecube.reports.services.info.ParameterInfo;
import net.ninecube.reports.services.info.ReportInfo;
import net.ninecube.reports.services.info.UserInfo;

/**
 * Provides static methods for converting standard OpenReports objects
 * to info objects.
 * 
 * @author Erik Swenson
 */

public class Converter
{
	public static ReportInfo convertToReportInfo(Report report)
	{		
		ReportInfo reportInfo = new ReportInfo();	
		reportInfo.setId(report.getId());
		reportInfo.setName(report.getName());
		reportInfo.setDescription(report.getDescription());
		reportInfo.setCsvExportEnabled(report.isCsvExportEnabled());
		reportInfo.setExcelExportEnabled(report.isExcelExportEnabled());
		reportInfo.setHtmlExportEnabled(report.isHtmlExportEnabled());
		reportInfo.setPdfExportEnabled(report.isPdfExportEnabled());
		reportInfo.setRtfExportEnabled(report.isRtfExportEnabled());
		reportInfo.setTextExportEnabled(report.isTextExportEnabled());
		reportInfo.setXlsExportEnabled(report.isXlsExportEnabled());
		reportInfo.setHidden(report.isHidden());
		
		if (report.isJXLSReport())
		{
			reportInfo.setDefaultExportType(ReportEngine.EXPORT_XLS);
		}
		else if (report.isHtmlExportEnabled() || report.isQueryReport() || report.isChartReport())
		{
			reportInfo.setDefaultExportType(ReportEngine.EXPORT_HTML);
		}
		else
		{
			reportInfo.setDefaultExportType(ReportEngine.EXPORT_PDF);
		}
		
		if (report.getParameters() != null && report.getParameters().size() > 0)
		{
			ArrayList reportParameters = new ArrayList(report.getParameters());
			
			ParameterInfo[] parameters = new ParameterInfo[reportParameters.size()];
			for (int x = 0; x < reportParameters.size(); x++)
			{
				ReportParameterMap reportParameter = (ReportParameterMap) reportParameters.get(x);
				
				ParameterInfo parameter = new ParameterInfo();
				parameter.setId(reportParameter.getReportParameter().getId());
				parameter.setName(reportParameter.getReportParameter().getName());
				parameter.setDescription(reportParameter.getReportParameter().getDescription());
				
				parameters[x] = parameter;
			}
			
			reportInfo.setParameters(parameters);
		}
							
		return reportInfo;
	}
	
	public static UserInfo convertToUserInfo(ReportUser reportUser)
	{
		UserInfo userInfo = new UserInfo();
		userInfo.setId(reportUser.getId());
		userInfo.setName(reportUser.getName());
		userInfo.setPassword(reportUser.getPassword());
		userInfo.setEmailAddress(reportUser.getEmail());
		userInfo.setExternalId(reportUser.getExternalId());
		
		return userInfo;
	}
	
	public static AlertInfo convertToAlertInfo(ReportUserAlert userAlert)
	{
		AlertInfo alertInfo = new AlertInfo();
		alertInfo = new AlertInfo();
		alertInfo.setAlertName(userAlert.getAlert().getName());
		alertInfo.setCondition(userAlert.getCondition());
		alertInfo.setCount(userAlert.getCount());
		alertInfo.setReportName(userAlert.getReport().getName());
		alertInfo.setTriggered(userAlert.isTriggered());
		
		return alertInfo;
	}
}
