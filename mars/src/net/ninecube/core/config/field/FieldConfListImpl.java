/**
 * 
 * created on 2007-1-23
 */
package net.ninecube.core.config.field;

import java.util.LinkedList;
import java.util.List;

import net.ninecube.core.config.FieldConf;
import net.ninecube.core.config.FieldConfList;
import net.ninecube.core.config.FieldSource;
import net.ninecube.core.config.FieldTagType;
import net.ninecube.core.config.impl.FieldConfImpl;
import net.ninecube.util.ArrayUtil;
import net.ninecube.util.StringUtil;

/**
 * 
 * @author jxb
 * 
 */
public class FieldConfListImpl implements FieldConfList {
	private List<FieldConfImpl> defaultFields = new LinkedList<FieldConfImpl>();
	private List<FieldConfImpl> fields = new LinkedList<FieldConfImpl>();
	private List<FieldConf> resolvedFields = new LinkedList<FieldConf>();
	private boolean input = true;
	
	public void resolve(FieldSource m, boolean input) {
		resolve(m);
		this.input = input;
	}
	
	private void resolve(FieldSource m) {
		this.resolvedFields.clear();
		List<FieldConfImpl> fs = fields == null || fields.isEmpty() ? defaultFields : fields;
		for (FieldConfImpl f : fs) {
			if (f instanceof ReferenceFieldsConf) {
				List<FieldConf> ret = ((ReferenceFieldsConf)f).resolve(m);
				boolean replace = f instanceof MergeFieldsConf;
				for (FieldConf mf : ret) {
					excludeAdd(resolvedFields, mf, replace);
				}
			}
			else {
				excludeAdd(resolvedFields, f, false);
			}
		}
	}
	
	public void addDefaultField(FieldConfImpl field) {
		this.defaultFields.add(field);
	}
	
	//~ implements FieldConfList

	public List<FieldConfImpl> getRawFields() {
		return fields;
	}
	
	public FieldConf getByName(String name) {
		for (FieldConf f : resolvedFields) {
			if (name.equals(f.getName())) return f;
		}
		return null;
	}
	
	public FieldConf getByIndex(int index) {
		if (index >= 0 && index < this.resolvedFields.size())
			return this.resolvedFields.get(index);
		return null;
	}
	
	public List<? extends FieldConf> getValidateFields() {
		// TODO: 静态字段不需要验证吗？
		// 非静态字段
		return filterStaticFields(false);
	}
	
	public List<? extends FieldConf> getStaticFields() {
		// 静态字段
		return filterStaticFields(true);
	}
	
	public List<? extends FieldConf> getQueryFields() {
		// 所有字段
		return resolvedFields;
	}
	
	public List<? extends FieldConf> getViewFields() {
		// 输入：非静态字段，输出：全部字段
		return this.input ? filterStaticFields(false) : this.resolvedFields; 
	}
	
	public List<? extends FieldConf> getDataFields() {
		// 所有字段(不包括客户端不可修改的字段：文本字段)
		return filterUpdateableFields();
	}
	
	private List<? extends FieldConf> filterStaticFields(boolean onlyStatic) {
		List<FieldConf> ret = new LinkedList<FieldConf>();
		for (FieldConf f : resolvedFields) {
			// 只取静态字段
			if (onlyStatic && isStaticField(f)) ret.add(f);
			// 只取非静态字段
			else if (!onlyStatic && !isStaticField(f)) ret.add(f);
		}
		return ret;
	}
	
	private List<? extends FieldConf> filterUpdateableFields() {
		List<FieldConf> ret = new LinkedList<FieldConf>();
		for (FieldConf f : resolvedFields) {
			// 只取可修改的字段
			if (isUpdateableField(f)) ret.add(f);
		}
		return ret;
	}
	
	//~ 
	
	public void add(FieldConfImpl f) {
		this.fields.add(f);
	}
	
	protected static boolean isStaticField(FieldConf f) {
		return !(f instanceof ModelFieldsConf) && !StringUtil.isEmpty(f.getValue());
	}
	
	protected static boolean isUpdateableField(FieldConf f) {
		return ! ArrayUtil.in(f.getTagtype(), new FieldTagType[]{
			FieldTagType.text });
	}
	
	protected static <T extends FieldConf> void excludeAdd(List<T> list, T f, boolean replace) {
		int removeIndex = -1;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getName().equals(f.getName())) {
				removeIndex = i;
				break;
			}
		}
		if (removeIndex == -1 || !replace) {
			list.add(f);
		}
		else {
			list.remove(removeIndex);
			list.add(removeIndex, f);
		}
	}
}
