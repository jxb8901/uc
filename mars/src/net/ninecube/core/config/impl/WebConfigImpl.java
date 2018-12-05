/**
 * 
 * created on 2006-12-15
 */
package net.ninecube.core.config.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.ninecube.core.config.PackageConf;
import net.ninecube.core.config.WebConfig;

/**
 * 
 * @author jxb
 * 
 */
public class WebConfigImpl implements WebConfig, PostProcess {
	private static final String DEFAULT_CONTROLLER_BASEDIR = "net.ninecube";
	private static final String DEFAULT_VIEW_BASEDIR = "/views";
	private String appname;
	private String appcname;
	private String controllerBaseDir;
	private String viewBaseDir;
	private List<PackageConfImpl> packages = new ArrayList<PackageConfImpl>();
	
	public WebConfigImpl(String appname, String appcname) {
		this.appname = appname;
		this.appcname = appcname;
		this.controllerBaseDir = DEFAULT_CONTROLLER_BASEDIR + "." + this.appname + ".trans";
		this.viewBaseDir = DEFAULT_VIEW_BASEDIR;
	}
	
	public void postProcessAfterLoadConfig() {
		for (PackageConfImpl p : packages) {
			p.postProcessAfterLoadConfig();
		}
	}
	
	public TransactionConfImpl getTransactionByClass(String clazz) {
		for (PackageConfImpl p : packages) {
			TransactionConfImpl ret = p.getTransactionByClass(clazz);
			if (ret != null) return ret;
		}
		return null;
	}
	
	public TransactionConfImpl getTransaction(String namespace, String name) {
		for (PackageConfImpl p : packages) {
			TransactionConfImpl ret = p.getTransaction(namespace, name);
			if (ret != null) return ret;
		}
		return null;
	}
	
	public PackageConf getPackageByNamespace(String namespace) {
		for (PackageConf p : packages) {
			PackageConf ret = p.getPackageByNamespace(namespace);
			if (ret != null) return ret;
		}
		return null;
	}
	
	public ModelConfImpl getModel(String name) {
		ModelConfImpl model = null;
		for (PackageConfImpl p : packages) {
			model = getModel(p, name);
			if (model != null) return model;
		}
		return model;
	}
	
	private ModelConfImpl getModel(PackageConfImpl p, String name) {
		ModelConfImpl model = p.getModel();
		if (model != null && name.equals(model.getName())) return model;
		for (PackageConfImpl s : p.getChildren()) {
			model = getModel(s, name);
			if (model != null) return model;
		}
		return null;
	}
	
	//~ getter and setter
	public void addPackage(PackageConfImpl conf) {
		this.packages.add(conf);
	}

	public List<PackageConfImpl> getPackages() {
		return Collections.unmodifiableList(packages);
	}

	public String getAppcname() {
		return appcname;
	}

	public String getAppname() {
		return appname;
	}

	public String getControllerBaseDir() {
		return controllerBaseDir;
	}

	public void setControllerBaseDir(String controllerBaseDir) {
		this.controllerBaseDir = controllerBaseDir;
	}

	public String getViewBaseDir() {
		return viewBaseDir;
	}

	public void setViewBaseDir(String viewBaseDir) {
		this.viewBaseDir = viewBaseDir;
	}

	public void setPackages(List<PackageConfImpl> packages) {
		this.packages = packages;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{Config: ");
		sb.append("appname=").append(this.appname).append(", ");
		sb.append("appcname=").append(this.appcname).append(", ");
		sb.append("controllerBaseDir=").append(this.controllerBaseDir).append(", ");
		sb.append("viewBaseDir=").append(this.viewBaseDir);
		sb.append("}");
		return sb.toString();
	}
}
