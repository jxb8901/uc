/**
 * 
 * created on 2006-11-30
 */
package net.ninecube.saturn.function.operation;

import net.ninecube.saturn.Context;

public interface Operation {

	public Object execute(Context context);

	public void setAttribute(String name, Object val);

	public Object getAttribute(String name);

}
