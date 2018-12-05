package org.codehaus.jrc.expression.stringfunctions;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jrc.expression.CustomExpression;
import org.codehaus.jrc.expression.Expression;

/**
 * Represents a "substring" function.
 * <p>
 * @author Ben Yu
 * Sep 4, 2006 9:04:28 PM
 */
public class SubstringExpression extends CustomExpression {
  private final Expression str;
  private final Expression start;
  private final Expression len;
  /**
   * Create a SubstringExpression instance.
   * @param str the string expression.
   * @param start the starting index expression.
   * @param len the length expression.
   */
  public SubstringExpression(Expression str, Expression start, Expression len) {
    this.str = str;
    this.start = start;
    this.len = len;
  }
  /**
   * Get the expression for the length.
   */
  public Expression getLength() {
    return len;
  }
  /**
   * Get the expression for the starting index.
   */
  public Expression getStart() {
    return start;
  }
  /**
   * Get the expression for the string.
   */
  public Expression getString() {
    return str;
  }

  public Expression bindArgument(String name, Object value) {
    return withInner(
        str.bindArgument(name, value), 
        start.bindArgument(name, value), 
        len.bindArgument(name, value)
    );
  }
  
  public Expression bindArguments(Map<String, ?> args) {
    return withInner(
        str.bindArguments(args),
        start.bindArguments(args),
        len.bindArguments(args)
    );
  }
  private Expression withInner(Expression newstr, Expression newstart, Expression newlen){
    if(newstr==str && newstart==start && newlen==len) return this;
    else return new SubstringExpression(newstr, newstart, newlen);
  }
  
  public String toString() {
	  // TODO: CustomExpression.toString()应该设为抽象方法，强制子类实现
	  return "substring(" + str.toString() + "," + start.toString() + "," + len.toString() +')';
  }
}
