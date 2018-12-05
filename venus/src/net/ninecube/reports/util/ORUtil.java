/*
 * Copyright (C) 2004 Erik Swenson - erik@oreports.com
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

package net.ninecube.reports.util;

import java.util.*;


import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.ninecube.reports.ORException;
import net.ninecube.reports.ORStatics;
import net.ninecube.reports.objects.ReportParameter;
import net.ninecube.reports.objects.ReportParameterMap;
import net.ninecube.reports.objects.ReportUser;
import net.ninecube.reports.providers.ParameterProvider;

public class ORUtil
{
	public static Map buildJRDesignParameters(Map parameters)
	{
		// convert parameters to JRDesignParameters so they can be parsed
		HashMap jrParameters = new HashMap();

		Iterator iterator = parameters.keySet().iterator();
		while (iterator.hasNext())
		{
			String key = (String) iterator.next();
			Object value = parameters.get(key);
			
			if (value != null)
			{
				JRDesignParameter jrParameter = new JRDesignParameter();
				jrParameter.setName(key);
				jrParameter.setValueClass(value.getClass());
				
				jrParameters.put(jrParameter.getName(), jrParameter);
			}			
		}

		return jrParameters;
	}
	
	/*
	 * Build map containing the parameter name and a test value in order to validate
	 * queries with parameters. 
	 */	
	public static Map buildQueryParameterMap(ReportUser reportUser, String queryString, ParameterProvider parameterProvider) throws ORException
	{
		String name = queryString.substring(queryString.indexOf("{") + 1, queryString.indexOf("}"));
		
		//handle standard report parameters
		if (name.equals(ORStatics.EXTERNAL_ID))
		{
			HashMap map = new HashMap();
			map.put(ORStatics.EXTERNAL_ID, reportUser.getExternalId());
			
			return map;
		}
		else if (name.equals(ORStatics.USER_ID))
		{
			HashMap map = new HashMap();
			map.put(ORStatics.USER_ID, reportUser.getId());
			
			return map;
		}
		else if (name.equals(ORStatics.USER_NAME))
		{
			HashMap map = new HashMap();
			map.put(ORStatics.USER_NAME, reportUser.getName());
			
			return map;
		}
		//
		
		ReportParameter queryParameter = parameterProvider.getReportParameter(name);						
		if (queryParameter == null)
		{			
			throw new ORException(LocalStrings.getString(LocalStrings.ERROR_PARAMETER_NOTFOUND) + ": " + name);			
		}		
		
		ReportParameterMap rpMap = new ReportParameterMap();
		rpMap.setReportParameter(queryParameter);
		
		ArrayList queryParameters = new ArrayList();
		queryParameters.add(rpMap);		
		
		Map parameterMap = new HashMap();
		if (queryParameter.getData().toUpperCase().indexOf("$P") > -1)
		{
			parameterMap = buildQueryParameterMap(reportUser, queryParameter.getData(), parameterProvider);
		}
		
		parameterProvider.loadReportParameterValues(queryParameters, parameterMap);					
		
		String testValue = queryParameter.getValues()[0].getId().toString();
		if (queryParameter.isMultipleSelect())
		{
			testValue = "'" + testValue + "'";
		}
		
		Map map = new HashMap();
		map.put(queryParameter.getName(), testValue);
		
		return map;
	}

}
