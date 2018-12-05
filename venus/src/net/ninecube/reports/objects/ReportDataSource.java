/*
 * Copyright (C) 2002 Erik Swenson - eswenson@opensourcesoft.net
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */

package net.ninecube.reports.objects;

import java.io.Serializable;

import net.ninecube.core.domain.Entity;

import org.apache.commons.dbcp.BasicDataSource;

public class ReportDataSource implements Entity, Serializable
{	
	private static final long serialVersionUID = 7990031344563814988L;
	
	private Integer id;
	private String name;
	private boolean jndi;
	private BasicDataSource dataSource;
	
	public ReportDataSource() {
		this.dataSource = new BasicDataSource();
	}
	
	public BasicDataSource getDataSource() {
		return this.dataSource;
	}

	public void setDataSource(BasicDataSource source) {
		this.dataSource = source;
	}
	 
	public boolean isJndi()
	{
		return jndi;
	}

	public void setJndi(boolean jndi)
	{
		this.jndi = jndi;
	}	
	
	public void setId(Integer id)
	{
		this.id = id;
	}	

	public String toString()
	{
		return name;
	} 

	public Integer getId()
	{		
		return id;		
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDriverClassName() {
		return dataSource.getDriverClassName();
	}

	public int getMaxActive() {
		return dataSource.getMaxActive();
	}

	public int getMaxIdle() {
		return dataSource.getMaxIdle();
	}

	public long getMaxWait() {
		return dataSource.getMaxWait();
	}

	public String getPassword() {
		return dataSource.getPassword();
	}

	public String getUrl() {
		return dataSource.getUrl();
	}

	public String getUsername() {
		return dataSource.getUsername();
	}

	public void setDriverClassName(String arg0) {
		dataSource.setDriverClassName(arg0);
	}

	public void setMaxActive(int arg0) {
		dataSource.setMaxActive(arg0);
	}

	public void setMaxIdle(int arg0) {
		dataSource.setMaxIdle(arg0);
	}

	public void setMaxWait(long arg0) {
		dataSource.setMaxWait(arg0);
	}

	public void setPassword(String arg0) {
		dataSource.setPassword(arg0);
	}

	public void setUrl(String arg0) {
		dataSource.setUrl(arg0);
	}

	public void setUsername(String arg0) {
		dataSource.setUsername(arg0);
	}

	public String getValidationQuery() {
		return dataSource.getValidationQuery();
	}

	public void setValidationQuery(String arg0) {
		dataSource.setValidationQuery(arg0);
	}

}
