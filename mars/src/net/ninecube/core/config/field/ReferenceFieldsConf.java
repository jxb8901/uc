/**
 * 
 * created on 2007-1-10
 */
package net.ninecube.core.config.field;

import java.util.List;

import net.ninecube.core.config.FieldConf;
import net.ninecube.core.config.FieldSource;


/**
 * 
 * @author jxb
 */
public interface ReferenceFieldsConf {
	
	public List<FieldConf> resolve(FieldSource m);
}
