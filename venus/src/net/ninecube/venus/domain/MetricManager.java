package net.ninecube.venus.domain;

import java.util.List;

import net.ninecube.core.domain.EntityManager;
import net.ninecube.formula.Formula;
import net.ninecube.formula.FormulaResolver;
import net.ninecube.lang.Frequence;

/**
 * 
 * @author jxb
 *
 */
public interface MetricManager extends EntityManager<Metric>, FormulaResolver {

	public Metric getByName(String name);
	
	/**
	 * 根据公式类型和频度取所有公式
	 * @param type 公式类型，如果为NULL或空字符串则表示取所有类型
	 * @param frequence 公式频度
	 * @return 返回满足条件的公式
	 */
	public List<Metric> getByTypeAndFreqence(String type, Frequence frequence);
}
