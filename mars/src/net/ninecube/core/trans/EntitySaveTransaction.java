/**
 * 
 * created on 2007-1-21
 */
package net.ninecube.core.trans;

import java.util.List;

import net.ninecube.core.config.FieldConf;
import net.ninecube.core.config.FieldType;
import net.ninecube.core.domain.Entity;
import net.ninecube.core.domain.EntityManager;
import net.ninecube.core.domain.EntityManagerFactory;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author jxb
 * 
 */
public abstract class EntitySaveTransaction extends EntityReadTransaction  {
	private static final Log log = LogFactory.getLog(EntitySaveTransaction.class);
	
	public String submit() throws Exception {
		log.debug("begin save entity transaction ...");
		
		this.getEntityManager().save(loadRelativeEntity(getEntity()));
		
		log.debug("... end save entity transaction");
		return SUCCESS;
	}
	
	/**
	 * 预先加载关联的实体对象，以便domain层可以透明地访问关联实体
	 * 之所以需要作预先加载，是因为webwork是通过new Entity().setId()这样的方式
	 * 生成关联实体的，也就是该关联实体本身只有id属性是有效的，但其它属性均未
	 * 从数据库中load。如果domain层不需要访问该关联属性，那么这里的预先加载
	 * 将是多余的，因为这样的非hibernate加载的实体也能被hibernate很好地保存。
	 */
	protected Entity loadRelativeEntity(Entity userentity) throws Exception {
		log.debug("prepare load relative entity ...");
		List<? extends FieldConf> list = this.getConfig().getInputDataFields();
		for (FieldConf f : list) {
			if (f.getType() == FieldType.model) {
				Object model = PropertyUtils.getProperty(userentity, f.getName()); 
				if (model instanceof Entity) {
					BeanUtils.copyProperty(userentity, f.getName(), getEntity((Entity) model));
				}
			}
		}
		log.debug("end  load relative entity.");
		return userentity;
	}
	
	@SuppressWarnings("unchecked")
	protected static <T extends Entity> T getEntity(T entity) {
		Class<T> entityClass = (Class<T>) entity.getClass();
		log.debug("prepare load entity: '" + entity.getClass() + "'");
		EntityManager<T> em = EntityManagerFactory.get().getEntityManager(entityClass);
		return em.getById(em.getId(entity));
	}
	
	protected abstract Entity getEntity() throws Exception;
}
