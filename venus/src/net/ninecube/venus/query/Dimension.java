/**
 * 
 * created on 2007-4-18
 */
package net.ninecube.venus.query;

import net.ninecube.formula.Formula;
import net.ninecube.util.StringUtil;

/**
 * 
 * @author jxb
 */
public class Dimension {
	
	public static boolean isDefault(Dimension dim) {
		return isDefault(dim.getType(), dim.getValue());
	}
	
	public static boolean isDefault(String type, String value) {
		return StringUtil.isEmpty(type) || StringUtil.isEmpty(value) ||
			Formula.DEFAULT_DIMENSION_VALUE.equals(type) ||
			Formula.DEFAULT_DIMENSION_VALUE.equals(value);
	}

	/**
	 * @hibernate.property column="F_DimensionType"
	 */
	private String type;
	/**
	 * @hibernate.property column="F_Dimension"
	 */
	private String value;
	
	public String toString() {
		return this.type + ":" + this.value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
