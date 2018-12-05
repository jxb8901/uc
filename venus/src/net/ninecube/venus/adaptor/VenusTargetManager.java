/**
 * 
 * created on 2007-3-19
 */
package net.ninecube.venus.adaptor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import net.ninecube.core.domain.Entity;
import net.ninecube.core.domain.EntityManager;
import net.ninecube.formula.Target;
import net.ninecube.formula.impl.TargetManagerSupport;
import net.ninecube.formula.impl.TargetManagerSupport.TargetEntity;
import net.ninecube.formula.impl.TargetManagerSupport.TypedTargetManager;

/**
 * 
 * @author jxb
 * 
 */
public class VenusTargetManager implements TypedTargetManager {
	private String type;
	private EntityManager<? extends Entity> entityManager;
	/**
	 * 指定TargetEntity中的字段名称与Entity中的字段名称的对应关系
	 */
	private Properties mappingFieldNames;

	public List<TargetEntity> getTargets() {
		List<TargetEntity> ret = new ArrayList<TargetEntity>();
		for (Entity e : this.entityManager.getAll()) {
			ret.add(getTargetEntity(e));
		}
		return ret;
	}

	public String getType() {
		return this.type;
	}
	
	protected TargetEntity getTargetEntity(Entity entity) {
		TargetEntity ret = new TargetEntity();
		for (Map.Entry en : this.mappingFieldNames.entrySet()) {
			try {
				Object value = PropertyUtils.getProperty(entity, (String)en.getValue());
				BeanUtils.copyProperty(ret, (String) en.getKey(), value);
			} catch (Exception e1) {
				throw new RuntimeException(e1);
			}
		}
		return ret;
	}

	public void setEntityManager(EntityManager<? extends Entity> entityManager) {
		this.entityManager = entityManager;
	}

	public void setMappingFieldNames(Properties properties) {
		this.mappingFieldNames = properties;
	}

	public void setType(String type) {
		this.type = type;
	}

}
