/**
 * 
 * created on 2007-5-19
 */
package net.ninecube.reports.providers.impl;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import net.ninecube.reports.objects.ReportDataSource;
import net.ninecube.reports.providers.DataSourceProvider;
import net.ninecube.reports.providers.ProviderException;

/**
 * 
 * @author jxb
 */
public class SpringDataSourceProviderImpl implements DataSourceProvider {
	private static final Log log = LogFactory.getLog(SpringDataSourceProviderImpl.class);
	private DataSource dataSource;
	private ReportDataSource reportDataSource;
	
	public void init() {
		this.reportDataSource = new ReportDataSource();
		this.reportDataSource.setId(new Integer(99));
		this.reportDataSource.setName("venus data source");
		this.reportDataSource.setJndi(false);
		if (this.dataSource instanceof BasicDataSource) {
			this.reportDataSource.setDataSource((BasicDataSource)this.dataSource);
		}
		else if (this.dataSource instanceof DriverManagerDataSource) {
			try {
				BeanUtils.copyProperties(this.reportDataSource, this.dataSource);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	public Connection getConnection(ReportDataSource id) throws ProviderException {
		try {
			return this.dataSource.getConnection();
		} catch (SQLException e) {
			throw new ProviderException(e);
		}
	}

	public ReportDataSource getDataSource(Integer id) throws ProviderException {
		return this.reportDataSource;
	}

	public ReportDataSource getDataSource(String name) throws ProviderException {
		return this.reportDataSource;
	}

	public List getDataSources() throws ProviderException {
		return Arrays.asList(this.reportDataSource);
	}

	public boolean isValidDataSource(Integer id) {
		return true;
	}
	
	public void deleteDataSource(ReportDataSource dataSource) throws ProviderException {
		throw new UnsupportedOperationException();
	}

	public ReportDataSource insertDataSource(ReportDataSource dataSource) throws ProviderException {
		throw new UnsupportedOperationException();
	}

	public void testDataSource(ReportDataSource dataSource) throws ProviderException {
		throw new UnsupportedOperationException();
	}

	public void updateDataSource(ReportDataSource dataSource) throws ProviderException {
		throw new UnsupportedOperationException();
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
