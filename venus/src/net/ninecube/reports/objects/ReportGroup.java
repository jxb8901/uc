/*
 * Copyright (C) 2002 Erik Swenson - eswenson@opensourcesoft.net
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.ninecube.core.domain.Entity;

public class ReportGroup implements Entity, Comparable, Serializable
{	
	private static final long serialVersionUID = 3591220643715469145L;
	
	private Integer id;
	private String name;
	private String description;

	private List reports;

	public ReportGroup()
	{
	}

	public List getReports()
	{
		return reports;
	}
	
	// does not include hidden reports
	public List getReportsForDisplay()
	{
		ArrayList list = new ArrayList();
		
		Iterator iterator = reports.iterator();
		while(iterator.hasNext())
		{
			Report report = (Report) iterator.next();
			if (!report.isHidden()) list.add(report);
		}
		
		return list;
	}

	public void setReports(List reports)
	{
		this.reports = reports;
	}

	public String toString()
	{
		return name;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public int compareTo(Object object)
	{
		ReportGroup reportGroup = (ReportGroup) object;
		return name.compareTo(reportGroup.getName());
	}

	public boolean isValidReport(Report report)
	{
		if (reports != null && reports.size() > 0)
		{
			Iterator iterator = reports.iterator();
			while (iterator.hasNext())
			{
				Report r = (Report) iterator.next();
				if (r.getId().equals(report.getId()))
					return true;
			}
		}

		return false;
	}

}
