/**
 * 
 * created on 2007-1-21
 */
package net.ninecube.core.trans;

import java.io.Serializable;

import net.ninecube.core.context.ServiceLocator;
import net.ninecube.core.domain.Entity;
import net.ninecube.core.domain.EntityManager;
import net.ninecube.util.StringUtil;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork.ModelDriven;
import com.opensymphony.xwork.Preparable;

/**
 * 
 * @author jxb
 * 
 */
public class EntityReadTransaction extends Transaction 
			implements ModelDriven, Preparable {
	private static final Log log = LogFactory.getLog(EntityReadTransaction.class);
	private Entity entity;
	
	@Override
	public String execute() throws Exception {
		log.debug("begin view entity transaction ...");
		
		if (!StringUtil.isEmpty(this.getParameter(this.getEntityKeyName())))
			loadEntity();
		
		log.debug("... end view entity transaction");
		return SUCCESS;
	}
	
	public void prepare() throws Exception {
		log.debug("prepare create entity.");
		this.entity = createEntity();
	}

	public Entity getModel() {
		return this.entity;
	}
	
	protected Entity createEntity() {
		return this.getEntityManager().create();
	}
	
	protected Entity loadEntity() throws Exception {
		return loadEntity(this.entity);
	}
	
	protected Entity loadEntity(Entity t) throws Exception {
		return getEntityManager().load(t, getSerializableId());
	}
	
	protected Serializable getSerializableId() throws Exception {
		return (Serializable) PropertyUtils.getProperty(this.entity, 
				this.getEntityKeyName());
	}
}
