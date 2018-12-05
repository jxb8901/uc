/**
 * 
 * created on 2007-1-25
 */
package net.ninecube.fishman;

/**
 * 表示模板文档或实例文档中的标记未结束的异常
 * 
 * @author jxb
 * 
 */
public class TagNotEndException extends FishManException {
	private boolean template;

	public TagNotEndException(boolean template) {
		super(getMsg(template, null));
		this.template = template;
	}

	public TagNotEndException(boolean template, String blockname) {
		super(getMsg(template, blockname));
		this.template = template;
	}
	
	public boolean isTemplate() {
		return this.template;
	}
	
	private static String getMsg(boolean template, String blockname) {
		String msg = "In" + (template? "template doc, " : "instance doc, ") +"before tag is not end.";
		if (blockname != null) msg += " block name:'" + blockname + "'";
		return msg;
	}
}
