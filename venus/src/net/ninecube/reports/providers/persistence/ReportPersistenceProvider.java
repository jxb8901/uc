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

package net.ninecube.reports.providers.persistence;

import java.util.Iterator;
import java.util.List;

import net.ninecube.reports.objects.Report;
import net.ninecube.reports.objects.ReportGroup;
import net.ninecube.reports.providers.HibernateProvider;
import net.ninecube.reports.providers.ProviderException;
import net.ninecube.reports.util.LocalStrings;

import org.apache.log4j.Logger;

import org.hibernate.*;
import org.hibernate.criterion.Expression;

public class ReportPersistenceProvider 
{
	protected static Logger log =
		Logger.getLogger(ReportPersistenceProvider.class.getName());

	public ReportPersistenceProvider() throws ProviderException
	{
		super();

		log.info("ReportPersistenceProvider Created.");
	}

	public Report getReport(Integer id) throws ProviderException
	{
		return (Report) HibernateProvider.load(Report.class, id);
	}

	 public Report getReport(String name) throws ProviderException
	{
		Session session = null;
		
		try
		{
			session = HibernateProvider.openSession();
			
			Criteria criteria = session.createCriteria(Report.class);
			criteria.add(Expression.eq("name", name));
			
			return (Report) criteria.uniqueResult();
		}
		catch (HibernateException he)
		{
			throw new ProviderException(he);
		}
		finally
		{
			HibernateProvider.closeSession(session);
		}
	}
	 
	public List getReports() throws ProviderException
	{
		String fromClause =
			"from net.ninecube.reports.objects.Report report order by report.name ";
		
		return HibernateProvider.query(fromClause);
	}

	public Report insertReport(Report report) throws ProviderException
	{
		return (Report) HibernateProvider.save(report);
	}

	public void updateReport(Report report) throws ProviderException
	{
		HibernateProvider.update(report);
	}

	public void deleteReport(Report report) throws ProviderException
	{
		Session session = HibernateProvider.openSession();
		Transaction tx = null;

		try
		{
			tx = session.beginTransaction();
			
			//delete report			
			session.delete(report);
			
			//delete report log entries for report
			session
					.createQuery(
							"DELETE net.ninecube.reports.objects.ReportLog reportLog where reportLog.report.id = ? ")
					.setInteger(0, report.getId().intValue()).executeUpdate();
								
			
			//remove report from groups
			Iterator iterator =  session
				.createQuery(
						"from net.ninecube.reports.objects.ReportGroup reportGroup").iterate();
						
			 while(iterator.hasNext())
			 {
			 	ReportGroup reportGroup = (ReportGroup) iterator.next();
			 	
			 	List reports = reportGroup.getReports();			 	
			 	if (reports.contains(report))
			 	{
			 		reports.remove(report);
			 	}
			 }	
			
			tx.commit();
		}
		catch (HibernateException he)
		{
			HibernateProvider.rollbackTransaction(tx);

			if (he.getCause().getMessage().toUpperCase().indexOf("CONSTRAINT") > 0)
			{
				throw new ProviderException(LocalStrings
						.getString(LocalStrings.ERROR_REPORT_DELETION), he);
			}

			throw new ProviderException(he);
		}
		finally
		{
			HibernateProvider.closeSession(session);
		}
	}
}
