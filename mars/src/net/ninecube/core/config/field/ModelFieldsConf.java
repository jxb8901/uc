/**
 * 
 * created on 2007-1-10
 */
package net.ninecube.core.config.field;

import java.util.ArrayList;
import java.util.List;

import net.ninecube.core.config.ConfigException;
import net.ninecube.core.config.FieldConf;
import net.ninecube.core.config.FieldSource;
import net.ninecube.core.config.FieldSourceProvider;
import net.ninecube.core.config.FieldTagType;
import net.ninecube.core.config.FieldType;
import net.ninecube.core.config.impl.FieldConfImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 与venus.dtd中modelfields元素对应的类
 * 引用其它实体的字段
 * 如果引用字段名称为*，表示引用所有字段
 * @author jxb
 */
public class ModelFieldsConf extends FieldConfImpl {
	private static final Log log = LogFactory.getLog(ModelFieldsConf.class);
	private FieldSourceProvider config;
	private String modelName;
	private FieldSource model;
	private List<FieldConf> fields;
	private boolean multi = false;
	
	public ModelFieldsConf(FieldSourceProvider config, boolean multi) {
		this.config = config;
		this.setTagtype(FieldTagType.select);
		this.multi = multi;
		/* 注意这里要显示调用父类的方法 */
		super.setType(FieldType.model);
	}
	
	@Override
	public void setType(FieldType type) {
		throw new ConfigException("can't set 'type' attribute of modelfields: '" + type + "'");
	}
	
	//~
	
	/**
	 * selectmodel.ftl中会引用
	 */
	public FieldSource getModel() {
		if (this.model == null) this.model = config.getModel(this.modelName);
		if (model == null) throw new ConfigException("can't find model:'" + this.modelName + "'");
		return this.model;
	}
	
	/**
	 * 页面显示时调用，可取得需要显示的字段信息
	 */
	public List<FieldConf> getFields() {
		if (this.fields == null) resolve();
		return fields;
	}
	
	private List<FieldConf> resolve() {
		if (this.fields != null) return this.fields;
		this.removeMergableFieldName("name");
		this.removeMergableFieldName("cname");
		this.removeMergableFieldName("value");
		this.fields = new ArrayList<FieldConf>();
		for (FieldConf f : getModel().getFields()) {
			if (this.getReferenceFields().contains("*") || 
					this.getReferenceFields().contains(f.getName())) {
				FieldConf m = f.merge(this);
				this.fields.add(m);
			}
		}
		if (this.fields.isEmpty() && getModel().getKey() != null) {
			this.fields.add(this.getModel().getKey());
		}
		return this.fields;
	}
	
	private String getReferenceFields() {
		return this.getValue() == null ? this.getModel().getKey().getName() : this.getValue();
	}

	public void setModelName(String model) {
		this.modelName = model;
	}

	public boolean isMulti() {
		return multi;
	}

	public void setMulti(boolean multi) {
		this.multi = multi;
	}
}
