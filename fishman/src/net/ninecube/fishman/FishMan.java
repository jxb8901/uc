/**
 * 
 * created on 2007-1-16
 */
package net.ninecube.fishman;

import java.io.IOException;
import java.io.Reader;

import net.ninecube.fishman.parser.InstanceParserImpl;
import net.ninecube.fishman.parser.TemplateParserImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 
 * @author jxb
 * 
 */
public class FishMan extends Reader {
	private static final Log log = LogFactory.getLog(FishMan.class);
	private IParser.ITemplateParser template;
	private IParser.IInstanceParser instance;
	private Reader in;
	private Block backup;
	private boolean inTemplate;
	private boolean inDynamic;
	
	public static Reader process(Reader template, Reader instance) {
		return new FishMan(template, instance);
	}
	
	private FishMan(Reader template, Reader instance) {
		this.template = new TemplateParserImpl(template);
		this.instance = new InstanceParserImpl(instance);
		this.inTemplate = true;
		this.inDynamic = false;
		this.in = this.next();
	}

	private Reader next() {
		if (this.inDynamic ) {
			if (inTemplate) {
				return readInTemplate();
			}
			else {
				return readInInstance();
			}
		}
		return readInTemplate();
	}
	
	private Reader readInTemplate() {
		Block block = template.next();
		if (this.inDynamic) {
			if (block == null) { // 到了文件尾，未找到动态块的结束标记
				throw new TagNotEndException(true);
			}
			if (block.isStatic()) { // 正常情况，正在读取模板的动态块
				return block.getInput();
			}
			else if (block.isHead()){ // 前一个块未结束，找到了第二个开始块标记
				throw new TagNotEndException(true, block.getName());
			}
			else { // 模板的动态内容块结束，丢弃之使用实例中的尾部代替
				this.inTemplate = true;
				this.inDynamic = false;
				if (this.backup != null)
					return readBackup();
				else
					return block.getInput();
			}
		}
		else {
			if (block == null) return null; // 文件结束
			if (block.isStatic()) {
				return block.getInput();
			}
			else if (block.isHead()){
				this.inDynamic = true;
				return readInTemplate(block);
			}
			else { // 在模板中遇到没有开始标记的结束标记，忽略！
				log.warn("模板中的块没有匹配的开始标志");
				return block.getInput();
			}
		}
	}
	
	/**
	 * 读到模板中的动态内容块，此时需要将实例文档中的相应名称的块替换模板内容
	 * 如果实例文档中相应名称的块内容为空，则使用模板的内容块作为默认内容
	 */
	private Reader readInTemplate(Block block) {
		Block head = instance.nextDynamicBegin(block.getName());
		if (head == null) { // 在实例文档中未找到相应名称的区块，使用模板中的区块代替
			this.inTemplate = true;
			this.inDynamic = true;
			return block.getInput();
		}
		else {
			Block content = instance.next();
			if (content.isStatic()) { // 实例文档中动态内容非空
				this.inTemplate = false;
				this.inDynamic = true;
				backup = content; // 备份预读出来的内容
				if (null == template.nextDynamicEnd()) // 跳过模板中本区块的内容
					throw new TagNotEndException(true, block.getName());
			}
			else if (content.isHead()) { // 两个头部
				throw new IllegalStateException("实例文档中不可能发生两个头部被读出的情况！");
			}
			else { // 实例文档中动态内容为空
				inTemplate = true; // 下一次应该在模板文件中读取内容
				this.inDynamic = true;
				backup = content; // 备份尾部
			}
			return head.getInput();
		}
	}
	
	private Reader readInInstance() {
		if (!this.inDynamic) throw new IllegalStateException("只会读取实例文档的动态内容块");
		if (backup != null) return readBackup();
		Block block = instance.next();
		if (block == null) {
			throw new IllegalStateException("不可能出现的情况，InstanceParser中增加了虚拟块");
		}
		if (block.isStatic()) {
			return block.getInput();
		}
		else if (block.isHead()) { // 
			throw new IllegalStateException("实例文档中不可能发生两个头部被读出的情况:"+block.getName());
		}
		else {
			this.inTemplate = true; // 下一次应该在模板文件中读取内容
			this.inDynamic = false;
			return block.getInput();
		}
	}
	
	private Reader readBackup() {
		Reader ret = backup.getInput();
		backup = null;
		return ret;
	}

	private int readNext(char[] cbuf, int off, int len) throws IOException {
		this.in.close();
		this.in = this.next();
		if (in == null) return -1;
		return read(cbuf, off, len);
	}

	@Override
	public void close() throws IOException {
		if (in != null) in.close();
		if (!this.template.isValid()) // 全部读完了，还没有读到有效标记，就认为模板无效
			throw new InvalidDocException(true);
	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		int ret = -1;
		if (in != null) ret = in.read(cbuf, off, len);
		if (ret == -1) {
			ret = readNext(cbuf, off, len);
		}
		return ret;
	}
	
	public static void main(String[] a) {
		
	}
}
