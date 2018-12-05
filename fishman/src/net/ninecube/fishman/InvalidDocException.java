/**
 * 
 * created on 2007-1-25
 */
package net.ninecube.fishman;

/**
 * 表示模板文档或实例文档中找不到FishMan的特定标记的异常
 * 
 * @author jxb
 * 
 */
public class InvalidDocException extends FishManException {

	private boolean template;
	
	public InvalidDocException(boolean template) {
		super((template? "Template":"Instance")+" doc is invalid!");
		this.template = template;
	}
	
	public boolean isTemplate() {
		return template;
	}
}
