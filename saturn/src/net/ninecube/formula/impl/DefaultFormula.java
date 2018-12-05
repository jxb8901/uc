/**
 * created on 2006-4-6
 */
package net.ninecube.formula.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.ninecube.formula.Context;
import net.ninecube.formula.Formula;
import net.ninecube.formula.FormulaResolver;
import net.ninecube.formula.Result;
import net.ninecube.formula.exception.FormulaCycleReferenceException;
import net.ninecube.formula.exception.FormulaNotFoundException;
import net.ninecube.formula.exception.FormulaSyntaxException;
import net.ninecube.formula.exception.FormulaValidateException;
import net.ninecube.formula.parser.ASTFormula;
import net.ninecube.formula.parser.ASTFunction;
import net.ninecube.formula.parser.FormulaParser;
import net.ninecube.formula.parser.Node;
import net.ninecube.formula.parser.ParseException;
import net.ninecube.formula.parser.TokenMgrError;

import org.apache.log4j.Logger;

/**
 * @author JXB
 */
public class DefaultFormula extends AbstractFormula {
	private static final Logger log = Logger.getLogger(DefaultFormula.class);

	private ASTFormula root;
	private int level;
	private List<String> resolvedReferences;
	
	public DefaultFormula(FormulaResolver resolver,
			Long id, String name, String type, String formula)  
			throws FormulaSyntaxException {
		super(resolver, id, name, type, formula);
	}

	public DefaultFormula(FormulaResolver resolver,
			Long id, String name, String formula)  
			throws FormulaSyntaxException {
		this(resolver, id, name, "", formula);
	}
	
	//~ implements Formula
	
	public int getEvalLevel() {
		return this.level;
	}
	
	public String[] getReferences() {
		return this.resolvedReferences.toArray(new String[this.resolvedReferences.size()]);
	}

	@Override
	protected Result execute(Context context) {
		Result ret =this.root.eval(context);
		context.getResultCollector().addResult(context, ret);
		return ret;
	}

	@Override
	protected void initialize() {
		this.root = parse(super.getFormula());
		this.resolvedReferences = new ArrayList<String>();
		FormulaValidateException ve = new FormulaValidateException();
		
		int maxLevel = -1;
		LinkedList<Node> queue = new LinkedList<Node>();
		queue.addLast(this.root);
		while (!queue.isEmpty()) {
			Node node = queue.removeFirst();
			if (node instanceof ASTFunction) {
				try {
					ASTFunction fn = (ASTFunction) node;
					if (fn.getName().equals(this.getId()) || fn.getName().equals(this.getName()))
						throw new FormulaCycleReferenceException(fn.getName());
					Formula ref = initializeFunctionNode((ASTFunction) node);
					this.resolvedReferences.add(((ASTFunction) node).getName());
					int level = ref.getEvalLevel();
					if (level > maxLevel) maxLevel = level;
				} catch (FormulaCycleReferenceException e) {
					ve.add(e);
				} catch (FormulaNotFoundException e) {
					ve.add(e);
				} catch (FormulaSyntaxException e) {
					ve.add(e);
				}
			}
			else {
				for (int i = 0; i < node.jjtGetNumChildren(); i++) 
					queue.addLast(node.jjtGetChild(i));
			}
		}
		
		//判断是否存在循环引用
		if (checkCycleReference()) ve.add(new FormulaCycleReferenceException(this.getName()));

		if (!ve.isEmpty()) throw ve;
		
		this.level = maxLevel + 1;
		this.setProperties(this.root.getProperties());
	}
	
	private ASTFormula parse(String formula) {
		try {
			ASTFormula s = (ASTFormula)new FormulaParser(formula).parse();
			if (log.isDebugEnabled()) s.dump("DEBUG:");
			return s;
		} catch (ParseException e) {
			throw new FormulaSyntaxException("syntax error, formula["+formula+"]", e);
		} catch (TokenMgrError e) {
			throw new FormulaSyntaxException("Illegal token, formula["+formula+"]", e);
		}
	}
	
	private Formula initializeFunctionNode(ASTFunction node) throws FormulaSyntaxException, FormulaNotFoundException {
		Formula ret = this.formulaResolver.resolve(node.getName());
		if (ret != null) {
			node.setFunction(ret);
		}
		else {
			throw new FormulaNotFoundException(node.getName());
		}
		return ret;
	}
	
	/**
	 * 判断是否存在“间接”或“直接”的循环引用
	 */
	private boolean checkCycleReference() {
		List<String> allRef = new ArrayList<String>();
		allRef.addAll(this.resolvedReferences);
		while (!allRef.isEmpty()) {
			String ref = allRef.remove(0);
			if (ref.equals(this.getName())) return true;
			Formula f = formulaResolver.resolve(ref);
			for (String s : f.getReferences()) allRef.add(s);
		}
		return false;
	}
	
	public String toString() {
		return this.getName() + "[" + this.getFormula() + "]";
	}
}
