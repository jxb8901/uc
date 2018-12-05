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

package net.ninecube.reports.services;

import net.ninecube.reports.services.info.ReportInfo;
import net.ninecube.reports.services.info.ServiceCallInfo;
import net.ninecube.reports.services.info.UserInfo;

/**
 * Central Interface for external access into OpenReports user information
 * 
 * @author Erik Swenson
 * @see UserServiceImpl
 */

public interface UserService
{	
	/**
	 * authenticate user
	 */	
	public boolean authenticate(String userName, String password);
	
	/**
	 * Get a list of reports for a given username
	 */	
	public ReportInfo[] getReports(String userName);
	
	/**
	 * Get user information for a given userName
	 */	
	public UserInfo getUserInfo(String userName);
	
	/**
	 * Update user information
	 */	
	public ServiceCallInfo updateUserInfo(UserInfo userInfo);
}
