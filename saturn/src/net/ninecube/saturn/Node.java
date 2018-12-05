package net.ninecube.saturn;

import java.util.List;


public interface Node {
	public void validate(Context context , List<Exception> exceptions);
	public Object execute(Context context);
	
}
