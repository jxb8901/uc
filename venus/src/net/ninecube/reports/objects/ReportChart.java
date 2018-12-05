/*
 * Copyright (C) 2003 Erik Swenson - erik@oreports.com
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

import net.ninecube.core.domain.Entity;

public class ReportChart extends ReportEntity implements Entity, Serializable
{	
	private static final long serialVersionUID = 7406441909199255551L;
	
	public static final int BAR_CHART = 0;
	public static final int PIE_CHART = 1;
	public static final int XY_CHART = 2;
	public static final int TIME_CHART = 3;
	public static final int RING_CHART = 4;	
	
	public static final int HORIZONTAL = 1;
	public static final int VERTICAL = 2;
	
	public static final String DRILLDOWN_PARAMETER = "DrillDown";
	
	private Integer id;
	private String name;
	private String description;
	private String query;
	
	private int chartType = BAR_CHART;
	private int width = 600;
	private int height = 400;
	
	private String xaxisLabel;
	private String yaxisLabel;	
	
	private boolean showLegend = false; 	
	private boolean showTitle = false;
	private boolean showValues = false;
	private int plotOrientation = VERTICAL;

	private ReportDataSource reportDataSource;
	
	private Report drillDownReport;

	public ReportChart()
	{
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String toString()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}

	public String getTitle()
	{
		if (showTitle) return description;
		return null;
	}
	
	public Integer getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int compareTo(Object object)
	{
		ReportChart reportChart = (ReportChart) object;
		return name.compareTo(reportChart.getName());
	}

	public ReportDataSource getReportDataSource()
	{
		return reportDataSource;
	}

	public void setReportDataSource(ReportDataSource dataSource)
	{
		this.reportDataSource = dataSource;
	}

	public String getQuery()
	{
		return query;
	}

	public void setQuery(String query)
	{
		this.query = query;
	}

	public int getChartType()
	{
		return chartType;
	}

	public void setChartType(int chartType)
	{
		this.chartType = chartType;
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public String getXaxisLabel()
	{
		return xaxisLabel;
	}

	public void setXaxisLabel(String axisLabel)
	{
		xaxisLabel = axisLabel;
	}

	public String getYaxisLabel()
	{
		return yaxisLabel;
	}

	public void setYaxisLabel(String axisLabel)
	{
		yaxisLabel = axisLabel;
	}

	public boolean isShowLegend()
	{
		return showLegend;
	}

	public void setShowLegend(boolean showLegend)
	{
		this.showLegend = showLegend;
	}
	
	public boolean isShowTitle()
	{
		return showTitle;
	}

	public void setShowTitle(boolean showTitle)
	{
		this.showTitle = showTitle;
	}		

	public int getPlotOrientation()
	{
		return plotOrientation;
	}

	public void setPlotOrientation(int plotOrientation)
	{
		this.plotOrientation = plotOrientation;
	}

	public Report getDrillDownReport()
	{
		return drillDownReport;
	}

	public void setDrillDownReport(Report drillDownReport)
	{
		this.drillDownReport = drillDownReport;
	}

	public boolean isShowValues()
	{
		return showValues;
	}

	public void setShowValues(boolean showValues)
	{
		this.showValues = showValues;
	}	
}
