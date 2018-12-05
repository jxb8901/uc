package net.ninecube.saturn;

import java.util.List;
import java.util.Map;

public interface Rule {
	public String getRule();
	public String getMapping();
	public List<String> getVarNames();
	public Map<String, ?> getDefaultVarValues();
}
