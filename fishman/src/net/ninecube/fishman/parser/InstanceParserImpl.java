/**
 * 
 * created on 2007-1-16
 */
package net.ninecube.fishman.parser;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.ninecube.fishman.Block;
import net.ninecube.fishman.InvalidDocException;


/**
 * 
 * @author jxb
 * 
 */
public class InstanceParserImpl implements net.ninecube.fishman.IParser.IInstanceParser {
	private Map<String, List<BlockImpl>> blocks = new HashMap<String, List<BlockImpl>>();
	private Iterator<BlockImpl> current;
	
	public InstanceParserImpl(Reader in) {
		readDynamicInParser(new Parser(in));
		if (this.blocks.isEmpty())
			throw new InvalidDocException(false);
	}
	
	public BlockImpl next() throws ParseException {
		if (current != null || current.hasNext()) {
			return current.next();
		}
		return null;
	}
	
	public BlockImpl nextDynamicBegin(String name) {
		List<BlockImpl> list = blocks.get(name);
		if (list != null) {
			current = list.iterator();
			return current.next();
		}
		return null;
	}
	
	private void readDynamicInParser(Parser parser) {
		boolean dynamic = false;
		String name = null;
		List<BlockImpl> list = null;
		BlockImpl b = null;
		while ((b = parser.next()) != null) {
			if (b.isStatic()) {
				if (dynamic) list.add(b);
			}
			else if (b.isHead()) {
				if (list != null) { // 遇到了未结束的动态块
					list.add(BlockImpl.newDummyEndBlock(name)); // 构造一个虚拟结束块
					blocks.put(name, list);
				}
				name = b.getName();
				dynamic = true;
				list = new ArrayList<BlockImpl>();
				list.add(b);
			}
			else if (b.isTail()) {
				if (list == null) { // 遇到了未开始的结束块
					// 忽略
				}
				else {
					list.add(b);
					blocks.put(name, list);
					name = null;
					dynamic = false;
					list = null;
				}
			}
			else {
				throw new IllegalStateException("不可能出现这种状态");
			}
		}
		if (list != null) { // 遇到了未结束的动态块
			list.add(BlockImpl.newDummyEndBlock(name)); // 构造一个虚拟结束块
			blocks.put(name, list);
		}
	}
}
