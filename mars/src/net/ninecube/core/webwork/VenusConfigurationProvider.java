/**
 * 
 * created on 2006-12-12
 */
package net.ninecube.core.webwork;

import java.io.InputStream;
import java.util.List;

import net.ninecube.core.config.PackageConf;
import net.ninecube.core.config.TransactionConf;
import net.ninecube.core.config.WebConfigManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.util.FileManager;
import com.opensymphony.util.TextUtils;
import com.opensymphony.xwork.config.Configuration;
import com.opensymphony.xwork.config.ConfigurationException;
import com.opensymphony.xwork.config.ConfigurationManager;
import com.opensymphony.xwork.config.ConfigurationProvider;
import com.opensymphony.xwork.config.ConfigurationUtil;
import com.opensymphony.xwork.config.entities.PackageConfig;
import com.opensymphony.xwork.config.providers.XmlConfigurationProvider;

/**
 * 
 * @author jxb
 * @TODO: 重写parametersInterceptor，以便仅设置交易需要的字段
 */
public class VenusConfigurationProvider implements ConfigurationProvider {
	private static final Log LOG = LogFactory.getLog(VenusConfigurationProvider.class);

	private WebConfigManager configmanager;
	
	private Configuration configuration;

	private String webworkConfigFileName = "venus-xwork.xml";
	private String defaultWebworkConfigFileName = "venus-default.xml";
	private String configFileName = "venus.xml";
	private String defaultPackageParent = "venus-default";
	
	public VenusConfigurationProvider(WebConfigManager configManager) { 
		this.configmanager = configManager;
	}
	
	private VenusConfigurationProvider(WebConfigManager configManager, String configFileName) {
		this.configmanager = configManager;
		this.configFileName = configFileName;
	}
	
	public void init() {
		LOG.debug("初始化自定义配置提供者");
		ConfigurationManager.addConfigurationProvider(new XmlConfigurationProvider(defaultWebworkConfigFileName));
		try {
			InputStream is = FileManager.loadFile(webworkConfigFileName, this.getClass()); 
			if (is != null) {
				is.close();
				LOG.debug("load xwork config file: '" + webworkConfigFileName + "'");
				ConfigurationManager.addConfigurationProvider(new XmlConfigurationProvider(webworkConfigFileName));
			}
		} catch (Exception e) {LOG.error(e.getMessage(), e); }
		ConfigurationManager.addConfigurationProvider(this);
	}

	public void destroy() {
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (!(o instanceof VenusConfigurationProvider)) {
			return false;
		}

		final VenusConfigurationProvider configurationProvider = (VenusConfigurationProvider) o;

		if ((configFileName != null) ? (!configFileName.equals(configurationProvider.configFileName)) : (configurationProvider.configFileName != null)) {
			return false;
		}

		return true;
	}

	public int hashCode() {
		return ((configFileName != null) ? configFileName.hashCode() : 0);
	}

	public boolean needsReload() {
		return false; // TODO：reload configuration 
	}

	public void init(Configuration configuration) {
		this.configuration = configuration;
		LOG.debug("转换交易配置");
		try {
			loadConfiguration();
		} catch (ConfigurationException e) {
			throw e;
		} catch (Exception e) {
			LOG.fatal("Could not load Venus configuration file, failing", e);
			throw new ConfigurationException("Error loading configuration file " + configFileName, e);
		}
	}

	private void loadConfiguration() {
		LOG.debug("配置："+configmanager.getConfiguration().getPackages().size());
		for (PackageConf p : configmanager.getConfiguration().getPackages()) {
			addPackage(p, null);
		}
	}

	protected PackageConfig addPackage(PackageConf p, PackageConfig parent) throws ConfigurationException {
		PackageConfig newPackage = buildPackageContext(p, parent);

		if (LOG.isDebugEnabled()) {
			LOG.debug("Loaded " + newPackage);
		}
		
		for (PackageConf p1 : p.getChildren()) {
			addPackage(p1, newPackage).addParent(newPackage);
		}
		
		for (TransactionConf t1 : p.getTransactions()) {
			new TransactionAdaptor().adaptor(t1, newPackage);
		}

		configuration.addPackageConfig(newPackage.getName(), newPackage);
		
		return newPackage;
	}

	@SuppressWarnings("unchecked")
	protected PackageConfig buildPackageContext(PackageConf p, PackageConfig parent) {
		String name = TextUtils.noNull(p.getName());
		String namespace = TextUtils.noNull(p.getNamespace());

		PackageConfig cfg = null;

		List parents = ConfigurationUtil.buildParentsFromString(configuration, defaultPackageParent);
		if (parent != null)
			parents.add(parent);

		if (parents.size() <= 0) {
			cfg = new PackageConfig(name, namespace, false, null);
		} else {
			cfg = new PackageConfig(name, namespace, false, null, parents);
		}

		return cfg;
	}
}
