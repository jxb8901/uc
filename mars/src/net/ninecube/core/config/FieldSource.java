/**
 * 
 * created on 2007-5-13
 */
package net.ninecube.core.config;

import java.util.List;

/**
 * 
 * @author jxb
 */
public interface FieldSource {
	
	public String getName();

	public List<? extends FieldConf> getFields();

	public FieldConf resolveReference(String name);

	public FieldConf getKey();

}
