/*
 * Copyright (C) 2005 Erik Swenson - eswenson@opensourcesoft.net
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

import net.ninecube.reports.objects.ORProperty;
import net.ninecube.reports.providers.PropertiesProvider;
import net.ninecube.reports.providers.ProviderException;
import net.ninecube.reports.providers.persistence.PropertiesPersistenceProvider;

import org.apache.log4j.Logger;

public class PropertiesProviderImpl implements PropertiesProvider
{
	protected static Logger log = Logger.getLogger(PropertiesProviderImpl.class
			.getName());

	private PropertiesPersistenceProvider propertiesPersistenceProvider;	

	public PropertiesProviderImpl() throws ProviderException
	{
		propertiesPersistenceProvider = new PropertiesPersistenceProvider();

		log.info("PropertiesProviderImpl created");
	}

	public ORProperty getProperty(String key) throws ProviderException
	{
		return null;
		//return propertiesPersistenceProvider.getProperty(key);		
	}

	public void setProperty(String key, String value) throws ProviderException
	{
		ORProperty property = getProperty(key);
		
		if (property == null)
		{
			property = new ORProperty();
			property.setKey(key);
			property.setValue(value);
			
			//propertiesPersistenceProvider.insertProperty(property);
		}
		else
		{
			property.setValue(value);
			
			//propertiesPersistenceProvider.updateProperty(property);
		}
	}
}
