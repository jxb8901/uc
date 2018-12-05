
package net.ninecube.saturn.ast;

import net.ninecube.lang.Frequence;
import net.ninecube.saturn.Context;

public class RegionProperty extends SimpleNode {
  public RegionProperty(int id) {
    super(id);
  }

  public RegionProperty(RuleParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(RuleParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
  
  @Override
  public Object execute(Context context){
	  SimpleNode node = (SimpleNode)jjtGetChild(0);
	  Frequence freq =Frequence.get((String) node.execute(context));
	  return freq;
  }
  
  
}
