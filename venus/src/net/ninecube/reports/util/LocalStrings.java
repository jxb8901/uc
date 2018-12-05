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

package net.ninecube.reports.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class LocalStrings
{
	public static final String ERROR_REPORT_EMPTY = "error.report.empty";
	public static final String ERROR_VALIDATION_NOSUPPORTED = "error.validation.notsupported";	
	public static final String ERROR_EXPORTTYPE_REQUIRED = "error.exporttype.required";
	public static final String ERROR_INVALID_PASSWORD = "error.invalid.password";
	public static final String ERROR_LOGIN_INCOMPLETE = "error.login.incomplete";
	public static final String ERROR_LOGIN_INVALID = "error.login.invalid";
	public static final String ERROR_REPORT_NOTAUTHORIZED = "error.report.notauthorized";
	public static final String ERROR_REPORT_INVALID = "error.report.invalid";
	public static final String ERROR_REPORTGROUP_NOTAUTHORIZED = "error.reportgroup.notauthorized";
	public static final String ERROR_EMAILADDRESS_REQUIRED = "error.emailaddress.required";
	public static final String ERROR_DATEANDTIME_REQUIRED = "error.dateandtime.required";
	public static final String ERROR_DATEANDTIME_INVALID = "error.dateandtime.invalid";
	public static final String ERROR_NOTLOGGEDIN = "error.notloggedin";
	public static final String ERROR_NOTAUTHORIZED = "error.notauthorized";
	public static final String ERROR_ALERTQUERY_INVALID = "error.alertquery.invalid";
	public static final String ERROR_CHARTQUERY_INVALID = "error.chartquery.invalid";
	public static final String ERROR_TESTING_CONNECTION = "error.testing.connection";
	public static final String ERROR_PARAMETER_REQUIRED = "error.parameter.required";
	public static final String ERROR_CHART_DELETION = "error.chart.deletion";
	public static final String ERROR_DATASOURCE_DELETION = "error.datasource.deletion";
	public static final String ERROR_GROUP_DELETION = "error.group.deletion";
	public static final String ERROR_ALERT_DELETION = "error.alert.deletion";
	public static final String ERROR_PARAMETER_DELETION = "error.parameter.deletion";
	public static final String ERROR_PARAMETER_NOTFOUND = "error.parameter.notfound";
	public static final String ERROR_REPORT_DELETION = "error.report.deletion";
	public static final String ERROR_INVALID_USER = "error.invalid.user";
	public static final String ERROR_UNIQUE_CONSTRAINT = "error.unique.constraint";	
	public static final String ERROR_INVALID_MAXROWS = "error.invalid.maxrows";
	public static final String ALERT_PASSED = "alert.passed";
	public static final String ALERT_FAILED = "alert.failed";
					
	public static final String MESSAGE_SCHEDULE_SUCCESSFUL = "message.schedule.successful";
	
	// service messages
	public static final String SERVICE_REQUEST_COMPLETE = "service.request.complete";
		
	//private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
	//		.getBundle("LocalStrings");  

	private static final java.util.Map<String, String> map = new java.util.HashMap<String, String>();
	static {
map.put("error.report.empty","The report is empty. Please try different parameters.");
map.put("error.validation.notsupported","Validation not supported on queries with parameters.");
map.put("error.exporttype.required","You must select at least one Export Type.");
map.put("error.invalid.password","Invalid Password: Both passwords must be equal.");
map.put("error.login.incomplete","Please Enter Both User Name and Password.");
map.put("error.login.invalid","Invalid User Name or Password!");
map.put("error.report.invalid","Invalid report.");
map.put("error.report.notauthorized","You are not authorized to view this Report.");
map.put("error.reportgroup.notauthorized","You are not authorized to view this Report Group.");
map.put("error.emailaddress.required","You must update your email address before scheduling reports!");
map.put("error.dateandtime.required","Start Date and Time required to schedule report.");
map.put("error.dateandtime.invalid","Start Time invalid.");
map.put("error.notauthorized","You are not authorized to perform this action. ");
map.put("error.notloggedin","You are not currently logged into the system. ");
map.put("error.invalid.user","You are not a valid OpenReports User. Please contact Administrator.");
map.put("error.chartquery.invalid","Invalid Chart Query");
map.put("error.alertquery.invalid","Invalid Alert Query");
map.put("error.testing.connection","Error testing connection, verify DataSource configuration and try again");
map.put("error.parameter.required","Please Fill Required Parameter");
map.put("error.chart.deletion","Report Chart must be removed from all reports before deletion.");
map.put("error.datasource.deletion","This DataSource is being used by Reports or Parameters and cannot be deleted.");
map.put("error.group.deletion","Report Group must be removed from all users before deletion.");
map.put("error.parameter.deletion","Report Parameter must be removed from all reports before deletion.");
map.put("error.report.deletion","Report must be removed from all Report Groups before deletion.");
map.put("error.parameter.notfound","Parameter not found");
map.put("error.alert.deletion","This alert can not be deleted.");
map.put("error.unique.constraint","This name is already in use. Please use a unique name.");
map.put("error.invalid.maxrows","QueryReport MaxRows must be set to a valid number. 500 is recommended.");
map.put("alert.failed","Failed");
map.put("alert.passed","Passed");
map.put("message.schedule.successful","Report scheduled successfully...");
map.put("service.request.complete","COMPLETE");
	}

	public static String getString(String key)
	{
		String ret = map.get(key);
		if (ret == null) ret = '!' + key + '!';
		return ret;
		/* try
		{
			return RESOURCE_BUNDLE.getString(key);
		}
		catch (MissingResourceException e)
		{
			return '!' + key + '!';
		} */
	}
}
