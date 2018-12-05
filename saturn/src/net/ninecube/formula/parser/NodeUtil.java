/**
 * created on 2006-4-14
 */
package net.ninecube.formula.parser;

import net.ninecube.formula.Arguments;

/**
 * @author JXB
 */
public class NodeUtil {

	public static Arguments getArguments(Node node, boolean isProperty) {
		Arguments args = new Arguments();
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			Node ch = node.jjtGetChild(i);
			if (!(ch instanceof ASTArgument)) continue;
			ASTArgument arg = (ASTArgument) ch;
			if (arg.getName() == null || arg.getName().length() == 0) {
				if (isProperty) {
					args.setArgument(arg.getValue(), arg.getValue());
				}
				else {
					args.setArgument(i, arg.getValue());
				}
			}
			else {
				args.setArgument(arg.getName(), arg.getValue());
			}
		}
		return args;
	}
}
