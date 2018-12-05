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

package net.ninecube.reports.providers.impl;

import java.util.ArrayList;
import java.util.List;

import net.ninecube.reports.objects.ReportUser;
import net.ninecube.reports.providers.GroupProvider;
import net.ninecube.reports.providers.ProviderException;
import net.ninecube.reports.providers.UserProvider;
import net.ninecube.reports.providers.persistence.UserPersistenceProvider;
import net.ninecube.venus.domain.User;
import net.ninecube.venus.domain.UserManager;

import org.apache.log4j.Logger;

public class UserProviderImpl implements UserProvider
{
	protected static Logger log =
		Logger.getLogger(UserProviderImpl.class.getName());
	
	private UserManager userManager;
	private GroupProvider groupProvider;

	public UserProviderImpl() throws ProviderException
	{
		log.info("UserProviderImpl created");
	}

	public ReportUser getUser(String name) throws ProviderException
	{
		return getReportUser(this.userManager.getByName(name));
	}

	public ReportUser getUser(Integer id) throws ProviderException
	{
		return getReportUser(this.userManager.getById(id));
	}

	public List getUsers() throws ProviderException
	{
		List<ReportUser> ret = new ArrayList<ReportUser>();
		List<User> users = this.userManager.getAll();
		for (User user : users) ret.add(getReportUser(user));
		return ret;
	}
	
	private ReportUser getReportUser(User user) throws ProviderException {
		ReportUser ret = new ReportUser();
		ret.setId(new Integer(user.getIdno().intValue()));
		ret.setName(user.getName());
		ret.setPassword(user.getPassword());
		ret.setGroups(this.groupProvider.getReportGroups());
		return ret;
	}

	public void setGroupProvider(GroupProvider groupProvider) {
		this.groupProvider = groupProvider;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
}
