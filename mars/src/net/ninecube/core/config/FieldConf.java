/**
 * 
 * created on 2007-1-11
 */
package net.ninecube.core.config;

import java.io.Serializable;
import java.util.List;

import net.ninecube.core.query.QueryCondition;

/**
 * 
 * @author jxb
 * 
 */
public interface FieldConf extends Serializable, QueryCondition, ActionsAware {
	
	//~ 基本属性

	public abstract String getName();

	public abstract String getCname();

	public abstract List<String> getAction();

	public abstract String getValue();

	public abstract String getConverter();
	
	public boolean isKey();
	
	//~ 用于数据校验及页面生成的属性

	public abstract FieldType getType();

	public abstract FieldTagType getTagtype();

	public abstract int getLen();

	public abstract int getMaxlen();

	public abstract String getMaxvalue();

	public abstract int getMinlen();

	public abstract String getMinvalue();

	public abstract boolean isReq();

	public abstract String getJspattern();

	public abstract String getPattern();

	public abstract String getPatternmsg();

	public abstract FieldConf merge(FieldConf conf);

}
