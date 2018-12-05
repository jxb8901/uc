/**
 * 
 * created on 2007-1-10
 */
package net.ninecube.core.config;

import java.io.Serializable;
import java.util.List;


/**
 * 
 * @author jxb
 * 
 */
public interface PackageConf extends Serializable {

	public String getName();
	
	public String getFullName();

	public String getCname();

	public String getNamespace();

	public String getViewPackageName();
	
	public String getViewBaseDir();

	public String getJavaPackageName();

	public String getTransactionClassName();

	public WebConfig getConfig();

	public ModelConf getModel();

	public PackageConf getParent();

	public List<? extends PackageConf> getChildren();

	public PackageConf getSubPackage(String name);

	public PackageConf getPackageByNamespace(String namespace);

	public List<? extends TransactionConf> getTransactions();

	public TransactionConf getTransactionByClass(String className);

	public TransactionConf getTransaction(String name);

	public TransactionConf getTransaction(String namespace, String name);

}
