/**
 * 
 * created on 2007-1-10
 */
package net.ninecube.core.config;

import java.io.Serializable;



/**
 * 
 * @author jxb
 * 
 */
public interface TransactionConf extends Serializable {

	public String getName();

	public String getCname();
	
	public String getFullName();
	
	public String getNamespace();
	
	public String getParameter(String name);

	public String getTransactionClassName();
	
	public ActionConf getAction(String action);

	public PackageConf getPackage();

	public Class getEntityClass();

	public TransactionType getType();

}
