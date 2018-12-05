package net.ninecube.saturn.ast;

import java.util.List;

import net.ninecube.saturn.Context;
import net.ninecube.saturn.NameResolver;
import net.ninecube.saturn.NameResolverProvider;
import net.ninecube.saturn.database.Column;
import net.ninecube.saturn.database.DataSet;
import net.ninecube.saturn.database.DataSetUtil;

import org.apache.log4j.Logger;
import org.codehaus.jrc.expression.Expression;

public class AggStatement extends SimpleNode {
	private SimpleNode aggNode;

	private SimpleNode aggRefNameNode;

	private SimpleNode stmtNode;

	private Logger log = Logger.getLogger(this.getClass());

	public AggStatement(int id) {
		super(id);
	}

	public AggStatement(RuleParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(RuleParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
	
	public void validate(Context context, List<Exception> exceptions){
		DataSet orgds = context.getDataSet();
		try {
			DataSet ds = orgds.clone();
			context.setDataSet(ds);
			Expression exp = null;
			String refName = null;
			if (aggNode != null) {
				aggNode.validate(context, exceptions);
				aggRefNameNode.validate(context, exceptions);
				exp = ((Column) aggNode.execute(context)).getSqlCode();
				refName = (String) aggRefNameNode.execute(context);
			}
			ds = DataSetUtil.aggregate(ds, exp, refName);
			context.setDataSet(ds);
			stmtNode.validate(context, exceptions);

		} finally {
			context.setDataSet(orgds);
		}
		
	}

	@Override
	public Object execute(Context context) {
		DataSet orgds = context.getDataSet();
		try {
			DataSet ds = orgds.clone();
			context.setDataSet(ds);
			Expression exp = null;
			String refName = null;
			if (aggNode != null) {
				exp = ((Column) aggNode.execute(context)).getSqlCode();
				refName = (String) aggRefNameNode.execute(context);
			}
			ds = DataSetUtil.aggregate(ds, exp, refName);
			context.setDataSet(ds);
			stmtNode.execute(context);

		} finally {
			context.setDataSet(orgds);
		}
		return null;
	}

	public SimpleNode getAggNode() {
		return aggNode;
	}

	public void setAggNode(SimpleNode aggNode) {
		log.debug("aggNode : " + aggNode);
		this.aggNode = aggNode;
	}

	public SimpleNode getStmtNode() {
		return stmtNode;
	}

	public void setStmtNode(SimpleNode stmtNode) {
		log.debug("stmt : " + stmtNode);
		this.stmtNode = stmtNode;
	}

	public SimpleNode getAggRefNameNode() {
		return aggRefNameNode;
	}

	public void setAggRefNameNode(SimpleNode aggRefNameNode) {
		log.debug("ref name : " + aggRefNameNode);
		this.aggRefNameNode = aggRefNameNode;
	}
}
