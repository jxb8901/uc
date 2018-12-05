/**
 * 
 * created on 2007-4-9
 */
package net.ninecube.saturn;

/**
 * 
 * @author jxb
 * 
 */
public interface ContextListener {
	
	public String getName();
	
	public void onInit(Context context);
	
	public void onFinished(Context context);

}
