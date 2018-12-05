/**
 * 
 * created on 2007-1-16
 */
package net.ninecube.fishman;

/**
 * 模板文件或实例文件解析器
 * 文件格式如下：<pre>
 * [静态内容块]
 * [动态内容块头部]
 *    [静态内容块]
 * [动态内容块尾部]
 * [静态内容块]
 * ......
 * [静态内容块]
 * [动态内容块头部]
 *    [静态内容块]
 * [动态内容块尾部]
 * [静态内容块]
 * ......</pre>
 * 其中：
 * 静态内容块为任意字符串
 * 动态内容块头部格式为：<pre>
 * &lt;!-- BeginTemplate/BeginInstance name="value" ... --&gt;</pre>
 * 动态内容块尾部格式为：<pre>
 * &lt;!-- EndTemplate/EndInstance --&gt;</pre>
 * 
 * @author jxb
 * 
 */
public interface IParser {

	/**
	 * 读下一块内容，可能是静态内容，也可能是动态内容的头部或尾部
	 * 如果读到文件尾，则返回null
	 */
	public Block next();
	
	public interface IInstanceParser extends IParser {
		/**
		 * 读取指定名称的动态块内容头部，从当前状态直到头部的内容块被抛弃
		 * 返回该头部内容块，没有该名称的内容块时返回null
		 */
		public Block nextDynamicBegin(String name);
	}
	
	public interface ITemplateParser extends IParser {
		/**
		 * 读取动态块尾部，从当前状态直到该尾部的内容块被抛弃
		 * 返回该内容块，找不到尾部块时返回null
		 */
		public Block nextDynamicEnd();
		
		/**
		 * 检查模板是否有效，主要看是否有模板标记，如果
		 * 没有模板标记返回假，否则返回真
		 */
		public boolean isValid();
	}
}
