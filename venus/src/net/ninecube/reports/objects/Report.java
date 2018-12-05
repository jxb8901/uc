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
import java.util.*;

import net.ninecube.core.domain.Entity;
import net.ninecube.util.StringUtil;

public class Report extends ReportEntity implements Entity, Comparable, Serializable
{
	private static final long serialVersionUID = 4068258161793785996l;

	private Integer id;

	private String name;

	private String description;

	private String file = "";

	private String query;

	private ReportDataSource dataSource;

	private ReportChart reportChart;

	private List parameters;

	private boolean pdfExportEnabled;

	private boolean htmlExportEnabled = true;

	private boolean csvExportEnabled;

	private boolean xlsExportEnabled;

	private boolean rtfExportEnabled;

	private boolean textExportEnabled;

	private boolean excelExportEnabled;
	
	private boolean virtualizationEnabled;
	
	private boolean hidden = false ;

	private ReportExportOption reportExportOption;
	
	private transient boolean displayInline;

	public Report()
	{
	}

	public boolean isBirtReport()
	{
		if (!StringUtil.isEmpty(file) && file.endsWith("rptdesign")) return true;
		
		return false;
	}

	public boolean isJasperReport()
	{
		if (!StringUtil.isEmpty(file) && (file.endsWith("jasper") || file.endsWith("jrxml"))) return true;
		
		return false;
	}
	
	public boolean isJXLSReport()
	{
		if (!StringUtil.isEmpty(file) && file.endsWith("xls"))
		{
			return true;
		}

		return false;
	}
	
	public boolean isQueryReport()
	{
		if (!StringUtil.isEmpty(query) && (StringUtil.isEmpty(file) || !file.endsWith("xls") && !file.endsWith("xml")))
		{
			return true;
		}

		return false;
	}

	public boolean isChartReport()
	{
		if (StringUtil.isEmpty(query) && StringUtil.isEmpty(file) && reportChart != null)
		{
			return true;
		}

		return false;
	}
	
	public boolean isJFreeReport()
	{
		if (!StringUtil.isEmpty(query) && !StringUtil.isEmpty(file) && file.endsWith("xml"))
		{
			return true;
		}

		return false;
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

	public String getFile()
	{
		return file;
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

	public void setFile(String file)
	{
		this.file = file;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List getParameters()
	{
		return parameters;
	}

	public List getSubReportParameters()
	{
		ArrayList subReportParameters = new ArrayList();

		if (parameters != null)
		{
			Iterator iterator = parameters.iterator();
			while (iterator.hasNext())
			{
				ReportParameterMap rpMap = (ReportParameterMap) iterator.next();

				if (rpMap.getReportParameter().getType().equals(
						ReportParameter.SUBREPORT_PARAM))
				{
					subReportParameters.add(rpMap);
				}
			}
		}

		return subReportParameters;
	}

	public void setParameters(List parameters)
	{
		this.parameters = parameters;
	}

	public ReportParameterMap getReportParameterMap(Integer parameterId)
	{
		Iterator iterator = parameters.iterator();
		while (iterator.hasNext())
		{
			ReportParameterMap rpMap = (ReportParameterMap) iterator.next();

			if (rpMap.getReportParameter().getId().equals(parameterId))
			{
				return rpMap;
			}
		}

		return null;
	}

	public List getReportParametersByStep(int step)
	{
		List list = new ArrayList();

		Iterator iterator = parameters.iterator();
		while (iterator.hasNext())
		{
			ReportParameterMap rpMap = (ReportParameterMap) iterator.next();

			if (rpMap.getStep() == step)
			{
				list.add(rpMap);
			}
		}

		Collections.sort(list);

		return list;
	}

	public ReportDataSource getDataSource()
	{
		return dataSource;
	}

	public void setDataSource(ReportDataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	public int compareTo(Object object)
	{
		Report report = (Report) object;
		return name.compareTo(report.getName());
	}

	public ReportChart getReportChart()
	{
		return reportChart;
	}

	public void setReportChart(ReportChart reportChart)
	{
		this.reportChart = reportChart;
	}

	public boolean isCsvExportEnabled()
	{
		return csvExportEnabled;
	}

	public void setCsvExportEnabled(boolean csvExportEnabled)
	{
		this.csvExportEnabled = csvExportEnabled;
	}

	public boolean isHtmlExportEnabled()
	{
		return htmlExportEnabled;
	}

	public void setHtmlExportEnabled(boolean htmlExportEnabled)
	{
		this.htmlExportEnabled = htmlExportEnabled;
	}

	public boolean isPdfExportEnabled()
	{
		return pdfExportEnabled;
	}

	public void setPdfExportEnabled(boolean pdfExportEnabled)
	{
		this.pdfExportEnabled = pdfExportEnabled;
	}

	public boolean isXlsExportEnabled()
	{
		return xlsExportEnabled;
	}

	public void setXlsExportEnabled(boolean xlsExportEnabled)
	{
		this.xlsExportEnabled = xlsExportEnabled;
	}

	public String getQuery()
	{
		return query;
	}

	public void setQuery(String query)
	{
		this.query = query;
	}

	public boolean isExcelExportEnabled()
	{
		return excelExportEnabled;
	}

	public void setExcelExportEnabled(Boolean excelExportEnabled)
	{
		if (excelExportEnabled == null) excelExportEnabled = new Boolean(false);
		this.excelExportEnabled = excelExportEnabled.booleanValue();
	}

	public boolean isRtfExportEnabled()
	{
		return rtfExportEnabled;
	}

	public void setRtfExportEnabled(Boolean rtfExportEnabled)
	{
		if (rtfExportEnabled == null) rtfExportEnabled = new Boolean(false);
		this.rtfExportEnabled = rtfExportEnabled.booleanValue();
	}

	public boolean isTextExportEnabled()
	{
		return textExportEnabled;
	}

	public void setTextExportEnabled(Boolean textExportEnabled)
	{
		if (textExportEnabled == null) textExportEnabled = new Boolean(false);
		this.textExportEnabled = textExportEnabled.booleanValue();
	}

	public ReportExportOption getReportExportOption()
	{
		if (reportExportOption == null) reportExportOption = new ReportExportOption();
		return reportExportOption;
	}

	public void setReportExportOption(ReportExportOption reportExportOption)
	{
		this.reportExportOption = reportExportOption;
	}

	public boolean isVirtualizationEnabled()
	{
		return virtualizationEnabled;
	}

	public void setVirtualizationEnabled(Boolean virtualizationEnabled)
	{
		if (virtualizationEnabled == null) virtualizationEnabled = new Boolean(false);
		this.virtualizationEnabled = virtualizationEnabled.booleanValue();
	}
	
	public boolean isHidden()
	{
		return hidden;
	}

	public void setHidden(Boolean hidden)
	{
		if (hidden == null) hidden = new Boolean(false);
		this.hidden = hidden.booleanValue();
	}

	public boolean isDisplayInline()
	{
		return displayInline;
	}

	public void setDisplayInline(boolean displayInline)
	{
		this.displayInline = displayInline;
	}

}
