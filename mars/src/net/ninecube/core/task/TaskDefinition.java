/**
 * 
 * created on 2007-4-29
 */
package net.ninecube.core.task;

import net.ninecube.core.config.FieldConf;


/**
 * 
 * @author jxb
 */
public interface TaskDefinition {
	
	public String getId();

	public String getName();
	
	public String getGroup();
	
	public String getDescription();
	
	public Class<? extends Runnable> getTaskClass();
		
	public FieldConf[] getParameters();

	public boolean isConcurrent();
	
}
