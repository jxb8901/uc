/**
 * 
 * created on 2007-1-10
 */
package net.ninecube.core.config.impl;

import net.ninecube.core.config.ModelConf;

/**
 * 与venus.dtd中的model元素对应的类
 * @author jxb
 * 
 */
public class ModelConfImpl extends FieldSourceSupport implements ModelConf {
	private PackageConfImpl owner;
	private Class entityClass;
	
	public ModelConfImpl(PackageConfImpl owner) {
		this.owner = owner;
	}
	
	public String getManagerName() {
		return this.getName() + "Manager";
	}
	
	//~ getter and setter
	
	public PackageConfImpl getOwner() {
		return this.owner;
	}
	
	public Class getEntityClass() {
		return this.entityClass;
	}
	
	public void setEntityClass(Class entityClass) {
		this.entityClass = entityClass;
	}
}
