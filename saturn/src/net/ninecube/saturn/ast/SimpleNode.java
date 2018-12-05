package net.ninecube.saturn.ast;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import net.ninecube.saturn.Context;

public class SimpleNode implements Node, net.ninecube.saturn.Node {
	protected Node parent;

	protected Node[] children;

	protected int id;

	protected RuleParser parser;
	
	private int beginLine = -1;
	private int beginColumn = -1;

	public SimpleNode(int i) {
		id = i;
	}

	public SimpleNode(RuleParser p, int i) {
		this(i);
		parser = p;
	}

	public void jjtOpen() {
	}

	public void jjtClose() {
	}

	public void jjtSetParent(Node n) {
		parent = n;
	}

	public Node jjtGetParent() {
		return parent;
	}

	public void jjtAddChild(Node n, int i) {
		if (children == null) {
			children = new Node[i + 1];
		} else if (i >= children.length) {
			Node c[] = new Node[i + 1];
			System.arraycopy(children, 0, c, 0, children.length);
			children = c;
		}
		children[i] = n;
	}

	public Node jjtGetChild(int i) {
		return children[i];
	}

	public int jjtGetNumChildren() {
		return (children == null) ? 0 : children.length;
	}

	/** Accept the visitor. * */
	public Object jjtAccept(RuleParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	/** Accept the visitor. * */
	public Object childrenAccept(RuleParserVisitor visitor, Object data) {
		if (children != null) {
			for (int i = 0; i < children.length; ++i) {
				children[i].jjtAccept(visitor, data);
			}
		}
		return data;
	}

	/*
	 * You can override these two methods in subclasses of SimpleNode to
	 * customize the way the node appears when the tree is dumped. If your
	 * output uses more than one line you should override toString(String),
	 * otherwise overriding toString() is probably all you need to do.
	 */

	public String toString() {
		return RuleParserTreeConstants.jjtNodeName[id];
	}

	public String toString(String prefix) {
		return prefix + toString();
	}

	/*
	 * Override this method if you want to customize how the node dumps out its
	 * children.
	 */

	public void dump(String prefix) {
		dump(System.out, prefix);
	}

	public void dump(PrintStream out, String prefix) {
		out.println(toString(prefix));
		if (children != null) {
			for (int i = 0; i < children.length; ++i) {
				SimpleNode n = (SimpleNode) children[i];
				if (n != null) {
					n.dump(out, prefix + " ");
				}
			}
		}
	}

	public String getDumpStr() {
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		dump(new PrintStream(byteArray), "");
		return byteArray.toString();
	}

	public Object execute(Context context){
		throw new UnsupportedOperationException(
				"unimplemented method invoking ! ");
	}
	
	public void validate(Context context, List<Exception> exceptions){
//		if(children == null || children.length == 0){
//			throw new IllegalArgumentException("叶子结点，必须自行实现validate方法. Node : " + this.toString());
//		}
		for(int i = 0 ; i < jjtGetNumChildren(); i++){
			((SimpleNode)jjtGetChild(i)).validate(context, exceptions);
		}
		
	}

	public int getBeginLine() {
		return beginLine;
	}

	public void setBeginLine(int beginLine) {
		this.beginLine = beginLine;
	}

	public int getBeginColumn() {
		return beginColumn;
	}

	public void setBeginColumn(int beginColumn) {
		this.beginColumn = beginColumn;
	}

}
