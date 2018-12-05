/**
 * 
 * created on 2007-1-10
 */
package net.ninecube.core.config.field;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.ninecube.core.config.FieldConf;
import net.ninecube.core.config.FieldSource;
import net.ninecube.core.config.impl.FieldConfImpl;
import net.ninecube.util.StringUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 排斥包含：引用明确指明的部分字段，如果该字段已被包含
 * 则先移除该字段再加入字段
 * @author jxb
 */
public class ExcludeFieldsConf extends FieldConfImpl implements ReferenceFieldsConf {
	private static final Log log = LogFactory.getLog(ExcludeFieldsConf.class);
	
	//~
	
	public List<FieldConf> resolve(FieldSource model) {
		this.removeMergableFieldName("value");
		List<FieldConf> ret = new ArrayList<FieldConf>();
		for (FieldConf f : model.getFields()) {
			if (this.getReferenceFields().contains(f.getName())) continue;
			FieldConf m = f.merge(this);
			ret.add(m);
		}
		return ret;
	}
	
	private List<String> getReferenceFields() {
		if (this.getValue() == null) return Collections.emptyList();
		return StringUtil.split(this.getValue());
	}
}
