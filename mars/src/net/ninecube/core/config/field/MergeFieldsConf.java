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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 合并字段
 * @author jxb
 */
public class MergeFieldsConf extends FieldConfImpl implements ReferenceFieldsConf {
	private static final Log log = LogFactory.getLog(MergeFieldsConf.class);

	public static final MergeFieldsConf DEFAULT_INPUT_MERGEKEY = createMergeKey(true);
	public static final MergeFieldsConf DEFAULT_OUTPUT_MERGEKEY = createMergeKey(false);

	/**
	 * 动态字段：用于指定引用主键字段
	 */
	public static final String DEFAULT_KEY_NAME = "$KEY$";

	public static MergeFieldsConf newMerge() {
		return new MergeFieldsConf();
	}
	
	public static MergeFieldsConf createMergeKey(boolean input) {
		MergeFieldsConf ret = MergeFieldsConf.newMerge();
		ret.setName(DEFAULT_KEY_NAME);
		ret.setTagtype(input ? FieldTagType.hidden : FieldTagType.radio);
		ret.setMergeableFieldNames(Arrays.asList("name", "tagtype"));
		return ret;
	}
	
	//~
	
	public List<FieldConf> resolve(FieldSource model) {
		List<FieldConf> ret = new ArrayList<FieldConf>();
		if (isDynamicMerge()) {
			FieldConf f = model.getKey();
			if (f != null) {
				this.removeMergableFieldName("name");
				FieldConf m = f.merge(this);
				ret.add(m);
			}
		}
		else {
			FieldConf f = model.resolveReference(this.getName());
			FieldConf m = f.merge(this);
			ret.add(m);
		}
		
		return ret;
	}
	
	private boolean isDynamicMerge() {
		return DEFAULT_KEY_NAME.equals(this.getName());
	}
}
