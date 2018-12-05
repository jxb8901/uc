/**
 * 
 * created on 2007-6-23
 */
package org.codehaus.jrc.contribute.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.codehaus.jrc.expression.CustomExpression;
import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.expression.ExpressionVisitor;
import org.codehaus.jrc.predicate.Predicate;
import org.codehaus.jrc.predicate.PredicateVisitor;
import org.codehaus.jrc.relation.Relation;
import org.codehaus.jrc.relation.RelationVisitor;

/**
 * 
 * @author jxb
 */
public class ReflectTreeWalker {
	private JrcVisitor visitor;

	private Object proxy;

	public ReflectTreeWalker(JrcVisitor visitor) {
		this.visitor = visitor;
		this.proxy = getProxy(new Handler());
	}
	
	public void visit(Object obj) {
		if (obj instanceof Relation) {
			this.visit((Relation)obj);
		} else if (obj instanceof Predicate) {
			this.visit((Predicate)obj);
		} else if (obj instanceof Expression) {
			this.visit((Expression)obj);
		} else if (obj.getClass().isArray()) {
			for (int i = 0; i < Array.getLength(obj); i++) {
				visit(Array.get(obj, i));
			}
		}
	}

	public void visit(Relation r) {
		this.visitObject(r);
	}

	public void visit(Predicate p) {
		this.visitObject(p);
	}

	public void visit(Expression e) {
		this.visitObject(e);
	}

	// ~ private method

	protected RelationVisitor getRelationVisitor() {
		return (RelationVisitor) proxy;
	}

	protected PredicateVisitor getPredicateVisitor() {
		return (PredicateVisitor) proxy;
	}

	protected ExpressionVisitor getExpressionVisitor() {
		return (ExpressionVisitor) proxy;
	}

	protected Object getProxy(InvocationHandler handler) {
		return Proxy.newProxyInstance(RelationVisitor.class.getClassLoader(), new Class[] { RelationVisitor.class, PredicateVisitor.class, ExpressionVisitor.class }, handler);
	}

	private class Handler implements InvocationHandler {
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			if ("visitCustom".equals(method.getName())) {
				visitCustom((CustomExpression) args[0]);
			}
			else {
				for (Object arg : args) {
					visitObject(arg);
				}
			}
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	protected void visitObject(Object obj) {
		if (obj == null) return;
		if (obj instanceof Relation) {
			if (this.visitor.visit((Relation) obj)) {
				((Relation) obj).accept(getRelationVisitor());
			}
		} else if (obj instanceof Predicate) {
			if (this.visitor.visit((Predicate) obj)) {
				((Predicate) obj).accept(getPredicateVisitor());
			}
		} else if (obj instanceof Expression) {
			if (this.visitor.visit((Expression) obj)) {
				((Expression) obj).accept(getExpressionVisitor());
			}
		} else if (obj.getClass().isArray()) {
			for (int i = 0; i < Array.getLength(obj); i++) {
				visitObject(Array.get(obj, i));
			}
		} else {
			this.visitor.visit((Object)obj);
		}
	}

	protected void visitCustom(CustomExpression expr) throws Exception {
		PropertyDescriptor[] pds = java.beans.Introspector.getBeanInfo(expr.getClass()).getPropertyDescriptors();
		for (PropertyDescriptor pd : pds) {
			Object obj = pd.getReadMethod().invoke(expr, new Object[0]);
			visitObject(obj);
		}
	}
}
