/**
 * 
 * created on 2007-3-13
 */
package net.ninecube.formula;

import net.ninecube.saturn.database.DataSet;

/**
 * 计算目标对象，如客户群、营销计划、等
 * @author jxb
 * 
 */
public interface Target {
	
	public static final String TARGETTYPE_ALL = "*";

	public Long getId();
	
	public String getName();
	
	public String getType();
	
	public String getRule();
	
}
