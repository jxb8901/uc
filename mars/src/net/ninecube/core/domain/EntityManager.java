package net.ninecube.core.domain;

import java.io.Serializable;


/**
 * 
 * @author jxb
 *
 */
public interface EntityManager<T extends Entity> extends EntityQueryManager<T> {

	public T create();

	/**
	 * 如果找不到对象则返回null，不抛异常
	 */
	public T getById(Serializable id);

	/**
	 * 找不到对象对抛异常
	 */
	public T load(Serializable id);
	public T load(T entity, Serializable id);
	
	public void save(T cg);
	
	public void delete(Serializable id);
	
	public void delete(T cg);
	
	/**
	 * 利用反射或hibernate元数据取得实体对象的ID
	 */
	public Serializable getId(T entity);
}
