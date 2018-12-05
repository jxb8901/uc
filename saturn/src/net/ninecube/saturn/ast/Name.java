package net.ninecube.saturn.ast;

import java.util.List;

import net.ninecube.lang.BaseException;
import net.ninecube.saturn.Context;
import net.ninecube.saturn.NameResolverProvider;
import net.ninecube.saturn.exception.RuleSyntaxException;

import org.apache.log4j.Logger;

public class Name extends SimpleNode {
	private static Logger log = Logger.getLogger(Name.class);

	private String name;

	private Object value;

	public Name(int id) {
		super(id);
	}

	public Name(RuleParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(RuleParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public void validate(Context context, List<Exception> exceptions) {
		if (!context.getNameResolver().validate(context, name))
			exceptions.add(new RuleSyntaxException(getBeginLine(),
					getBeginColumn(), "不能解析标识符'" + name + "'"));

	}

	public Object execute(Context context) {
		return value = context.getNameResolver().resolve(context, name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		String str = super.toString();
		str += " [" + getName() + "]";
		return str;
	}

}
