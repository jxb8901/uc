/**
 * 
 * created on 2007-3-19
 */
package net.ninecube.formula.impl;

import java.util.ArrayList;
import java.util.List;

import net.ninecube.formula.Target;
import net.ninecube.formula.TargetManager;
import net.ninecube.formula.exception.UnknownTargetTypeException;
import net.ninecube.saturn.RuleEngine;

/**
 * 
 * @author jxb
 * 
 */
public class TargetManagerSupport implements TargetManager {
	private RuleEngine ruleEngine;
	private List<? extends TypedTargetManager> managers = new ArrayList<TypedTargetManager>();
	
	public Target createTarget(Long idname, String type, String rule) {
		return createTarget(idname, "" + idname, type, rule);
	}
	
	public Target createTarget(Long id, String name, String type, String rule) {
		return new DefaultTarget(id, name, type, rule, ruleEngine.getFilter(rule));
	}

	public void setRuleEngine(RuleEngine ruleEngine) {
		this.ruleEngine = ruleEngine;
	}
	
	public Target getTargetByName(String name, String targetType){
		List<TargetEntity> ts = getManager(targetType).getTargets();
		for(TargetEntity te : ts){
			if(te.name.equals(name)) return targetEntityToTarget(te, targetType);
		}
		return null;
	}

	public List<Target> getTargetsByType(String type) {
		List<Target> ret = new ArrayList<Target>();
		List<TargetEntity> list;
		if (Target.TARGETTYPE_ALL.equals(type)) {
			list = new ArrayList<TargetEntity>();
			for (TypedTargetManager manager : managers) {
				list.addAll(manager.getTargets());
				for (TargetEntity t : list)
					ret.add(targetEntityToTarget(t, manager.getType()));
			}
		}
		else {
			list = getManager(type).getTargets();
			for (TargetEntity t : list)
				ret.add(targetEntityToTarget(t, type));
		}
		return ret;
	}
	
	public TypedTargetManager getManager(String type) {
		for (TypedTargetManager manager : managers) {
			if (manager.getType().equals(type)) return manager;
		}
		throw new UnknownTargetTypeException(type);
	}
	
	public void setManagers(List<? extends TypedTargetManager> managers) {
		this.managers = managers;
	}
	
	protected Target targetEntityToTarget(TargetEntity t, String type) {
		return createTarget(t.getId(), t.getName(), type, t.getRule());
	}

	public static class ListTypedTargetManager implements TypedTargetManager {
		private String type;
		private List<TargetEntity> list;
		public ListTypedTargetManager(String type, List<TargetEntity> list) {
			this.type = type;
			this.list = list; 
		}
		public String getType() { return type; }
		public List<TargetEntity> getTargets() { return list; }
	}

	public static interface TypedTargetManager {
		public String getType();
		public List<TargetEntity> getTargets();
	}
	
	public static class TargetEntity {
		private Long id;
		private String name, rule;
		public Long getId() { return id; }
		public String getName() { return name; }
		public String getRule() { return rule; }
		public void setId(Long id) { this.id = id; }
		public void setName(String name) { this.name = name; }
		public void setRule(String rule) { this.rule = rule; }
	}
}
