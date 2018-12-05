/**
 * 
 * created on 2007-6-26
 */
package net.ninecube.venus.query;

/**
 * 
 * @author jxb
 */
public interface TargetEntity {
	public static final String TARGETTYPE_ALL = net.ninecube.formula.Target.TARGETTYPE_ALL;

	public Long getId();
	
	public String getName();
	
	public String getType();
}
