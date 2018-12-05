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

import java.util.List;

import net.ninecube.reports.engine.input.ReportEngineInput;
import net.ninecube.reports.engine.output.ReportEngineOutput;
import net.ninecube.reports.objects.Report;
import net.ninecube.reports.providers.DataSourceProvider;
import net.ninecube.reports.providers.DirectoryProvider;
import net.ninecube.reports.providers.PropertiesProvider;
import net.ninecube.reports.providers.ProviderException;

public abstract class ReportEngine
{
	//	export types
	public static final int EXPORT_PDF = 0;
	public static final int EXPORT_XLS = 1;
	public static final int EXPORT_HTML = 2;
	public static final int EXPORT_CSV = 3;
	public static final int EXPORT_IMAGE = 4;
	public static final int EXPORT_RTF = 5;
	public static final int EXPORT_TEXT = 6;
	public static final int EXPORT_EXCEL = 7;
	
	protected DataSourceProvider dataSourceProvider;
	protected DirectoryProvider directoryProvider;
	protected PropertiesProvider propertiesProvider;	
	
	public ReportEngine(DataSourceProvider dataSourceProvider,
			DirectoryProvider directoryProvider, PropertiesProvider propertiesProvider)
	{
		this.dataSourceProvider = dataSourceProvider;
		this.directoryProvider = directoryProvider;
		this.propertiesProvider = propertiesProvider;
	}
	
	public abstract ReportEngineOutput generateReport(ReportEngineInput input)
			throws ProviderException;

	public abstract List buildParameterList(Report report) throws ProviderException;	
}
