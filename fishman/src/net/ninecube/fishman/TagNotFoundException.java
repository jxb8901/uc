/**
 * 
 * created on 2007-1-25
 */
package net.ninecube.fishman;

/**
 * 表示在实例文档中找不到指定名称的标记的异常
 * 
 * @author jxb
 * 
 */
public class TagNotFoundException extends FishManException {

	public TagNotFoundException(String blockname) {
		super("can't find tag: '" + blockname + "'");
	}
}
