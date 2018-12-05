/**
 * 
 * created on 2007-1-21
 */
package net.ninecube.core.trans;

import net.ninecube.core.domain.Entity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author jxb
 * 
 */
public class EntityCreateTransaction extends EntitySaveTransaction {
	private static final Log log = LogFactory.getLog(EntityCreateTransaction.class);
	
	protected Entity getEntity() throws Exception {
		return super.getModel();
	}
}
