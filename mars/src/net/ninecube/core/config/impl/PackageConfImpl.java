/**
 * 
 * created on 2006-12-14
 */
package net.ninecube.core.config.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.ninecube.core.config.PackageConf;
import net.ninecube.util.StringUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 
 * @author jxb
 * 
 */
public class PackageConfImpl implements PackageConf, PostProcess {
	private static final Log log = LogFactory.getLog(PackageConfImpl.class);
	
	private String name;
	private String cname;
	private String controllerBaseDir;
	private String viewBaseDir;
	private ModelConfImpl model = new ModelConfImpl(this);
	private WebConfigImpl config;

	private PackageConfImpl parent;
	private List<PackageConfImpl> children = new ArrayList<PackageConfImpl>();
	
	private List<TransactionConfImpl> transactions = new ArrayList<TransactionConfImpl>();
	
	public PackageConfImpl(WebConfigImpl config, String name, String cname, String controllerBaseDir, String viewBaseDir) {
		this.parent = null;
		this.name = name;
		this.cname = cname;
		this.controllerBaseDir = controllerBaseDir == null ? config.getControllerBaseDir() : controllerBaseDir;
		this.viewBaseDir = viewBaseDir == null ? config.getViewBaseDir() : viewBaseDir;
		this.config = config;
		config.addPackage(this);
	}
	
	public PackageConfImpl(PackageConfImpl parent, String name, String cname) {
		this.parent = parent;
		this.name = name;
		this.cname = cname;
		this.config = parent.getConfig();
		this.parent.addChild(this);
	}
	
	public void postProcessAfterLoadConfig() {
		for (PackageConfImpl p : children) {
			p.postProcessAfterLoadConfig();
		}
		for (TransactionConfImpl t : transactions) {
			t.postProcessAfterLoadConfig();
		}
	}
	
	public TransactionConfImpl getTransactionByClass(String className) {
		if (this.parent == null) { // 只在顶级包中按类查找交易，因为只有顶级包才可以设置包前缀
			if (className.startsWith(this.controllerBaseDir)) { // 检查包前缀是否一致
				int index = className.lastIndexOf(".");
				String transName = (index >= 0) ? className.substring(index + 1) : className;
				String namespace = className.substring(this.controllerBaseDir.length(), index);
				PackageConfImpl p = this.getPackageByNamespace(StringUtil.split(namespace, "\\."));
				if (p == null) return null;
				return p.getTransaction(StringUtils.uncapitalize(transName));
			}
		}
		return null;
	}
	
	public TransactionConfImpl getTransaction(String name) {		
		for (TransactionConfImpl t : transactions) {
			if (t.getName().equals(name)) return t;
		}
		return null;
	}
	
	public TransactionConfImpl getTransaction(String namespace, String name) {
		PackageConfImpl p = this.getPackageByNamespace(namespace);
		if (p != null) return p.getTransaction(name);
		return null;
	}
	
	public PackageConfImpl getSubPackage(String name) {
		for (PackageConfImpl p : children) {
			if (p.name.equals(name)) return p;
		}
		return null;
	}
	
	public PackageConfImpl getPackageByNamespace(String namespace) {
		return getPackageByNamespace(StringUtil.split(namespace, "/"));
	}
	
	private PackageConfImpl getPackageByNamespace(List<String> namespace) {
		if (namespace.isEmpty()) return null;
		if (!namespace.get(0).equals(this.name)) return null;
		String packagename = namespace.remove(0);
		if (namespace.isEmpty()) {
			return this;
		}
		else {
			PackageConfImpl ret = this.getSubPackage(namespace.get(0));
			if (ret != null) return ret.getPackageByNamespace(namespace);
			return null;
		}
	}
	
	public String getJavaPackageName() {
		if (parent != null) 
			return parent.getJavaPackageName() + "." + this.name;
		return this.controllerBaseDir + "." + this.name;
	}
	
	public String getTransactionClassName() {
		if (parent != null) 
			return parent.getJavaPackageName() + "." + StringUtils.capitalize(this.name);
		return this.controllerBaseDir + "." + StringUtils.capitalize(this.name);
	}
	
	public String getViewPackageName() {
		if (parent != null) 
			return parent.getViewPackageName() + "/" + this.name;
		return this.viewBaseDir + "/" + this.name;
	}
	
	public String getViewBaseDir() {
		if (parent != null) 
			return parent.getViewBaseDir();
		return this.viewBaseDir;
	}

	public String getNamespace() {
		if (parent != null) 
			return parent.getNamespace() + "/" + this.name;
		return "/" + this.name;
	}
	
	public String getFullName() {
		return this.getJavaPackageName();
	}
	
	//~ getter and setter

	public List<PackageConfImpl> getChildren() {
		return Collections.unmodifiableList(children);
	}
	
	public void addChild(PackageConfImpl p) {
		this.children.add(p);
	}

	public String getCname() {
		return cname;
	}

	public String getName() {
		if (this.parent != null) 
			return this.parent.name + "." + this.name;
		return this.name;
	}

	public PackageConf getParent() {
		return parent;
	}

	public List<TransactionConfImpl> getTransactions() {
		return Collections.unmodifiableList(transactions);
	}
	
	public void addTransaction(TransactionConfImpl t) {
		this.transactions.add(t);
	}

	public WebConfigImpl getConfig() {
		return config;
	}

	public ModelConfImpl getModel() {
		return model;
	}

	public void setModel(ModelConfImpl model) {
		this.model = model;
	}
	
	public String toString() {
		if (log.isDebugEnabled())
			return ToStringBuilder.reflectionToString(this);
		else
			return new ToStringBuilder(this).
				append("name", name).
				toString();
	}
}
