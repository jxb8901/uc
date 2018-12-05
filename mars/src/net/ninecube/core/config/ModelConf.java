/**
 * 
 * created on 2007-1-10
 */
package net.ninecube.core.config;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author jxb
 * 
 */
public interface ModelConf extends Serializable, FieldSource {
	
	public PackageConf getOwner();

	public Class getEntityClass();
	
	public String getManagerName();

}
