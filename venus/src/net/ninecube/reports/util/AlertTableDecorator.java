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

package net.ninecube.reports.util;

import net.ninecube.reports.objects.ReportUserAlert;

import org.displaytag.decorator.TableDecorator;

public class AlertTableDecorator extends TableDecorator
{		
	  public String addRowClass()
      {
		  ReportUserAlert userAlert = (ReportUserAlert) getCurrentRowObject();
		  
		  if (userAlert.isTriggered()) return "triggered";
		  return "nottriggered";         
      }    
	  
	  public Object getStatus()
	  {
		  ReportUserAlert userAlert = (ReportUserAlert) getCurrentRowObject();
		  
		  if (userAlert.isTriggered()) return LocalStrings.getString(LocalStrings.ALERT_PASSED);
		  return LocalStrings.getString(LocalStrings.ALERT_FAILED);
	  }	
}
