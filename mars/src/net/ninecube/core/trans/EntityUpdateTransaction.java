/**
 * 
 * created on 2007-1-21
 */
package net.ninecube.core.trans;

import java.util.List;

import net.ninecube.core.config.FieldConf;
import net.ninecube.core.domain.Entity;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author jxb
 * 
 */
public class EntityUpdateTransaction extends EntitySaveTransaction {
	private static final Log log = LogFactory.getLog(EntityUpdateTransaction.class);
	
	protected Entity getEntity() throws Exception {
		log.debug("prepare to load entity from database...");
		Entity dbentity = this.loadEntity(this.createEntity());
		Entity userentity = this.getModel();

		log.debug("prepare to copy properties...");
		List<? extends FieldConf> list = this.getConfig().getInputDataFields();
		for (FieldConf f : list) {
			copyProperty(dbentity, f.getName(), userentity);
		}
		
		log.debug("end copy entity...");
		return dbentity;
	}
	
	protected static void copyProperty(Object dest, String name, Object src) throws Exception {
		BeanUtils.copyProperty(dest, name,  PropertyUtils.getProperty(src, name));
	}
}
