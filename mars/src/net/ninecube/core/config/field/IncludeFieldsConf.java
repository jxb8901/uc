/**
 * 
 * created on 2007-1-10
 */
package net.ninecube.core.config.field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.ninecube.core.config.FieldConf;
import net.ninecube.core.config.FieldSource;
import net.ninecube.core.config.FieldTagType;
import net.ninecube.core.config.impl.FieldConfImpl;
import net.ninecube.util.StringUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 包含：引用明确指明的部分字段，如果该字段已被包含
 * 则先移除该字段再加入字段
 * 如果引用字段名称为*，表示引用所有字段，在excludefields中使用"*"引用无意义
 * @author jxb
 */
public class IncludeFieldsConf extends FieldConfImpl implements ReferenceFieldsConf {
	private static final Log log = LogFactory.getLog(IncludeFieldsConf.class);
	
	public static final IncludeFieldsConf DEFAULT_INPUT_INCLUDEALL = createIncludeAll(true);
	public static final IncludeFieldsConf DEFAULT_OUTPUT_INCLUDEALL = createIncludeAll(false);
	
	public static IncludeFieldsConf createIncludeAll(boolean input) {
		IncludeFieldsConf ret = new IncludeFieldsConf();
		ret.setValue("*");
		if (!input) {
			ret.setTagtype(FieldTagType.text);
			ret.setMergeableFieldNames(Arrays.asList("tagtype"));
		}
		return ret;
	}
	
	//~
	
	public List<FieldConf> resolve(FieldSource model) {
		this.removeMergableFieldName("value");
		List<FieldConf> ret = new ArrayList<FieldConf>();
		if (StringUtil.isEmpty(this.getValue()) || "*".equals(this.getValue().trim())) {
			for (FieldConf f : model.getFields()) {
				FieldConf m = f.merge(this);
				ret.add(m);
			}
		}
		else {
			for (String name : StringUtil.split(this.getValue())) {
				FieldConf f = model.resolveReference(name);
				FieldConf m = f.merge(this);
				ret.add(m);
			}
		}
		return ret;
	}
}
