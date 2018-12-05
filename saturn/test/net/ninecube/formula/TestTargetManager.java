/**
 * created on 2006-4-12
 */
package net.ninecube.formula;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ninecube.formula.impl.DefaultTarget;
import net.ninecube.saturn.database.DataSet;

/**
 * @author JXB
 */
public class TestTargetManager implements TargetManager {
	private Map<String, Target> targets = new HashMap<String, Target>(); 
	
	public DefaultTarget getTarget(Long id, String type) {
		return new DefaultTarget(id, "" + id, type, "", null);
	}
	
	public Target getTargetByName(String name, String type){
		return targets.get(name);
	}

	public List<Target> getTargetsByType(String targetType) {
		return new ArrayList<Target>();
	}
	
	public void addTarget(Target t){
		targets.put(t.getName(), t);
	}

	public static Target getTarget(Long id, String type, String rule, DataSet ds) {
		DefaultTarget ret = new TestTargetManager().getTarget(id, type);
		ret.setRule(rule);
		ret.setFilter(ds);
		return ret;
	}
	
	
}
