/**
 * 
 * created on 2007-2-7
 */
package org.codehaus.jrc.contribute.util;

import org.codehaus.jrc.relation.OrderElement;

/**
 * 
 * @author jxb
 * 
 */
public class CloneVisitor implements Cloneable {
	protected CloneVisitorUtil cvu;

	//~ visitor
	
	protected OrderElement vo(OrderElement o) {
		return new OrderElement(cvu.ve(o.getExpression()), o.isAscending());
	}
	
	protected OrderElement[] vo(OrderElement[] o) {
		OrderElement[] ret = new OrderElement[o.length];
		for (int i = 0; i < o.length; i++) ret[i] = vo(o[i]);
		return ret;
	}
	
	protected Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

}
