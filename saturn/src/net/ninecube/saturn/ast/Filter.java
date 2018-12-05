package net.ninecube.saturn.ast;

import net.ninecube.saturn.Context;

import org.codehaus.jrc.predicate.Predicate;

public class Filter extends SimpleNode {
  public Filter(int id) {
    super(id);
  }

  public Filter(RuleParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(RuleParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
  
 @Override 
  public Object execute(Context context){
	 SimpleNode node = (SimpleNode)jjtGetChild(0);
	 Object rst =  node.execute(context);
	 if(rst instanceof Predicate){
		 context.setDataSet(context.getDataSet().where((Predicate)rst));
	 }
	 
	 return rst ;
 }
}
