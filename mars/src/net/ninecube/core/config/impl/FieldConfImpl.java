/**
 * 
 * created on 2006-12-14
 */
package net.ninecube.core.config.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.ninecube.core.config.ConfigException;
import net.ninecube.core.config.FieldConf;
import net.ninecube.core.config.FieldTagType;
import net.ninecube.core.config.FieldType;
import net.ninecube.core.query.Operator;
import net.ninecube.core.query.QueryCondition;
import net.ninecube.util.DynamicBean;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author jxb
 * 
 */
public class FieldConfImpl extends DynamicBean implements FieldConf, QueryCondition, Cloneable {
	private static final Log log = LogFactory.getLog(FieldConfImpl.class);

	private String name;
	private String cname;
	private List<String> action = Collections.emptyList();
	private FieldType type = FieldType.string;
	private FieldTagType tagtype = FieldTagType.auto;
	private boolean key = false;
	private boolean req = false;
	private int len = 0;
	private int maxlen = 0;
	private int minlen = 0;
	private String maxvalue;
	private String minvalue;
	private String converter;
	private String pattern;
	private String jspattern;
	private String patternmsg;
	private String value;
	private Operator operator = Operator.eq;
	private String entityName;
	// TODO: 去掉，使用反射机制拦截set方法调用，自动生成需要合并的字段列表
	private List<String> mergeableFieldNames = new java.util.ArrayList<String>();
	
	public FieldConfImpl clone() {
		try {
			FieldConfImpl ret = (FieldConfImpl) super.clone();
			// 全部是primitive类型的字段，故不需为deep-clone做其它的手工复制
			return ret;
		} catch (CloneNotSupportedException e) {
			throw new ConfigException(e);
		}
	}
	
	/**
	 * 将两个字段状态合并后返回一个新的字段对象
	 * 其中字段other的状态优先
	 */
	public FieldConfImpl merge(FieldConf other) {
		if (!(other instanceof FieldConfImpl))
			throw new IllegalArgumentException("other must be type '" + FieldConfImpl.class.getName() + "'");
		FieldConfImpl ret = this.clone();
		for (Field f : this.getClass().getDeclaredFields()) {
			try {
				Object value = f.get(other);
				if (((FieldConfImpl)other).mergeableFieldNames.contains(f.getName())) {
					f.setAccessible(true);
					f.set(ret, value);
				}
			} catch (Exception e) { } 
		}
		return ret;
	}
	
	protected void removeMergableFieldName(String name) {
		if (this.mergeableFieldNames.contains(name)) {
			this.mergeableFieldNames.remove(name);
		}
	}

	//~ getter and setter

	/**
	 * @hibernate.property
	 */
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * @hibernate.property
	 */
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	/**
	 * @hibernate.property
	 */
	public String getConverter() {
		return converter;
	}
	public void setConverter(String converter) {
		this.converter = converter;
	}
	/**
	 * @hibernate.property
	 */
	public int getLen() {
		return len;
	}
	public void setLen(int len) {
		this.len = len;
	}
	/**
	 * @hibernate.property
	 */
	public int getMaxlen() {
		return maxlen;
	}
	public void setMaxlen(int maxlen) {
		this.maxlen = maxlen;
	}
	/**
	 * @hibernate.property
	 */
	public String getMaxvalue() {
		return maxvalue;
	}
	public void setMaxvalue(String maxvalue) {
		this.maxvalue = maxvalue;
	}
	/**
	 * @hibernate.property
	 */
	public int getMinlen() {
		return minlen;
	}
	public void setMinlen(int minlen) {
		this.minlen = minlen;
	}
	/**
	 * @hibernate.property
	 */
	public String getMinvalue() {
		return minvalue;
	}
	public void setMinvalue(String minvalue) {
		this.minvalue = minvalue;
	}
	/**
	 * @hibernate.property
	 */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @hibernate.property
	 */
	public boolean isReq() {
		return req;
	}
	public void setReq(boolean req) {
		this.req = req;
	}
	/**
	 * @hibernate.property type="net.ninecube.core.config.FieldTypeUserType"
	 */
	public FieldType getType() {
		return type;
	}
	public void setType(FieldType type) {
		this.type = type;
	}
	/**
	 * @hibernate.property type="net.ninecube.core.util.List2StringUserType"
	 */
	public List<String> getAction() {
		if (action == null) return Collections.emptyList();
		return this.action;
	}
	public void setAction(List<String> action) {
		this.action = action;
	}
	/**
	 * @hibernate.property
	 */
	public String getJspattern() {
		return jspattern;
	}
	public void setJspattern(String jspattern) {
		this.jspattern = jspattern;
	}
	/**
	 * @hibernate.property
	 */
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	/**
	 * @hibernate.property
	 */
	public String getPatternmsg() {
		return patternmsg;
	}
	public void setPatternmsg(String patternmsg) {
		this.patternmsg = patternmsg;
	}
	/**
	 * @hibernate.property
	 */
	public String getEntityName() {
		if (entityName == null) return this.getName();
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	/**
	 * @hibernate.property type="net.ninecube.core.query.OperatorUserType"
	 */
	public Operator getOperator() {
		return operator;
	}
	public void setOperator(Operator operator) {
		this.operator = operator;
	}
	/**
	 * @hibernate.property column="is_key"
	 */
	public boolean isKey() {
		return key;
	}
	public void setKey(boolean key) {
		this.key = key;
	}
	/**
	 * @hibernate.property type="net.ninecube.core.config.FieldTagTypeUserType"
	 */
	public FieldTagType getTagtype() {
		return tagtype;
	}
	public void setTagtype(FieldTagType tagType) {
		this.tagtype = tagType;
	}

	public void setMergeableFieldNames(List<String> providedFieldName) {
		// 重新构建为ArrayList的实例，以便可以调用mergeableFieldNames.remove()方法
		this.mergeableFieldNames = new ArrayList<String>(providedFieldName);
	}

	public String toString() {
		if (log.isDebugEnabled())
			return ToStringBuilder.reflectionToString(this);
		else
			return new ToStringBuilder(this).
				append("name", name).
				toString();
	}
}
