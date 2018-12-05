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

package net.ninecube.reports.engine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.util.JRQueryExecuter;
import net.ninecube.reports.engine.input.ReportEngineInput;
import net.ninecube.reports.engine.output.QueryEngineOutput;
import net.ninecube.reports.engine.output.ReportEngineOutput;
import net.ninecube.reports.objects.ORProperty;
import net.ninecube.reports.objects.Report;
import net.ninecube.reports.objects.ReportDataSource;
import net.ninecube.reports.providers.DataSourceProvider;
import net.ninecube.reports.providers.DirectoryProvider;
import net.ninecube.reports.providers.PropertiesProvider;
import net.ninecube.reports.providers.ProviderException;
import net.ninecube.reports.util.DisplayProperty;
import net.ninecube.reports.util.ORUtil;
import net.ninecube.reports.util.VenusRowSetDynaClass;

import org.apache.commons.beanutils.DynaProperty;
import org.apache.log4j.Logger;

/**
*  QueryReport ReportEngine implementation.
* 
* @author Erik Swenson
* 
*/
public class QueryReportEngine extends ReportEngine
{
	protected static Logger log = Logger.getLogger(QueryReportEngine.class);		
	
	public QueryReportEngine(DataSourceProvider dataSourceProvider,
			DirectoryProvider directoryProvider, PropertiesProvider propertiesProvider)
	{
		super(dataSourceProvider,directoryProvider, propertiesProvider);
	}
	
	public ReportEngineOutput generateReport(ReportEngineInput input) throws ProviderException
	{
		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rs = null;

		try
		{
			Report report = input.getReport();
			Map parameters = input.getParameters();
			
			conn = dataSourceProvider.getConnection(report.getDataSource());

			if (parameters == null || parameters.isEmpty())
			{
				pStmt = conn.prepareStatement(report.getQuery());
			}
			else
			{
				// Use JasperReports Query logic to parse parameters in chart
				// queries

				JRDesignQuery query = new JRDesignQuery();
				query.setText(report.getQuery());

				// convert parameters to JRDesignParameters so they can be
				// parsed
				Map jrParameters = ORUtil.buildJRDesignParameters(parameters);

				pStmt = JRQueryExecuter.getStatement(query, jrParameters, parameters,
						conn);
			}

			ORProperty maxRows = propertiesProvider
					.getProperty(ORProperty.QUERYREPORT_MAXROWS);
			if (maxRows != null && maxRows.getValue() != null)
			{
				pStmt.setMaxRows(Integer.parseInt(maxRows.getValue()));
			}

			rs = pStmt.executeQuery();

			VenusRowSetDynaClass rowSetDynaClass = new VenusRowSetDynaClass(rs);

			List results = rowSetDynaClass.getRows();

			DynaProperty[] dynaProperties = rowSetDynaClass.getDynaProperties();

			DisplayProperty[] properties = new DisplayProperty[dynaProperties.length];
			for (int i = 0; i < dynaProperties.length; i++)
			{
				properties[i] = new DisplayProperty(dynaProperties[i].getName(),
						dynaProperties[i].getType().getName());
			}

			rs.close();
			
			QueryEngineOutput output = new QueryEngineOutput();
			output.setResults(results);
			output.setProperties(properties);
			
			return output;
		}
		catch (Exception e)
		{
			throw new ProviderException("Error executing report query: " + e.getMessage(), e);
		}
		finally
		{
			try
			{
				if (pStmt != null) pStmt.close();
				if (conn != null) conn.close();
			}
			catch (Exception c)
			{
				log.error("Error closing", c);
			}
		}
	}	
	
	public List buildParameterList(Report report) throws ProviderException
	{
		throw new ProviderException("QueryReportEngine: buildParameterList not implemented.");
	}		
}
