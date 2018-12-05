/**
 * 
 * created on 2006-12-14
 */
package net.ninecube.core.config;

import java.io.Serializable;
import java.util.List;


/**
 * 常见的交易流程：
 * 1、主机交易类：一般交易分为input、confirm、submit等几个步骤（confirm通常省略）
 * 
 * 2、数据库操作类：一般有query、view、add、save、delete几个交易，各交易步骤大致如下：
 * 	A、list交易：通常只有一步query，也可能有两步：input、query
 * 	B、view交易：只有一步view，其input步骤可以认为是query
 * 	C、add交易：一般有两步add、save
 * 	D、save交易：
 * 	E、delete交易：
 * 
 * @author jxb
 * 
 */
public interface WebConfig extends Serializable, FieldSourceProvider {
	
	public String getAppname();
	
	public String getAppcname();

	public List<? extends PackageConf> getPackages();
	
	public ModelConf getModel(String name);
	
	public TransactionConf getTransactionByClass(String clazz);
	
	public TransactionConf getTransaction(String namespace, String name);
	
	public PackageConf getPackageByNamespace(String namespace);

}
