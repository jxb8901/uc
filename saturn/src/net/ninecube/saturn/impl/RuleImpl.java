package net.ninecube.saturn.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.ninecube.saturn.Rule;

public class RuleImpl implements Rule {
	
	private String rule;
	private String mapping;

	public RuleImpl(String rule, String mapping){
		this.rule = rule;
		this.mapping = mapping;
	}

	public Map<String, ?> getDefaultVarValues() {
		Map<String, String>paras = new HashMap<String, String>();
		if(mapping == null || "".equals(mapping.trim())){
			return paras;
		}
		String pattern="<([^:>]*):([^:>]*)>";
		Matcher matcher = Pattern.compile(pattern).matcher(mapping);
		while(matcher.find()){
			paras.put(matcher.group(1).trim(), matcher.group(2).trim());
		}
		return paras;
	}

	public String getMapping() {
		return mapping;
	}

	public String getRule() {
		return rule;
	}

	public List<String> getVarNames() {
		// TODO Auto-generated method stub
		return null;
	}

}
