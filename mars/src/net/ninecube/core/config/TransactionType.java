/**
 * 
 * created on 2007-1-21
 */
package net.ninecube.core.config;

import net.ninecube.core.trans.EntityCreateTransaction;
import net.ninecube.core.trans.EntityDeleteTransaction;
import net.ninecube.core.trans.EntityDownloadTransaction;
import net.ninecube.core.trans.EntityQueryTransaction;
import net.ninecube.core.trans.EntityReadTransaction;
import net.ninecube.core.trans.EntityUpdateTransaction;
import net.ninecube.core.trans.ReflectTransaction;
import net.ninecube.core.trans.Transaction;

/**
 * 
 * @author jxb
 * 
 */
public enum TransactionType {
	query(EntityQueryTransaction.class), 
	create(EntityCreateTransaction.class), 
	read(EntityReadTransaction.class), 
	update(EntityUpdateTransaction.class), 
	delete(EntityDeleteTransaction.class), 
	download(EntityDownloadTransaction.class), 
	reflect(ReflectTransaction.class), 
	unknown(null),
	xwork(null);
	
	private Class<? extends Transaction> transactionClass;
	
	private TransactionType(Class<? extends Transaction> transactionClass) {
		this.transactionClass = transactionClass;
	}
	
	public Class<? extends Transaction> getTransactionClass() {
		return this.transactionClass;
	}
	
	public static TransactionType resolve(String type) {
		try {
			return TransactionType.valueOf(type);
		} catch (RuntimeException e) {
			return unknown;
		}
	}
}
