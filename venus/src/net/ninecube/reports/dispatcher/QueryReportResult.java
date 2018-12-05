/*
 * Copyright (C) 2005 Erik Swenson - eswenson@opensourcesoft.net
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

package net.ninecube.reports.dispatcher;

import net.ninecube.reports.actions.QueryReportResultAction;
import net.ninecube.reports.objects.Report;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;

import com.opensymphony.webwork.config.Configuration;
import com.opensymphony.webwork.dispatcher.VelocityResult;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.util.OgnlValueStack;

public class QueryReportResult extends VelocityResult
{
	protected static Logger log = Logger.getLogger(QueryReportResult.class);

	private VelocityEngine velocityEngine;

	public QueryReportResult()
	{
		velocityEngine = new VelocityEngine();
		velocityEngine.setProperty(VelocityEngine.RESOURCE_LOADER, "file, wwclass");
		velocityEngine.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH,
				Configuration.getString("webwork.multipart.saveDir").trim());
		velocityEngine.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_CACHE, "true");
		velocityEngine.setProperty("file.resource.loader.modificationCheckInterval", "2");
		velocityEngine.setProperty("wwclass.resource.loader.modificationCheckInterval", "2");
		velocityEngine.setProperty("wwclass.resource.loader.description",
				"Velocity Classpath Resource Loader");
		velocityEngine.setProperty("wwclass.resource.loader.class",
				"com.opensymphony.webwork.views.velocity.WebWorkResourceLoader");
		velocityEngine.setProperty("wwclass.resource.loader.modificationCheckInterval", "2");
		velocityEngine.setProperty("wwclass.resource.loader.cache", "true");
		velocityEngine.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM_CLASS,
				"org.apache.velocity.runtime.log.NullLogSystem");

		String directives = "com.opensymphony.webwork.views.velocity.ParamDirective" + ","
				+ "com.opensymphony.webwork.views.velocity.TagDirective" + ","
				+ "com.opensymphony.webwork.views.velocity.BodyTagDirective";
		
		velocityEngine.setProperty("userdirective", directives);

		try
		{
			velocityEngine.init();
		}
		catch (Exception e)
		{
			log.error(e);
		}
	}

	protected Template getTemplate(OgnlValueStack stack, VelocityEngine velocity,
			ActionInvocation invocation, String location, String encoding) throws Exception
	{
		Object action = invocation.getAction();
		if (action instanceof QueryReportResultAction)
		{
			QueryReportResultAction queryReportAction = (QueryReportResultAction) action;

			Report report = queryReportAction.getReport();
			if (report.getFile() != null && report.getFile().endsWith(".vm"))
			{
				return velocityEngine.getTemplate(report.getFile());
			}
		}

		return super.getTemplate(stack, velocity, invocation, location, encoding);
	}
}
