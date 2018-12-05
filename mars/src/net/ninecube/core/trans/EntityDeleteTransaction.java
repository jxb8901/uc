/**
 * 
 * created on 2007-1-21
 */
package net.ninecube.core.trans;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author jxb
 * 
 */
public class EntityDeleteTransaction extends EntityReadTransaction {
	private static final Log log = LogFactory.getLog(EntityDeleteTransaction.class);
	
	public String submit() throws Exception {
		log.debug("begin delete entity transaction: " + " ...");
		
		this.getEntityManager().delete(this.loadEntity());
		
		log.debug("... end delete entity transaction");
		return SUCCESS;
	}
}
