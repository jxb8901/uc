package net.ninecube.saturn.function;

import java.util.List;
import java.util.Map;

import net.ninecube.saturn.Context;

public interface Function {
	public Object execute(Context context, List indexArgs, Map namedArgs);
}
