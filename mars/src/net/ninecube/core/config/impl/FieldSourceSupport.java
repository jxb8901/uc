/**
 * 
 * created on 2007-5-14
 */
package net.ninecube.core.config.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.ninecube.core.config.FieldConf;
import net.ninecube.core.config.FieldSource;
import net.ninecube.core.config.ReferenceNotFoundException;
import net.ninecube.core.config.field.ModelFieldsConf;

/**
 * 
 * @author jxb
 */
public class FieldSourceSupport implements FieldSource {
	private String name;
	private List<FieldConfImpl> fields = new ArrayList<FieldConfImpl>();
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public List<? extends FieldConf> getFields() {
		return Collections.unmodifiableList(fields);
	}

	public void addField(FieldConfImpl f) {
		this.fields.add(f);
	}

	public FieldConf getKey() {
		for (FieldConfImpl f : this.fields) {
			if (f.isKey()) return f;
		}
		return null;
	}
	
	public FieldConf resolveReference(String refname) {
		int index = refname.indexOf(".");
		if (index == -1) {
			for (FieldConfImpl f : fields) {
				if (f.getName().equals(refname)) return f;
			}
		}
		else {
			String refModelName = refname.substring(0, index);
			String refFieldName = refname.substring(index+1);
			for (FieldConfImpl f : fields) {
				if (f instanceof ModelFieldsConf && f.getName().equals(refModelName)) {
					return ((ModelFieldsConf)f).getModel().resolveReference(refFieldName);
				}
			}
		}
		throw new ReferenceNotFoundException("can't resolve reference ''"+refname+"' in model '" + this.name + "'");
	}

	void setFields(List<FieldConfImpl> fields) {
		this.fields = fields;
	}
}
