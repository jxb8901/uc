/**
 * 
 * created on 2007-1-19
 */
package net.ninecube.core.config.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ninecube.core.config.ActionsAware;
import net.ninecube.core.config.TransactionUtils;

/**
 * actions为空表示对非默认动作有效
 * actions包含any或＊表示对所有动作有效
 * actions包含default则表示对默认动作有效
 * 其它情况只对名称匹配的交易有效
 * @author jxb
 * 
 */
public class ActionsFilter {

	public static <T extends ActionsAware> List<T> filter(List<T> list, String action) {
		List<T> ret = new ArrayList<T>();
		for (T item : list) {
			if (acceptable(item, action)) ret.add(item);
		}
		return ret;
	}

	public static <K, T extends ActionsAware> Map<K, T> filter(Map<K, T> map, String action) {
		Map<K, T> ret = new HashMap<K, T>();
		for (K key : map.keySet()) {
			T value = map.get(key);
			if (acceptable(value, action)) ret.put(key, value);
		}
		return ret;
	}
	
	private static <T extends ActionsAware> boolean acceptable(T item, String action) {
		if (item.getAction() == null || item.getAction().isEmpty()) {
			if (!TransactionUtils.isDefaultAction(action)) return true;
		}
		else if (item.getAction().contains(action)) return true;
		else if (item.getAction().contains("any")) return true;
		else if (item.getAction().contains("default")) {
			if (TransactionUtils.isDefaultAction(action)) return true;
		}
		return false;
	}
}
